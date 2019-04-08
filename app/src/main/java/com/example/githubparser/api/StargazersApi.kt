package com.example.githubparser.api

import com.example.githubparser.model.Stargazers
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers

const val BASE_URL = "https://api.github.com/repos/Omega-R/OmegaRecyclerView/"

interface StargazersApi {

    @GET("stargazers")
    @Headers("Accept: application/vnd.github.v3.star+json")
    fun getStargazers() : Call<List<Stargazers>>

    companion object {
        operator fun invoke() : StargazersApi{
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(StargazersApi::class.java)//для singlton StargazersApi
        }
    }
}