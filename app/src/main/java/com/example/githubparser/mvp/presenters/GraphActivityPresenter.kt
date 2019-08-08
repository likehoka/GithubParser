package com.example.githubparser.mvp.presenters

import com.example.githubparser.mvp.ViewGraphActivity
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarEntry
import com.omegar.mvp.InjectViewState
import com.omegar.mvp.MvpPresenter

@InjectViewState
class GraphActivityPresenter : MvpPresenter<ViewGraphActivity>() {

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


}