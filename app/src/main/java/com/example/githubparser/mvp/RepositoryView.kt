package com.example.githubparser.mvp

import android.content.DialogInterface
import com.example.githubparser.model.Repository
import com.omega_r.base.mvp.views.OmegaView
import com.omegar.mvp.viewstate.strategy.*

interface RepositoryView : OmegaView {

    @StateStrategyType(SingleStateStrategy::class)
    fun setList(
        list: List<Repository>,
        repository: Repository
    )

    @StateStrategyType(SingleStateStrategy::class)
    fun showAddDialog()

    @StateStrategyType(SingleStateStrategy::class)
    fun onCloseAlertDialog(dialog: DialogInterface)

    @StateStrategyType(AddToEndStrategy::class)
    fun onShowAlertDialogText(ownerText: String, repositoryText: String)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun onDeleteClicked(repository: Repository)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun onShowAdapter(dataBase: List<Repository>)

    @StateStrategyType(SkipStrategy ::class)
    fun removeList(
        minus: List<Repository>,
        repository: Repository
    )
}