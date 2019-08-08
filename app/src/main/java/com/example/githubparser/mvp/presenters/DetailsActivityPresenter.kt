package com.example.githubparser.mvp.presenters

import android.webkit.WebView
import com.example.githubparser.mvp.ViewDetailsActivity
import com.omegar.mvp.InjectViewState
import com.omegar.mvp.MvpPresenter

@InjectViewState
class DetailsActivityPresenter : MvpPresenter<ViewDetailsActivity>(){
    fun showWebView() {
        viewState.onShowWebView()
    }
}