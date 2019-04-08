package com.example.githubparser.model

import com.google.gson.annotations.SerializedName

data class Stargazers (

    @SerializedName("starred_at")
    val date: String,

    @SerializedName("user")
    val user: User

)