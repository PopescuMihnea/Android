package com.example.proiect_android.activities

import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.constraintlayout.motion.widget.MotionLayout
import com.example.proiect_android.R
import com.example.proiect_android.model.Workout

class SplashActivity : AppCompatActivity() {

    private lateinit var motionLayout : MotionLayout
    private val preferencesName = "Preferences"
    private val refreshedDataKey = "refreshedData"
    private val fromWorkoutUriKey = "fromWorkoutUri"
    private val workoutKey = "workout"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val sharedPreferences: SharedPreferences = getSharedPreferences(preferencesName, Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean(refreshedDataKey, false).apply()

        val bundle = intent.extras

        motionLayout = findViewById(R.id.motion_layout)
        motionLayout.startLayoutAnimation()

        motionLayout.setTransitionListener(object : MotionLayout.TransitionListener{
            override fun onTransitionStarted(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int
            ) {

            }

            override fun onTransitionChange(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int,
                progress: Float
            ) {

            }

            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                val intent : Intent =
                    if (bundle != null && bundle.getBoolean(fromWorkoutUriKey, false)) {
                    val tempIntent = Intent(this@SplashActivity, WorkoutActivity::class.java)
                    val workout = bundle.getParcelable<Workout>(workoutKey)

                    if (workout != null)
                    {
                        val stackBuilder = TaskStackBuilder.create(this@SplashActivity)
                        stackBuilder.addParentStack(WorkoutActivity::class.java)
                        stackBuilder.addNextIntent(tempIntent)

                        tempIntent.putExtra(workoutKey, workout)
                        tempIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        tempIntent
                    }
                    else
                    {
                        Intent(this@SplashActivity, MainActivity::class.java)
                    }

                } else {
                    Intent(this@SplashActivity, MainActivity::class.java)
                }

                if (bundle != null && bundle.getParcelable<Workout>(workoutKey) == null)
                {
                    Toast.makeText(this@SplashActivity, "Workout has been deleted", Toast.LENGTH_LONG).show()
                }

                startActivity(intent)
                finish()
            }

            override fun onTransitionTrigger(
                motionLayout: MotionLayout?,
                triggerId: Int,
                positive: Boolean,
                progress: Float
            ) {

            }

        })
    }
}