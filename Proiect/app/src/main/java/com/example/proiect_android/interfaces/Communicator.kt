package com.example.proiect_android.interfaces

import com.example.proiect_android.model.Workout

interface Communicator {
    fun passWorkout(workout: Workout)
}