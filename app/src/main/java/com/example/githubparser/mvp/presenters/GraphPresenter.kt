package com.example.githubparser.mvp.presenters

import android.content.Context
import android.util.Log
import com.example.githubparser.api.StargazersApi
import com.example.githubparser.api.StargazersList
import com.example.githubparser.mvp.GraphView
import com.example.githubparser.utils.NewStarsgazers
import com.example.githubparser.utils.SortMap
import com.example.githubparser.utils.UsersStorage
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarEntry
import com.omegar.mvp.InjectViewState
import com.omegar.mvp.MvpPresenter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@InjectViewState
class GraphPresenter : MvpPresenter<GraphView>() {
    private var compareBaseStatus = false
    private var labelsArrayList = ArrayList<String>()
    private var counterStargazers: Long = 1
    private var stargazersList: List<StargazersList> = emptyList()
    private lateinit var context: Context
    private val listStargazers = UsersStorage().getAllStargazersList()

    fun setContext(context: Context) {
        this.context = context
    }

    fun setBarChartEntries(
        barChart: BarChart,
        data: BarData,
        entries: ArrayList<BarEntry>
    ) {
        viewState.showBarChart(barChart, data, entries)
    }

    fun setLabels(labels: ArrayList<String>) {
        this.labelsArrayList = labels
    }

    fun getLabels(): ArrayList<String> {
        return labelsArrayList
    }

    fun setPageCounter(counterStargazers: Long) {
        this.counterStargazers = counterStargazers
    }

    fun getPageCounter(): Long {
        return counterStargazers
    }


    fun showListOfStars(ownerId: Long) {
        val labels = ArrayList<String>()
        val entries = ArrayList<BarEntry>()
        entries.clear()
        var count = 0
        var starsCount = 0
        UsersStorage().getAllStargazersList().forEach {
            if (it.idRepository == ownerId) {
                starsCount += it.likes
                entries.add(BarEntry(it.likes.toFloat(), count))
                labels.add(it.month + " " + it.year)
                count += 1
            }
        }
        viewState.setBarChart(entries, labels)
    }

    fun requestStargazers(
        ownerId: Long,
        counterStargazers: Long,
        ownerNameText: String,
        repositoryNameText: String
    ) {
        Log.d("test", "counterStargazers = $counterStargazers")
        var starsCount = 0
        if (listStargazers != null && this.counterStargazers == 1.toLong()) {
            val entries = ArrayList<BarEntry>()
            var count = 0
            listStargazers.forEach {
                if (it.idRepository == ownerId) {
                    starsCount += it.likes
                    entries.add(BarEntry(it.likes.toFloat(), count))
                    count += 1
                }
            }
            viewState.setStargazersCounter(
                ownerId,
                ((starsCount / 100) + 1).toLong(), ownerNameText, repositoryNameText)

        } else viewState.setStargazersCounter(
            ownerId, counterStargazers, ownerNameText, repositoryNameText)

    }

    fun requestStargazersRetrofit(
        ownerId: Long,
        counterStargazers: Long,
        ownerNameText: String,
        repositoryNameText: String
    ) {

        StargazersApi().getStargazers(ownerNameText, repositoryNameText, counterStargazers.toString())
            .enqueue(object : Callback<List<StargazersList>> {
                override fun onFailure(call: Call<List<StargazersList>>, t: Throwable) {
                    viewState.onShowMistake()
                }

                override fun onResponse(call: Call<List<StargazersList>>, response: Response<List<StargazersList>>) {
                    if (response.message() != "Forbidden") {
                        if (response.body() == null && stargazersList.isEmpty()) {
                            viewState.onShowMistake()
                        } else {
                            viewState.callBodySort(
                                response.body(),
                                ownerId,
                                response,
                                ownerNameText,
                                repositoryNameText
                            )
                        }
                    }
                    if (response.message() == "Forbidden" && stargazersList.isEmpty()) {
                        viewState.limitRequest()
                    }
                    if (response.message() == "Forbidden" && stargazersList.isNotEmpty()) {
                        viewState.limitRequest()
                    }
                }
            })
    }

    fun bodySort(
        body: List<StargazersList>?,
        ownerId: Long,
        response: Response<List<StargazersList>>,
        ownerNameText: String,
        repositoryNameText: String
    ) {

        if (body != null) {
            stargazersList += body
            counterStargazers += 1
        }
        if (body != null && body.size == 100) {
            if (stargazersList.size <= 100) {
                viewState.requestStargazers(ownerId, getPageCounter(), ownerNameText, repositoryNameText)
            } else {
                counterStargazers += 1
                viewState.addItemBase(ownerId, compareBaseStatus, ownerNameText, repositoryNameText)
            }
        }
        if (response.message() == "Forbidden") {
            compareBaseStatus = false
            if (stargazersList.isNotEmpty()) {
                viewState.addItemBase(ownerId, compareBaseStatus, ownerNameText, repositoryNameText)
                counterStargazers = 1
            }

        }
        if (body == null) {
            compareBaseStatus = false
            if (stargazersList.isNotEmpty()) {
                viewState.addItemBase(ownerId, compareBaseStatus, ownerNameText, repositoryNameText)
                viewState.loadingComplete()

            }
        }

        if (body?.size!! < 100) {
            compareBaseStatus = false
            counterStargazers = 1
            if (stargazersList.isNotEmpty()) {
                viewState.addItemBase(ownerId, compareBaseStatus, ownerNameText, repositoryNameText)
                viewState.loadingComplete()
            }
        }
    }

    fun addItemDataBase(
        ownerId: Long,
        compareBaseStatus: Boolean,
        ownerNameText: String,
        repositoryNameText: String
    ) {
        var sortStargazerslist: List<StargazersList> = emptyList()
        val countStars: Int = UsersStorage().getUsers(ownerId)!!.size
        if (!compareBaseStatus) {
            stargazersList?.forEach {
                val stargazer = it
                it.idOwner = ownerId
                if (countStars == 0) {
                    sortStargazerslist += stargazer
                }

                if (countStars < 100 && countStars != 0) {
                    if (!UsersStorage().getUsers(ownerId)!!.contains(stargazer.user.username)) {
                        sortStargazerslist += stargazer
                    }
                }

                if (countStars >= 100) {
                    if (!UsersStorage().getUsers(ownerId)!!.contains(stargazer.user.username)) {
                        sortStargazerslist += stargazer
                    }
                }
            }
        } else stargazersList.forEach {
            it.idOwner = ownerId
            sortStargazerslist += it
        }
        this.compareBaseStatus = true

        sortStargazerslist?.let {
            val stars = SortMap().getStargazersMap(it)
            NewStarsgazers().sortDataToDatabase(
                ownerNameText, repositoryNameText,
                stars,
                context, ownerId, false
            )
        }

        if (stargazersList.size == 200) {
            stargazersList = emptyList()
            sortStargazerslist = emptyList()
            showListOfStars(ownerId)
            viewState.requestStargazers(ownerId, getPageCounter(), ownerNameText, repositoryNameText)
        } else {
            stargazersList = emptyList()
            sortStargazerslist = emptyList()
            showListOfStars(ownerId)
        }
    }
}