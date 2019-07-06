package com.example.githubparser

import android.app.Application
import com.example.githubparser.Database.objectbox.ObjectBox
import com.example.githubparser.api.StargazersApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://api.github.com/repos/"

class ParserApplication : Application() {


    private lateinit var retrofit: Retrofit

    override fun onCreate() {
        super.onCreate()
        retrofit = initRetrofit()
        ObjectBox.init(this)

        //val stargezersApi = retrofit.create(StargazersApi::class.java)
    }

    private fun initRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

}