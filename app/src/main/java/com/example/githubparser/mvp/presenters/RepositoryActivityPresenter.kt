package com.example.githubparser.mvp.presenters

import android.content.DialogInterface
import com.example.githubparser.adapters.RepositoryAdapter
import com.example.githubparser.model.Repository
import com.example.githubparser.mvp.RepositoryView
import com.omega_r.base.mvp.presenters.OmegaPresenter
import com.omegar.mvp.InjectViewState

@InjectViewState
class RepositoryActivityPresenter : OmegaPresenter<RepositoryView>() {
    fun closeAlertDialog(dialog: DialogInterface) {
        viewState.onCloseAlertDialog(dialog)
    }

    fun addAdapterItem(
        repository: Repository,
        adapter: RepositoryAdapter
    ) {
        viewState.setList(adapter.list.plus(repository), repository)
    }

    fun removeAdapterItem(repository: Repository,
                          adapter: RepositoryAdapter) {
        viewState.removeList(adapter.list.minus(repository), repository)
    }

    fun requestOpenAddDialog() {
        viewState.showAddDialog()
    }

    fun showAdapter(dataBase: List<Repository>) {
        viewState.onShowAdapter(dataBase)
    }

    fun showTextAlertDialog(ownerText: String, repositoryText: String) {
        viewState.onShowAlertDialogText(ownerText, repositoryText)
    }
}