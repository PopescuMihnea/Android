package com.example.proiect_android.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.proiect_android.R
import com.example.proiect_android.model.Workout
import com.example.proiect_android.viewmodel.WorkoutsViewModel

class CreateWorkoutActivity : AppCompatActivity() {
    private lateinit var operationText : TextView
    private lateinit var workoutName : EditText
    private lateinit var modifyButton : Button
    private lateinit var workoutViewModel : WorkoutsViewModel
    private val fromWorkoutKey = "fromWorkouts"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modify_workout)

        operationText = findViewById(R.id.operation_text)
        workoutName = findViewById(R.id.workout_name)
        modifyButton = findViewById(R.id.add_workout_button)
        workoutViewModel = ViewModelProvider(this)[WorkoutsViewModel::class.java]

        modifyButton.setOnClickListener {
            if (checkInput(workoutName.text.toString()))
            {
                val workout = Workout(0, workoutName.text.toString())
                workoutViewModel.insertWorkout(workout)
                Toast.makeText(this, "Created workout", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra(fromWorkoutKey, true)
                startActivity(intent)
            }
        }
    }

    private fun checkInput(workoutName : String): Boolean{
        return !TextUtils.isEmpty(workoutName)
    }
}