package com.example.githubparser.model

import com.google.gson.annotations.SerializedName

data class Repository(

    @SerializedName("owner")
    val ownerName: String = "ownerName",

    @SerializedName("repository")
    val repositoryName: String = "repositoryName"

)