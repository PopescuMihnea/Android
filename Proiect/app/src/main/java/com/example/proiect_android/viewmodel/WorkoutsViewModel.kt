package com.example.proiect_android.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.proiect_android.data.WorkoutDatabase
import com.example.proiect_android.model.Exercise
import com.example.proiect_android.model.Workout
import com.example.proiect_android.repository.WorkoutRepository
import kotlinx.coroutines.launch

class WorkoutsViewModel(application: Application): AndroidViewModel(application) {
    private val repository: WorkoutRepository
    val readAllData: LiveData<List<Workout>>

    init{
        val workoutDao = WorkoutDatabase.getDatabase(application).workoutDao()
        repository = WorkoutRepository(workoutDao)
        readAllData = repository.readAllData().asLiveData()
    }

    fun getWorkout(id: Int): LiveData<Workout?>
    {
        return repository.getWorkout(id).asLiveData()
    }

    fun insertWorkout(workout: Workout)
    {
        viewModelScope.launch {
            repository.insertWorkout(workout)
        }
    }

    fun updateWorkout(workout: Workout)
    {
        viewModelScope.launch {
            repository.updateWorkout(workout)
        }
    }

    fun deleteWorkout(workout: Workout)
    {
        viewModelScope.launch {
            repository.deleteWorkout(workout)
        }
    }
}