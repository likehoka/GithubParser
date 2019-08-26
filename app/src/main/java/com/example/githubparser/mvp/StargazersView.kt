package com.example.githubparser.mvp

import com.omegar.mvp.MvpView

interface StargazersView : MvpView {
    fun onShowStargazers(ownerId: Long, stringDateText: String)
}