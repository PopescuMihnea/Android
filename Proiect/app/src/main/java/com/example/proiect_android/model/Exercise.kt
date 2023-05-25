package com.example.proiect_android.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "exercise_table")
@Parcelize
data class Exercise(
    @PrimaryKey(autoGenerate = true)
    val id : Int,
    var exerciseImage: String,
    var exerciseName: String,
    var exerciseCategory: String,
    var exerciseDescription : String,
    var exerciseVideo : String,
    var exerciseShareVideo : String,
    var exerciseUserVideo: String?
    ) : Parcelable
