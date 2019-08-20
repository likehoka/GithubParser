package com.example.githubparser.mvp

import com.example.githubparser.api.StargazersList
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarEntry
import com.omegar.mvp.MvpView
import com.omegar.mvp.viewstate.strategy.AddToEndStrategy
import com.omegar.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.omegar.mvp.viewstate.strategy.SingleStateStrategy
import com.omegar.mvp.viewstate.strategy.StateStrategyType
import retrofit2.Response

interface ViewGraphActivity : MvpView {
    @StateStrategyType(AddToEndStrategy::class)
    fun onShowMistake()
    @StateStrategyType(AddToEndStrategy ::class)
    fun showBarChart(
        barChart: BarChart,
        data: BarData,
        entries: ArrayList<BarEntry>
    )

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun onShowListOfStars(ownerId: Long)

    @StateStrategyType(SingleStateStrategy::class)
    fun onShowRequestStargazers(
        ownerId: Long,
        counterStargazers: Long
    )

    @StateStrategyType(SingleStateStrategy::class)
    fun onRequestStargazersRetrofit(
        ownerId: Long,
        toLong: Long
    )

    @StateStrategyType(SingleStateStrategy::class)
    fun onBodySort(
        body: List<StargazersList>?,
        ownerId: Long,
        response: Response<List<StargazersList>>
    )

    @StateStrategyType(SingleStateStrategy::class)
    fun addItemToDataBase(
        ownerId: Long,
        compareBaseStatus: Boolean
    )

    @StateStrategyType(SingleStateStrategy::class)
    fun setBarChart(entries: ArrayList<BarEntry>)
}