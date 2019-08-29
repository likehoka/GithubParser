package com.example.githubparser.mvp.presenters

import com.example.githubparser.mvp.DetailsView
import com.omegar.mvp.InjectViewState
import com.omegar.mvp.MvpPresenter

@InjectViewState
class DetailsPresenter(url: String) : MvpPresenter<DetailsView>(){

    init {
        viewState.showWebUrl(url)
    }

}