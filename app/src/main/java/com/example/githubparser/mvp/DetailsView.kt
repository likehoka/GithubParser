package com.example.githubparser.mvp

import com.omegar.mvp.MvpView
import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.omegar.mvp.viewstate.strategy.StateStrategyType


interface DetailsView : MvpView {
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showWebUrl(url: String)
}