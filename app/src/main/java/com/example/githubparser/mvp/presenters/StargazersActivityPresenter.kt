package com.example.githubparser.mvp.presenters

import com.example.githubparser.mvp.ViewStargazersActivity
import com.omegar.mvp.InjectViewState
import com.omegar.mvp.MvpPresenter

@InjectViewState
class StargazersActivityPresenter : MvpPresenter<ViewStargazersActivity>(){

    fun showStargazers(ownerNameText: String, repositoryNameText: String, stringDateText: String) {
        viewState.onShowStargazers(ownerNameText, repositoryNameText, stringDateText)
    }
}