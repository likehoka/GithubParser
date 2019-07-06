package com.example.githubparser.model

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
data class Repository(
    @Id var id: Long = 0,
    val ownerName: String = "ownerName",
    val repositoryName: String = "repositoryName"

)