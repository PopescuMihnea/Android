package com.example.proiect_android.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.proiect_android.model.Exercise
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseDao {

    @Query("SELECT * FROM exercise_table")
    fun readAllData(): Flow<List<Exercise>>

    @Query("SELECT * FROM exercise_table WHERE id = :id")
    fun getExercise(id: Int): Flow<Exercise>

    @Insert()
    suspend fun insertExercise(exercise: Exercise)

    @Update()
    suspend fun updateExercise(exercise: Exercise)

    @Delete()
    suspend fun deleteExercise(exercise: Exercise)

    @Query("SELECT * FROM exercise_table WHERE exerciseName LIKE :searchQuery OR exerciseCategory LIKE :searchQuery")
    fun searchDatabase(searchQuery : String) : Flow<List<Exercise>>

    @Query("SELECT COUNT() FROM exercise_table WHERE id = :id")
    fun count(id: Int): Flow<Int>
}