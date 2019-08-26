package com.example.githubparser.mvp.presenters

import com.example.githubparser.mvp.StargazersView
import com.omegar.mvp.InjectViewState
import com.omegar.mvp.MvpPresenter

@InjectViewState
class StargazersPresenter : MvpPresenter<StargazersView>(){

    fun showStargazers(ownerId: Long, stringDateText: String) {
        viewState.onShowStargazers(ownerId, stringDateText)
    }
}