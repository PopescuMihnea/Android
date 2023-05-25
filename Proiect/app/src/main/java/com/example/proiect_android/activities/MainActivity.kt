package com.example.proiect_android.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.proiect_android.fragments.ExercisesFragment
import com.example.proiect_android.R
import com.example.proiect_android.fragments.WorkoutsFragment
import com.example.proiect_android.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var bottomNavigation : BottomNavigationView
    private val fromWorkoutsKey = "fromWorkouts"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("STATE", "In main activity")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val bundle = intent.extras
        bottomNavigation = findViewById(R.id.bottomNavigationView)

        if (bundle != null && bundle.containsKey(fromWorkoutsKey))
        {
            setFragment(WorkoutsFragment())
        }
        else
        {
            setFragment(ExercisesFragment())
            bottomNavigation.selectedItemId = R.id.exercises_nav
        }

        binding.bottomNavigationView.setOnItemSelectedListener{
            when(it.itemId)
            {
                R.id.exercises_nav -> setFragment(ExercisesFragment())
                R.id.workout_nav -> setFragment(WorkoutsFragment())
                else -> {}
            }

            true
        }
    }

    private fun setFragment(fragment : Fragment)
    {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.exercises_frame_layout, fragment)
        fragmentTransaction.commit()
    }

}