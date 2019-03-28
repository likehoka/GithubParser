package com.example.githubparser.Model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("login")
    val login: String,
    @SerializedName("avatar_url")
    val avatar_url: String,
    @SerializedName("html_url")
    val html_url: String
)