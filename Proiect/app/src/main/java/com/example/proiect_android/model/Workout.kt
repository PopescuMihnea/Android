package com.example.proiect_android.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "workout_table")
@Parcelize
data class Workout(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var name: String
) : Parcelable
