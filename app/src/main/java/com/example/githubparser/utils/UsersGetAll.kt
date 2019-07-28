package com.example.githubparser.utils

import com.example.githubparser.Database.objectbox.ObjectBox
import com.example.githubparser.model.Stargazers
import io.objectbox.kotlin.boxFor

class UsersGetAll {
    var notesStargazers = ObjectBox.boxStore.boxFor<Stargazers>()

    fun getallUsers(ownerName: String, repositoryName: String): List<String> {
        val notes = getStargazersObjectbox()
        var starsCount: Int = 0
        var listValue: List<String> = listOf()
        notes.forEach {
            if (it.owner == ownerName && it.repository == repositoryName) {
                starsCount += it.likes
                val listValueCash: List<String> = it.username.split(",").map { it -> it.trim() }
                listValue += listValueCash
            }
        }
        return listValue
    }

    fun getStargazersObjectbox(): MutableList<Stargazers> {
        val notes = notesStargazers.query().build().find()
        return notes
    }

    fun setStargazersObjectbox(stargazer: Stargazers) {
        notesStargazers.put(stargazer)
    }
}