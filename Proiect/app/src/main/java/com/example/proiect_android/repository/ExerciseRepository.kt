package com.example.proiect_android.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.proiect_android.api.RetrofitInstance
import com.example.proiect_android.model.Exercise
import com.example.proiect_android.data.ExerciseDao
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class ExerciseRepository(private val exerciseDao: ExerciseDao) {

   fun readAllData() : Flow<List<Exercise>>
   {
       return exerciseDao.readAllData()
   }

    fun getExercise(id: Int): Flow<Exercise>
    {
        return exerciseDao.getExercise(id)
    }

    suspend fun insertExercise(exercise: Exercise)
    {
        exerciseDao.insertExercise(exercise)
    }

    suspend fun updateExercise(exercise: Exercise)
    {
        exerciseDao.updateExercise(exercise)
    }

    suspend fun deleteExercise(exercise: Exercise)
    {
        exerciseDao.deleteExercise(exercise)
    }

    fun searchDatabase(searchQuery : String) : Flow<List<Exercise>>
    {
        return exerciseDao.searchDatabase(searchQuery)
    }

    fun count(id: Int): Flow<Int>
    {
        return exerciseDao.count(id)
    }

    suspend fun getExercises() : Response<List<Exercise>>
    {
        return RetrofitInstance.api.getExercises()
    }

}