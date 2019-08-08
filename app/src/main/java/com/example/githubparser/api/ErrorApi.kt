package com.example.githubparser.api

import com.example.githubparser.model.ErrorMessage
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

private const val BASE_URL = "https://api.github.com/repos/"
interface ErrorApi {
    @GET("{owner}/{repository}/stargazers?per_page=100")
    @Headers("Accept: application/vnd.github.v3.star+json")
    fun getStargazers(@Path("owner") ownerName: String,
                      @Path("repository") repositoryName: String,
                      @Query("page") counterStargazers: String): Call<List<ErrorMessage>>

    companion object {
        operator fun invoke(): ErrorApi {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ErrorApi::class.java)
        }
    }
}