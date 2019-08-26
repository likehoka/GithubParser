package com.example.githubparser.utils

import com.example.githubparser.api.StargazersList
import java.util.*

class SortMap {
    fun getStargazersMap(
        stargazer: List<StargazersList>
    ): MutableMap<Int, YearMap> {
        val map = mutableMapOf<Int, YearMap>()

        stargazer.forEach {
            val date = it.getDate()
            val userName = it.user
            val instance = Calendar.getInstance()
            instance.time = date
            val year = instance.get(Calendar.YEAR)
            val month = instance.get(Calendar.MONTH)
            var mapYear = map[year]

            if (mapYear == null) {
                mapYear = YearMap()
                map[year] = mapYear
            }

            var mapMonth = mapYear.monthMap[month]
            if (mapMonth == null) {
                mapMonth = StarsMonth()
                mapYear.monthMap[month] = mapMonth
            }

            mapMonth.likes += 1
            mapMonth.monthName = (instance.time.month + 1).toString()
            mapMonth.year = year
            mapMonth.ownerId = it.idOwner

            if (mapMonth.user == "") {
                mapMonth.user += userName.username
            } else mapMonth.user += ", " + userName.username
        }
        return map
    }
}