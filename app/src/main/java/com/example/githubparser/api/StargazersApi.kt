package com.example.githubparser.api

import com.example.githubparser.model.Stargazers
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path


private const val BASE_URL = "https://api.github.com/repos/"
//Our URL is "https://api.github.com/repos/Omega-R/OmegaRecyclerView/stargazers"
interface StargazersApi {


    @GET("{owner}/{repository}/stargazers?per_page=100&page=0")
    @Headers("Accept: application/vnd.github.v3.star+json")
    fun getStargazers(@Path("owner") ownerName: String,
                      @Path("repository") repositoryName: String): Call<List<Stargazers>>

    companion object {
        operator fun invoke(): StargazersApi {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(StargazersApi::class.java)
        }
    }
}