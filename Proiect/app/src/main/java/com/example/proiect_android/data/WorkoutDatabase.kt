package com.example.proiect_android.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.proiect_android.model.Exercise
import com.example.proiect_android.model.Workout
import com.example.proiect_android.utils.Constants.SEED_DB

@Database(entities = [Exercise::class, Workout::class], version = 1, exportSchema = false)
abstract class WorkoutDatabase: RoomDatabase() {

    abstract fun exerciseDao() : ExerciseDao
    abstract fun workoutDao(): WorkoutDao

    companion object{
        @Volatile
        private var INSTANCE: WorkoutDatabase? = null
        private const val databaseName = "workout_database"

        fun getDatabase(context: Context): WorkoutDatabase
        {
            val tempInstance = INSTANCE

            if (tempInstance != null)
            {
                return tempInstance
            }
            synchronized(this)
            {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WorkoutDatabase::class.java,
                    databaseName
                ).createFromAsset(SEED_DB).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}