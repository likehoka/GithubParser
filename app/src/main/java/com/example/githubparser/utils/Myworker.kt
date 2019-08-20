package com.example.githubparser.utils

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.githubparser.Database.objectbox.ObjectBox
import com.example.githubparser.api.StargazersApi
import com.example.githubparser.api.StargazersList
import com.example.githubparser.model.Repository
import com.github.mikephil.charting.data.BarEntry
import io.objectbox.BoxStore.context
import io.objectbox.kotlin.boxFor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread


class MyWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    var notesRepository = ObjectBox.boxStore.boxFor<Repository>()
    var counterStargazers: Long = 1
    var stargazersList: List<StargazersList> = emptyList()
    var sortStargazerslist: List<StargazersList> = emptyList()

    override fun doWork(): Result {
        workMethod()
        Log.d("test", "doWork: start")
        TimeUnit.SECONDS.sleep(5)
        Log.d("test", "doWork: end")
        return Result.success()
    }

    private fun getRepositories(): MutableList<Repository> {
        return notesRepository.query().build().find()
    }

    private fun workMethod() {
        getRepositories().forEach {
            thread {
                fetchStargazers(it.ownerName, it.repositoryName, it.id, counterStargazers)
            }
        }
    }

    private fun fetchStargazers(ownerName: String, repositoryName: String, idOwner: Long, counterStargazers: Long) {
        if (UsersGetAll().getStargazersObjectbox() != null) {
            val entries = ArrayList<BarEntry>()
            var count: Int = 0
            var starsCount: Int = 0
            UsersGetAll().getStargazersObjectbox().forEach {
                if (it.idRepository == idOwner) {
                    starsCount += it.likes
                    entries.add(BarEntry(it.likes.toFloat(), count))
                    count += 1
                }
            }
            if (entries.size != 0) {
                fetchStargazersRetrofit(ownerName, repositoryName, idOwner, ((starsCount / 100) + 1).toLong())
            } else fetchStargazersRetrofit(ownerName, repositoryName, idOwner, counterStargazers)
        } else fetchStargazersRetrofit(ownerName, repositoryName, idOwner, counterStargazers)
    }

    private fun fetchStargazersRetrofit(ownerName: String, repositoryName: String, idOwner: Long, counterStargazers: Long) {
        this.counterStargazers = counterStargazers
        StargazersApi().getStargazers(ownerName, repositoryName, counterStargazers.toString())
            .enqueue(object : Callback<List<StargazersList>> {
                override fun onFailure(call: Call<List<StargazersList>>, t: Throwable) {
                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<List<StargazersList>>, response: Response<List<StargazersList>>) {
                    if (response.body() == null) {
                        Toast.makeText(applicationContext, "This repository is failed", Toast.LENGTH_LONG).show()
                    } else
                        stargazersCounter(response.body(), ownerName, repositoryName, idOwner)
                }
            })
    }



    private fun stargazersCounter(
        body: List<StargazersList>?,
        ownerName: String,
        repositoryName: String,
        idOwner: Long
    ) {
        //Здесь надо сделать обработку getAllUsers.compare(StargazersList)
        stargazersList += body!!
        sortStargazerslist
        if (body.size == 100) {
            counterStargazers += 1
            fetchStargazers(ownerName, repositoryName, idOwner, counterStargazers)
        } else if (body.size < 100) {
            stargazersList?.forEach {
                val username = it.user.username
                val stargazer = it
                it.owner = ownerName
                it.repository = repositoryName
                counterStargazers = 1
                var statusCompare: Boolean = false
                UsersGetAll().getallUserss(idOwner).forEach {
                    if (stargazer.user.username == it) {
                        statusCompare = true
                    }
                }
                if (!statusCompare) {
                    sortStargazerslist += stargazer
                }
            }

            sortStargazerslist?.let {
                NewStarsgazers().sortDataToDatabase(ownerName, repositoryName,  DistributeStars().distributeStargazers(it), context as Context, idOwner, true)//, ownerName, repositoryName))//, ownerName, repositoryName)
            }

            sortStargazerslist = emptyList()
            stargazersList = emptyList()
        }
    }

}