package com.example.githubparser.api

import com.example.githubparser.model.User
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*

class StargazersList(
    var owner: String = "",
    var repository: String = "",
    @SerializedName("starred_at")
    val stringDate: String,
    @SerializedName("documentation_url")
    val documentation_url: String,
    @SerializedName("user")
    val user: User
) {
    companion object {
        private val formatter = SimpleDateFormat("yyyy-MM-dd")
    }

    fun getDate(): Date {
        return formatter.parse(stringDate)
    }

}