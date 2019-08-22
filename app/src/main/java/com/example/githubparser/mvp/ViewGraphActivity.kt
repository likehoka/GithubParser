package com.example.githubparser.mvp

import com.example.githubparser.api.StargazersList
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarEntry
import com.omegar.mvp.MvpView
import com.omegar.mvp.viewstate.strategy.*
import retrofit2.Response

interface ViewGraphActivity : MvpView {
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun onShowMistake()
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showBarChart(
        barChart: BarChart,
        data: BarData,
        entries: ArrayList<BarEntry>
    )

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun onShowListOfStars(ownerId: Long)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun onShowRequestStargazers(
        ownerId: Long,
        counterStargazers: Long
    )

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun onRequestStargazersRetrofit(
        ownerId: Long,
        toLong: Long
    )

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun onBodySort(
        body: List<StargazersList>?,
        ownerId: Long,
        response: Response<List<StargazersList>>
    )

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun addItemToDataBase(
        ownerId: Long,
        compareBaseStatus: Boolean
    )

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setBarChart(
        entries: ArrayList<BarEntry>,
        labels: ArrayList<String>
    )
}