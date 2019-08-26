package com.example.githubparser.utils

import com.example.githubparser.Database.objectbox.ObjectBox
import com.example.githubparser.model.Stargazers
import io.objectbox.kotlin.boxFor

class UsersStorage {
    private var stargazersBox = ObjectBox.boxStore.boxFor<Stargazers>()

    fun getUsers(ownerId: Long): MutableSet<String>? {
        val listStargazers = getAllStargazersList()
        var listValue: List<String> = listOf()
        listStargazers.forEach { it ->
            if (it.idRepository == ownerId) {
                listValue +=  it.username.split(",").map { it.trim() }
            }
        }
        if (listValue.size > 100){
            return listValue.subList(listValue.size-100, listValue.size).toMutableSet()
        }
        return listValue.toMutableSet()
    }

    fun getStargazersList(ownerId: Long, stringDate: String) : MutableSet<String>? {
        val notes = getAllStargazersList()
        var listValue: List<String> = listOf()
        notes.forEach {
            if (it.idRepository == ownerId && it.stringDate == stringDate) {
                listValue +=  it.stringDate
            }
        }
        return listValue.toMutableSet()
    }


    fun getAllStargazersList(): MutableList<Stargazers> {
        return stargazersBox.query().build().find()
    }

    fun setStargazers(stargazer: Stargazers) {
        stargazersBox.put(stargazer)
    }
}