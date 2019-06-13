package com.example.githubparser.utils

import android.util.Log
import com.example.githubparser.model.Stargazers
import com.example.githubparser.model.StargazersSize

class GraphCounter() {
    fun getData(month: Int, year: Int, stargazers: List<Stargazers>) {
        val yearNow: Int = month
        val monthNow: Int = year

        Log.d("test", "stargazer Graph size: ${stargazers.size}")
        for (stargazer in stargazers) {
            Log.d("test", "Graph List: ${stargazer.stringDate}")
        }

    }
}