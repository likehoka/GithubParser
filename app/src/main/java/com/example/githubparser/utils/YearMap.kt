package com.example.githubparser.utils

class YearMap {
    val monthMap = mutableMapOf<Int, StarsMonth>()
}

class StarsMonth {
    var year: Int = 0
    var monthName: String = ""
    var likes = 0
    var user: String = ""
    var ownerId: Long = 0
}