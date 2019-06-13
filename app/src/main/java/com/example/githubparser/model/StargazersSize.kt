package com.example.githubparser.model

import android.util.Log
import com.google.gson.annotations.SerializedName
import java.time.Month
import java.time.Year


class StargazersSize() {
    var year: Int = 0
    var month: Int = 0
    var stargazersCounter: Int = 0

    fun setDataStargazer(month: Int, year: Int) {
        this.year = year
        this.month = month
        this.stargazersCounter += 1
        Log.d("test", "setDataStargazer: year: ${this.year}, month: ${this.month}")
        //return StargazersSize()
    }
}
