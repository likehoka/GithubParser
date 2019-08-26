package com.example.githubparser.api

import com.example.githubparser.model.User
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*

class StargazersList(
    var repository: String = "",
    var idOwner: Long = 0,
    @SerializedName("starred_at")
    val stringDate: String,
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