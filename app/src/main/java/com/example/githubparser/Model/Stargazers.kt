package com.example.githubparser.Model

import com.google.gson.annotations.SerializedName

data class Stargazers(
    @SerializedName("starred_at")
    val starred_at: String,
    @SerializedName("user")
    val user: User
)