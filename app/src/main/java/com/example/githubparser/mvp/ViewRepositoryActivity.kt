package com.example.githubparser.mvp

import android.content.DialogInterface
import com.omegar.mvp.MvpView
import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.omegar.mvp.viewstate.strategy.SingleStateStrategy
import com.omegar.mvp.viewstate.strategy.StateStrategyType


interface ViewRepositoryActivity : MvpView{
    @StateStrategyType(SingleStateStrategy::class)
    fun onShowAlertDialog()
    @StateStrategyType(SingleStateStrategy::class)
    fun onCloseAlertDialog(dialog: DialogInterface)
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun onShowAlertDialogText(ownerText: String, repositoryText: String)
    @StateStrategyType(SingleStateStrategy::class)
    fun statusAlertDialog()
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun onShowAdapter()


}