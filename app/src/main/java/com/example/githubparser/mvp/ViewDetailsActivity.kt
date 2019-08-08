package com.example.githubparser.mvp

import android.webkit.WebView
import com.omegar.mvp.MvpView
import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.omegar.mvp.viewstate.strategy.StateStrategyType


interface ViewDetailsActivity : MvpView {
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun onShowWebView()
}