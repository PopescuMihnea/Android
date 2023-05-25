package com.example.proiect_android.api

import android.util.Log
import com.example.proiect_android.model.Exercise
import com.example.proiect_android.utils.Constants.X_API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers

interface ExerciseApi {

    @GET("exercises")
    @Headers("x-api-key: $X_API_KEY")
    suspend fun getExercises() : Response<List<Exercise>>
}