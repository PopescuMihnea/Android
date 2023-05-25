package com.example.proiect_android.repository

import com.example.proiect_android.data.WorkoutDao
import com.example.proiect_android.model.Exercise
import com.example.proiect_android.model.Workout
import kotlinx.coroutines.flow.Flow

class WorkoutRepository(private val workoutDao: WorkoutDao) {
    fun readAllData(): Flow<List<Workout>>
    {
        return workoutDao.readAllData()
    }

    fun getWorkout(id: Int): Flow<Workout?>
    {
        return workoutDao.getWorkout(id)
    }

    suspend fun insertWorkout(workout: Workout)
    {
        workoutDao.insertWorkout(workout)
    }

    suspend fun updateWorkout(workout: Workout)
    {
        workoutDao.updateWorkout(workout)
    }

    suspend fun deleteWorkout(workout: Workout)
    {
        workoutDao.deleteWorkout(workout)
    }
}