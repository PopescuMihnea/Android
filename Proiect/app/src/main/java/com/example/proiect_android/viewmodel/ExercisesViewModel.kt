package com.example.proiect_android.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.proiect_android.model.Exercise
import com.example.proiect_android.repository.ExerciseRepository
import com.example.proiect_android.data.WorkoutDatabase
import kotlinx.coroutines.launch
import retrofit2.Response

class ExercisesViewModel(application: Application): AndroidViewModel(application) {
    private val repository: ExerciseRepository
    val readAllData : LiveData<List<Exercise>>
    val exercisesResponse : MutableLiveData<Response<List<Exercise>>> = MutableLiveData()

    init{
        val exerciseDao = WorkoutDatabase.getDatabase(application).exerciseDao()
        repository = ExerciseRepository(exerciseDao)
        readAllData = repository.readAllData().asLiveData()
    }

    fun getExercise(id: Int): LiveData<Exercise>
    {
        return repository.getExercise(id).asLiveData()
    }

    fun insertExercise(exercise: Exercise)
    {
        viewModelScope.launch {
            repository.insertExercise(exercise)
        }
    }

    fun updateExercise(exercise: Exercise)
    {
        viewModelScope.launch {
            repository.updateExercise(exercise)
        }
    }

    fun deleteExercise(exercise: Exercise)
    {
        viewModelScope.launch {
            repository.deleteExercise(exercise)
        }
    }

    fun searchDatabase(searchQuery : String) : LiveData<List<Exercise>>
    {
        return repository.searchDatabase(searchQuery).asLiveData()
    }

    fun count(id : Int): LiveData<Int>
    {
        var res : LiveData<Int> = MutableLiveData()
        viewModelScope.launch {
            res = repository.count(id).asLiveData()
        }

        return res
    }

    fun getExercises()
    {
        viewModelScope.launch {
            val response = repository.getExercises()
            exercisesResponse.value = response
        }
    }

}