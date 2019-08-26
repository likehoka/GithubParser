package com.example.githubparser.utils

import android.content.Context
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
import kotlin.concurrent.thread


class MyWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    private var notesRepository = ObjectBox.boxStore.boxFor<Repository>()
    private var counterStargazers: Long = 1
    private var sortStargazersList: List<StargazersList> = emptyList()
    private var stargazersList: List<StargazersList> = emptyList()

    override fun doWork(): Result {
        workMethod()
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
        if (UsersStorage().getAllStargazersList() != null) {
            val entries = ArrayList<BarEntry>()
            var count = 0
            var starsCount = 0
            UsersStorage().getAllStargazersList().forEach {
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

    private fun fetchStargazersRetrofit(
        ownerName: String,
        repositoryName: String,
        idOwner: Long,
        counterStargazers: Long
    ) {
        this.counterStargazers = counterStargazers
        StargazersApi().getStargazers(ownerName, repositoryName, counterStargazers.toString())
            .enqueue(object : Callback<List<StargazersList>> {
                override fun onFailure(call: Call<List<StargazersList>>, t: Throwable) {
                }

                override fun onResponse(call: Call<List<StargazersList>>, response: Response<List<StargazersList>>) {
                    if (response.body() == null) {
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
        stargazersList += body!!
        if (body.size == 100) {
            counterStargazers += 1
            fetchStargazers(ownerName, repositoryName, idOwner, counterStargazers)
        }
        if (body.size < 100) {
            stargazersList?.forEach {
                val stargazer = it
                it.idOwner = idOwner
                counterStargazers = 1
                if (UsersStorage().getUsers(idOwner)!!.contains(stargazer.user.username)) {}
                else sortStargazersList += stargazer
            }
            sortStargazersList?.let {
                NewStarsgazers().sortDataToDatabase(
                    ownerName,
                    repositoryName,
                    SortMap().getStargazersMap(it),
                    context as Context,
                    idOwner,
                    true
                )
            }
            sortStargazersList = emptyList()
            stargazersList = emptyList()
        }
    }

}