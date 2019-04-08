package com.example.githubparser.model

import com.google.gson.annotations.SerializedName

//моделька для дальнейшего запроса ранее запрошенных репозиториев
//будем запрашивать из БД
data class Repository(

    @SerializedName("owner")
    val ownerName: String = "ownerName",

    @SerializedName("repository")
    val repositoryName: String = "repositoryName"

)