package com.example.githubparser.mvp.presenters

import com.example.githubparser.api.StargazersList
import com.example.githubparser.mvp.GraphView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarEntry
import com.omegar.mvp.InjectViewState
import com.omegar.mvp.MvpPresenter
import retrofit2.Response

@InjectViewState
class GraphPresenter : MvpPresenter<GraphView>() {
    private var labels = ArrayList<String>()
    private var counterStargazers: Long = 1

    fun failedRepository(){
        viewState.onShowMistake()
    }

    fun setBarChartEntries(
        barChart: BarChart,
        data: BarData,
        entries: ArrayList<BarEntry>
    ) {
        viewState.showBarChart(barChart, data, entries)
    }

    fun setLabels(labels: ArrayList<String>) {
        this.labels = labels
    }

    fun getLabels(): ArrayList<String> {
        return labels
    }

    fun setPageCounter(counterStargazers: Long) {
        this.counterStargazers = counterStargazers
    }

    fun getPageCounter() : Long {
        return counterStargazers
    }


    fun showListOfStars(ownerId: Long) {
        viewState.onShowListOfStars(ownerId)
    }

    fun requestStargazers(
        ownerId: Long,
        counterStargazers: Long
    ) {
        viewState.onShowRequestStargazers(ownerId, counterStargazers)
    }

    fun resuestStargazersRetrofit(
        ownerId: Long,
        toLong: Long
    ) {
        viewState.onRequestStargazersRetrofit(ownerId, toLong)
    }

    fun bodySort(
        body: List<StargazersList>?,
        ownerId: Long,
        response: Response<List<StargazersList>>
    ) {
        viewState.onBodySort(body, ownerId, response)
    }

    fun addItemDataBase(
        ownerId: Long,
        compareBaseStatus: Boolean
    ) {
        viewState.addItemToDataBase(ownerId, compareBaseStatus)
    }

    fun setBarChartValues(
        entries: ArrayList<BarEntry>,
        labels: ArrayList<String>
    ) {
        viewState.setBarChart(entries, labels)
    }
}