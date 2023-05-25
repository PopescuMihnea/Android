package com.example.proiect_android.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.proiect_android.model.Workout
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao {
    @Query("SELECT * FROM workout_table")
    fun readAllData(): Flow<List<Workout>>

    @Query("SELECT * FROM workout_table WHERE id = :id")
    fun getWorkout(id : Int): Flow<Workout?>

    @Insert()
    suspend fun insertWorkout(workout: Workout)

    @Update()
    suspend fun updateWorkout(workout: Workout)

    @Delete()
    suspend fun deleteWorkout(workout: Workout)

}