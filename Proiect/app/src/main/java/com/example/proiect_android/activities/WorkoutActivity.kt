package com.example.proiect_android.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.proiect_android.Extensions.Companion.observeOnce
import com.example.proiect_android.R
import com.example.proiect_android.fragments.WorkoutFragment
import com.example.proiect_android.model.Workout
import com.example.proiect_android.viewmodel.WorkoutsViewModel

class WorkoutActivity : AppCompatActivity() {
    private lateinit var workoutsViewModel: WorkoutsViewModel
    private val workoutKey = "workout"
    private val fromWorkoutUriKey = "fromWorkoutUri"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workout)

        val uri = intent.data;

        if (uri != null) {
            val parameters = uri.pathSegments
            val workoutId : Int = parameters[parameters.size - 1].toInt()
            workoutsViewModel = ViewModelProvider(this)[WorkoutsViewModel::class.java]
            workoutsViewModel.getWorkout(workoutId).observeOnce(this, Observer{
                workout ->
                val intent = Intent(this, SplashActivity::class.java)
                intent.putExtra(fromWorkoutUriKey, true)
                intent.putExtra(workoutKey, workout)
                startActivity(intent)
            })
        }
        else
        {
            val intentBundle : Bundle? = intent.extras
            val workout = intentBundle!!.getParcelable<Workout>(workoutKey)

            val transaction=supportFragmentManager.beginTransaction()
            val workoutFragment = WorkoutFragment()
            val bundle = Bundle()
            bundle.putParcelable("workout", workout)
            workoutFragment.arguments = bundle
            transaction.add(R.id.workout_frame_layout, workoutFragment).commit()
        }
    }
}