package com.example.githubparser.model

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToMany
import java.text.SimpleDateFormat
import java.util.*

@Entity
class Stargazers(
    @Id var id: Long = 0,
    val owner: String = "",
    val repository: String = "",
    val stringDate: String = "",
    val month: String = "",
    val year: String = "",
    var likes: Int = 0,
    var username: String = ""
) {
    companion object {
        private val formatter = SimpleDateFormat("yyyy-MM-dd")
    }


    val user: ToMany<User> = ToMany(this, Stargazers_.user)

    constructor(
        id: Long = 0,
        owner: String = "",
        repository: String = "",
        stringDate: String = "",
        month: String = "",
        year: String = "",
        likes: Int = 0,
        username: String = "",
        user: List<User> = emptyList()
    ) : this(id, owner, repository, stringDate, month, year, likes, username) {
        this.user.addAll(user)
    }

    fun getDate(): Date {
        return formatter.parse(stringDate)
    }
}