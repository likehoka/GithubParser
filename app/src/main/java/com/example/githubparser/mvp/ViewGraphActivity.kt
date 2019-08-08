package com.example.githubparser.mvp

import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarEntry
import com.omegar.mvp.MvpView
import com.omegar.mvp.viewstate.strategy.SingleStateStrategy
import com.omegar.mvp.viewstate.strategy.StateStrategyType

interface ViewGraphActivity : MvpView {
    @StateStrategyType(SingleStateStrategy::class)
    fun onShowMistake()
    @StateStrategyType(SingleStateStrategy::class)
    fun showBarChart(
        barChart: BarChart,
        data: BarData,
        entries: ArrayList<BarEntry>
    )
}