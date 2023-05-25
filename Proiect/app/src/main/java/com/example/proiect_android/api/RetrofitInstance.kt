package com.example.proiect_android.api

import com.example.proiect_android.utils.Constants.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api : ExerciseApi by lazy {
        retrofit.create(ExerciseApi::class.java)
    }

}