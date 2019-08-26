package com.example.githubparser.model

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToMany

@Entity
class Stargazers(
    @Id var id: Long = 0,
    val idRepository: Long = 0,
    val stringDate: String = "",
    val month: String = "",
    val year: String = "",
    var likes: Int = 0,
    var username: String = ""
) {
    val user: ToMany<User> = ToMany(this, Stargazers_.user)

    constructor(
        id: Long = 0,
        idRepository: Long = 0,
        stringDate: String = "",
        month: String = "",
        year: String = "",
        likes: Int = 0,
        username: String = "",
        user: List<User> = emptyList()
    ) : this(id, idRepository, stringDate, month, year, likes, username) {
        this.user.addAll(user)
    }
}