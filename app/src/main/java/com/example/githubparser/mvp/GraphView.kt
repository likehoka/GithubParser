package com.example.githubparser.mvp

import com.example.githubparser.api.StargazersList
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarEntry
import com.omegar.mvp.MvpView
import com.omegar.mvp.viewstate.strategy.*
import retrofit2.Response

interface GraphView : MvpView {

    @StateStrategyType(SingleStateStrategy::class)
    fun onShowMistake()
    @StateStrategyType(SingleStateStrategy::class)
    fun showBarChart(
        barChart: BarChart,
        data: BarData,
        entries: ArrayList<BarEntry>
    )

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setBarChart(
        entries: ArrayList<BarEntry>,
        labels: ArrayList<String>
    )

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setStargazersCounter(
        ownerId: Long,
        toLong: Long,
        ownerNameText: String,
        repositoryNameText: String
    )

    @StateStrategyType(SingleStateStrategy::class)
    fun limitRequest()

    @StateStrategyType(SingleStateStrategy::class)
    fun loadingComplete()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setPage(counter: Long)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun callBodySort(
        body: List<StargazersList>?,
        ownerId: Long,
        response: Response<List<StargazersList>>,
        ownerNameText: String,
        repositoryNameText: String
    )

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun addItemBase(
        ownerId: Long,
        compareBaseStatus: Boolean,
        ownerNameText: String,
        repositoryNameText: String
    )

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun requestStargazers(
        ownerId: Long,
        pageCounter: Long,
        ownerNameText: String,
        repositoryNameText: String
    )
}