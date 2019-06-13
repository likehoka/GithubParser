package com.example.githubparser.model

import com.google.gson.annotations.SerializedName
import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable
import java.text.SimpleDateFormat
import java.util.*


class Stargazers (
    @SerializedName("starred_at")
    val stringDate: String,
    @SerializedName("user")
    val user: User
) {

    companion object {
       private val formatter = SimpleDateFormat("yyyy-MM-dd")
    }


    fun getDate(): Date {
        return formatter.parse(stringDate)
    }

}