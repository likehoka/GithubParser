package com.example.githubparser.utils

import android.util.Log
import com.example.githubparser.api.StargazersList
import java.text.SimpleDateFormat
import java.util.*

class DistributeStars {
    fun distributeStargazers(
        stargazer: List<StargazersList>//,
        //ownerName: String,
        //repositoryName: String
    ): MutableMap<Int, MyClassYear> {
        val map = mutableMapOf<Int, MyClassYear>()

        stargazer.forEach {
            val date = it.getDate()
            val userName = it.user
            Log.d("test", "User: " + userName.username + " Date: " + date)
            val instance = Calendar.getInstance()
            instance.time = date
            val year = instance.get(Calendar.YEAR)
            val month = instance.get(Calendar.MONTH)
            var yearrr = map[year]

            if (yearrr == null) {
                yearrr = MyClassYear()
                map.put(year, yearrr)  //ключ значение
            }

            var monthhhh = yearrr.monthMap[month]
            if (monthhhh == null) {
                monthhhh = MyClassMonth()
                yearrr.monthMap[month] = monthhhh
            }

            monthhhh.likes += 1
            monthhhh.monthName = (instance.time.month + 1).toString()
            monthhhh.year = year
            monthhhh.ownerName = it.owner
            monthhhh.repositoryName = it.repository

            if (monthhhh.users == "") {
                monthhhh.users += userName.username
            } else monthhhh.users += ", " + userName.username


            val sdf = SimpleDateFormat("MMM")
            val currentDate = sdf.format(Date())
        }

        return map
    }
}