package com.example.githubparser.utils

import android.util.Log
import com.example.githubparser.Database.objectbox.ObjectBox
import com.example.githubparser.model.Stargazers
import io.objectbox.kotlin.boxFor

class UsersGetAll {
    var notesStargazers = ObjectBox.boxStore.boxFor<Stargazers>()

    fun getallUserss(ownerName: String, repositoryName: String): List<String> {
        val notes = getStargazersObjectbox()
        var listValue: List<String> = listOf()
        var returnList: List<String> = listOf()
        notes.forEach {
            if (it.owner == ownerName && it.repository == repositoryName) {
                //listSet = it.usernames
                listValue +=  it.username.split(",").map { it -> it.trim() }
            }
        }
        if (listValue.size > 100){
            listValue.subList(listValue.size-100, listValue.size).forEach {
                returnList += it
            }
            Log.d("test", "if (listValue.size > 100){")
        }

        if (listValue.size <= 100) {
            listValue.forEach {
                returnList += it
            }
            Log.d("test", "if (listValue.size <= 100) {")
        }

        Log.d("test", "return returnList.size = " + returnList.size)
        return returnList
    }

    fun getallUsersss(ownerName: String, repositoryName: String): MutableSet<String>? {
        val notes = getStargazersObjectbox()
        var listValue: List<String> = listOf()
        var listSet: MutableSet<String>? = mutableSetOf()
        notes.forEach {
            if (it.owner == ownerName && it.repository == repositoryName) {
                listValue +=  it.username.split(",").map { it -> it.trim() }
                //listSet?.addAll(it.username.split(",").map { it -> it.trim() })
            }
        }

        if (listValue.size > 100){
            listValue.subList(listValue.size-100, listValue.size).forEach {
                listSet!!.add(it)
            }
        }

        if (listValue.size <= 100) {
            listValue.forEach {
                listSet!!.add(it)
            }
        }
        return listSet
    }


    fun getStargazersObjectbox(): MutableList<Stargazers> {
        return notesStargazers.query().build().find()
    }

    fun setStargazersObjectbox(stargazer: Stargazers) {
        notesStargazers.put(stargazer)
    }
}