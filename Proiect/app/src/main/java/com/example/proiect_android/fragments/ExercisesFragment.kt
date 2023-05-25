package com.example.proiect_android.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proiect_android.R
import com.example.proiect_android.adapters.ExerciseRecyclerAdapter
import com.example.proiect_android.viewmodel.ExercisesViewModel
import com.example.proiect_android.Extensions.Companion.observeOnce

class ExercisesFragment : Fragment() {
    private lateinit var exerciseRecyclerView : RecyclerView
    private lateinit var exercisesViewModel: ExercisesViewModel
    private lateinit var searchView : SearchView
    private lateinit var updateTextView : TextView
    private lateinit var loadingBar: ProgressBar
    private val adapter = ExerciseRecyclerAdapter()
    private val preferencesName = "Preferences"
    private val refreshedDataKey = "refreshedData"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchView = view.findViewById(R.id.searchView)
        exerciseRecyclerView = view.findViewById(R.id.recyclerView)
        updateTextView = view.findViewById(R.id.updating_text)
        loadingBar = view.findViewById(R.id.loading_bar)

        exerciseRecyclerView.adapter = adapter
        exerciseRecyclerView.layoutManager = LinearLayoutManager(context)

        exercisesViewModel = ViewModelProvider(this)[ExercisesViewModel::class.java]
        exercisesViewModel.readAllData.observe(viewLifecycleOwner) { exercises ->
            adapter.setData(exercises)
        }

        val sharedPreferences: SharedPreferences = this.requireActivity()
            .getSharedPreferences(preferencesName, Context.MODE_PRIVATE)

        if (!sharedPreferences.getBoolean(refreshedDataKey, false))
        {
            exercisesViewModel.getExercises()
            hideRecyclerView()
            exercisesViewModel.exercisesResponse.observe(viewLifecycleOwner) { res ->
                if (res.isSuccessful)
                {
                    res.body()?.forEach { exercise ->
                        exercisesViewModel.count(exercise.id)
                            .observeOnce(viewLifecycleOwner, Observer { count ->
                                if (count > 0)
                                {
                                    exercisesViewModel.getExercise(exercise.id)
                                        .observeOnce(viewLifecycleOwner,
                                            Observer { oldExercise ->
                                                exercise.exerciseUserVideo =
                                                    oldExercise.exerciseUserVideo
                                                exercisesViewModel.updateExercise(exercise)
                                            })
                                }
                                else
                                {
                                    exercisesViewModel.insertExercise(exercise)
                                }
                            })
                    }
                }
                else
                {
                    Log.d("Response", res.errorBody().toString())
                    Toast.makeText(activity, "Could not get exercises", Toast.LENGTH_SHORT).show()
                }
                showRecyclerView()
            }

            sharedPreferences.edit().putBoolean(refreshedDataKey, true).apply()
        }


        searchView.setOnQueryTextListener(object : OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null)
                {
                    searchDatabase(query)
                }

                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                if (query != null)
                {
                    searchDatabase(query)
                }

                return true
            }

        })

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_exercises, container, false)
    }

    private fun searchDatabase(query : String)
    {
        val searchQuery = "%$query%"

        exercisesViewModel.searchDatabase(searchQuery).observe(viewLifecycleOwner) { list ->
            list.let{
                adapter.setData(it)
            }
        }
    }

    private fun hideRecyclerView()
    {
        exerciseRecyclerView.visibility = View.GONE
        searchView.visibility = View.GONE
        updateTextView.visibility = View.VISIBLE
        loadingBar.visibility = View.VISIBLE
    }

    private fun showRecyclerView()
    {
        exerciseRecyclerView.visibility = View.VISIBLE
        searchView.visibility = View.VISIBLE
        updateTextView.visibility = View.GONE
        loadingBar.visibility = View.GONE
    }
}