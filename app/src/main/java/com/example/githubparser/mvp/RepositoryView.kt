package com.example.githubparser.mvp

import com.example.githubparser.model.Repository
import com.omegar.mvp.MvpView
import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.omegar.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.omegar.mvp.viewstate.strategy.StateStrategyType

interface RepositoryView : MvpView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setList(list: List<Repository>)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showAddDialog()
}