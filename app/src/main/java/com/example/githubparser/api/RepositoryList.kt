package com.example.githubparser.api

import com.google.gson.annotations.SerializedName

data class RepositoryList(
    @SerializedName("owner")
    val ownerName: String = "ownerName",
    @SerializedName("repository")
    val repositoryName: String = "repositoryName"
)