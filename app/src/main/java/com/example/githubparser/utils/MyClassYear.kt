package com.example.githubparser.utils

class MyClassYear {
    val monthMap = mutableMapOf<Int, MyClassMonth>()
}

class MyClassMonth {
    var ownerName: String = ""
    var repositoryName: String = ""
    var year: Int = 0
    var monthName: String = ""
    var likes = 0
    var users: String = ""
}