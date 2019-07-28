package com.example.githubparser.utils

import com.example.githubparser.api.StargazersList

class StarsCounter {
    /*
    var stargazersList: List<StargazersList> = emptyList()
    var sortStargazerslist: List<StargazersList> = emptyList()
    var counterStargazers: Long = 1

    fun stargazersCounter(body: List<StargazersList>?, ownerName: String, repositoryName: String) {
        //Здесь надо сделать обработку getAllUsers.compare(StargazersList)
        stargazersList += body!!
        sortStargazerslist
        if (body.size == 100) {
            counterStargazers += 1
            fetchStargazers(ownerName, repositoryName, counterStargazers)
        } else if (body.size < 100) {
            stargazersList?.forEach {
                val username = it.user.username
                val stargazer = it
                it.owner = ownerName
                it.repository = repositoryName
                counterStargazers = 1
                var statusCompare: Boolean = false
                UsersGetAll().getallUsers(ownerName, repositoryName).forEach {
                    if (stargazer.user.username == it) {
                        statusCompare = true
                    }
                }
                if (statusCompare == false) {
                    sortStargazerslist += stargazer
                }
            }

            sortStargazerslist?.let {
                NewStarsgazers().sortDataToDatabase(DistributeStars().distributeStargazers(it, ownerName, repositoryName), ownerName, repositoryName)
                showBarOb(ownerName, repositoryName)
            }
        }
    }
    */
}