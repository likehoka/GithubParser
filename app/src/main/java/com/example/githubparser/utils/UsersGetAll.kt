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


    fun getStargazersObjectbox(): MutableList<Stargazers> {
        return notesStargazers.query().build().find()
    }

    fun setStargazersObjectbox(stargazer: Stargazers) {
        notesStargazers.put(stargazer)
    }

    fun getallUsers(ownerName: String, repositoryName: String): List<String> {
        val notes = getStargazersObjectbox()
        var starsCount: Int = 0
        var listValue: List<String> = listOf()
        notes.forEach {
            if (it.owner == ownerName && it.repository == repositoryName) {
                //starsCount += it.likes
                val listValueCash: List<String> = it.username.split(",").map { it -> it.trim() }
                listValue += listValueCash
                if (listValue.size > 100){
                    var returnList: List<String> = listOf()
                    listValue.subList(listValue.size-100, listValue.size).forEach {
                        returnList += it
                    }
                    return returnList
                }

                if (listValue.size <= 100) {
                    var returnList: List<String> = listOf()
                    listValue.forEach {
                        returnList += it
                    }
                    return returnList
                }

            }
        }
        return listValue
    }


}