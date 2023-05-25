package com.example.proiect_android.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proiect_android.R
import com.example.proiect_android.activities.CreateWorkoutActivity
import com.example.proiect_android.adapters.WorkoutRecyclerAdapter
import com.example.proiect_android.interfaces.Communicator
import com.example.proiect_android.model.Workout
import com.example.proiect_android.viewmodel.WorkoutsViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class WorkoutsFragment : Fragment(), Communicator {

    private lateinit var workoutRecyclerView: RecyclerView
    private lateinit var workoutsViewModel: WorkoutsViewModel
    private lateinit var addButton : FloatingActionButton
    private var adapter = WorkoutRecyclerAdapter(this@WorkoutsFragment)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_workouts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addButton = view.findViewById(R.id.addButton)
        workoutRecyclerView = view.findViewById(R.id.recyclerView)

        workoutRecyclerView.adapter = adapter
        workoutRecyclerView.layoutManager = LinearLayoutManager(context)

        addButton.setOnClickListener{
            val intent = Intent(activity, CreateWorkoutActivity::class.java)
            requireActivity().startActivity(intent)
        }

        workoutsViewModel = ViewModelProvider(this)[WorkoutsViewModel::class.java]
        workoutsViewModel.readAllData.observe(viewLifecycleOwner){
            workouts -> adapter.setData(workouts)
        }
    }

    override fun passWorkout(workout: Workout) {
        workoutsViewModel.deleteWorkout(workout)
    }

}