package com.example.githubparser.utils

import android.util.Log
import com.example.githubparser.Database.objectbox.ObjectBox
import com.example.githubparser.model.Stargazers
import io.objectbox.kotlin.boxFor

class NewStarsgazers {

    var notesStargazers = ObjectBox.boxStore.boxFor<Stargazers>()

    private fun getnoteObjectbox(): MutableList<Stargazers> {
        return notesStargazers.query().build().find()
    }

    fun sortDataToDatabase(
        map: MutableMap<Int, MyClassYear>//,
        //ownerName: String,
        //repositoryName: String
    ) {
        map.forEach() {
            val year = it.value
            it.value.monthMap.forEach {
                val month = it.value
                val likes = month.likes
                val owner = month.ownerName
                val repository = month.repositoryName
                val monthName = month.monthName
                val users = it.value.users
                Log.d(
                    "test",
                    "Owner: " + owner + " Year: " + it.value.year + " Month: " + monthName + " Likes: " + likes + " Users: " + it.value.users
                )

                val stargazer = Stargazers(
                    owner = owner, repository = repository, username = users, likes = likes,
                    month = monthName, year = it.value.year.toString(), stringDate = monthName + " " + it.value.year
                )
                var statusCompare: Boolean = false
                var stargazers: Stargazers? = null
                getnoteObjectbox().forEach {
                    if (it.owner == stargazer.owner && it.repository == stargazer.repository && it.stringDate == stargazer.stringDate) {
                        it.likes += likes
                        it.username += ", $users"
                        Log.d("test", "Равенство работает")
                        stargazers = it
                        statusCompare = true
                        getnoteObjectbox().remove(it)
                    }
                }
                if (statusCompare == true){
                    stargazers?.let { it1 -> UsersGetAll().setStargazersObjectbox(it1) }
                }

                if (statusCompare == false) {
                    UsersGetAll().setStargazersObjectbox(stargazer)
                }
            }
        }
    }
}