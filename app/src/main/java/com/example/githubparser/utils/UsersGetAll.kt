package com.example.githubparser.utils

import android.util.Log
import com.example.githubparser.Database.objectbox.ObjectBox
import com.example.githubparser.model.Stargazers
import io.objectbox.kotlin.boxFor

class UsersGetAll {
    var notesStargazers = ObjectBox.boxStore.boxFor<Stargazers>()

    fun getallUsers(ownerId: Long): MutableSet<String>? {
        val notes = getStargazersObjectbox()
        var listValue: List<String> = listOf()
        notes.forEach {
            if (it.idRepository == ownerId) {
                listValue +=  it.username.split(",").map { it -> it.trim() }
            }
        }
        if (listValue.size > 100){
            Log.d("test", "listValue.size = " + listValue.subList(listValue.size-100, listValue.size).toMutableSet().size)
            return listValue.subList(listValue.size-100, listValue.size).toMutableSet()
        }

        Log.d("test", "listValue.toMutableSet().size = " + listValue.toMutableSet().size)
        return listValue.toMutableSet()
    }

    fun getAllstringDate(ownerId: Long, stringDate: String) : MutableSet<String>? {
        val notes = getStargazersObjectbox()
        var listValue: List<String> = listOf()
        notes.forEach {
            if (it.idRepository == ownerId && it.stringDate == stringDate) {
                listValue +=  it.stringDate
            }
        }
        return listValue.toMutableSet()
    }


    fun getStargazersObjectbox(): MutableList<Stargazers> {
        return notesStargazers.query().build().find()
    }

    fun setStargazersObjectbox(stargazer: Stargazers) {
        notesStargazers.put(stargazer)
    }
}