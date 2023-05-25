package com.example.proiect_android.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.proiect_android.R
import com.example.proiect_android.model.Workout

class WorkoutFragment : Fragment() {

    private lateinit var workoutName: TextView
    private lateinit var notificationButton : Button
    private val workoutKey = "workout"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_workout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        workoutName = view.findViewById(R.id.textView)
        notificationButton = view.findViewById(R.id.workout_notification_button)

        val bundle = this.arguments
        val workout = bundle!!.getParcelable<Workout>(workoutKey)
        workoutName.text = workout!!.name

        notificationButton.setOnClickListener {
            val transaction=requireActivity().supportFragmentManager
                .beginTransaction()
            val notificationFragment = WorkoutNotification()
            val bundle = Bundle()
            bundle.putParcelable(workoutKey, workout)
            notificationFragment.arguments = bundle
            transaction.replace(R.id.workout_frame_layout, notificationFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }
}