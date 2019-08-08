package com.example.githubparser.api

import com.example.githubparser.model.ErrorMessage
import com.example.githubparser.model.User
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

private const val BASE_URL = "https://api.github.com/users/"

interface UserApi {

    @GET("{user}")
    fun getUser(@Path("user") user: String) : Call<List<ErrorMessage>>
    companion object {
        operator fun invoke(): UserApi {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(UserApi::class.java)
        }
    }
}