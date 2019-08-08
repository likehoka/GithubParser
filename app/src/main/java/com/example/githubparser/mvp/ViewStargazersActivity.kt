package com.example.githubparser.mvp

import com.omegar.mvp.MvpView

interface ViewStargazersActivity : MvpView {
    fun onShowStargazers(ownerNameText: String, repositoryNameText: String, stringDateText: String)
}