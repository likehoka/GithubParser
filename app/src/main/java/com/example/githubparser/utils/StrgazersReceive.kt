package com.example.githubparser.utils

import android.util.Log
import android.widget.Toast
import com.example.githubparser.api.StargazersApi
import com.example.githubparser.api.StargazersList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StrgazersReceive () {
    /*
    private fun fetchStargazersRetrofit(ownerName: String, repositoryName: String, counterStargazers: Long) {
        this.counterStargazers = counterStargazers
        StargazersApi().getStargazers(ownerName, repositoryName, counterStargazers.toString())
            .enqueue(object : Callback<List<StargazersList>> {
                override fun onFailure(call: Call<List<StargazersList>>, t: Throwable) {
                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<List<StargazersList>>, response: Response<List<StargazersList>>) {
                    Log.d("test", response.body().toString())
                    if (response.body() == null) {
                        Toast.makeText(applicationContext, "This repository is failed", Toast.LENGTH_LONG).show()
                    } else
                        stargazersCounter(response.body(), ownerName, repositoryName)
                }
            })
    } */
}