package com.example.githubparser.mvp.presenters

import android.content.DialogInterface
import androidx.recyclerview.widget.RecyclerView
import com.example.githubparser.adapters.RepositoryAdapter
import com.example.githubparser.mvp.ViewRepositoryActivity
import com.omegar.mvp.InjectViewState
import com.omegar.mvp.MvpPresenter

@InjectViewState
class RepositoryActivityPresenter : MvpPresenter<ViewRepositoryActivity>() {
    fun showAlertDialog() {
        viewState.onShowAlertDialog()
    }

    fun closeAlertDialog(dialog: DialogInterface) {
        viewState.onCloseAlertDialog(dialog)
    }

    fun showTextAlertDialog(ownerText: String, repositoryText: String) {
        viewState.onShowAlertDialogText(ownerText, repositoryText)
    }

    fun statusCloseAlertDialog() {
        viewState.statusAlertDialog()
    }

    fun showAdapter(
        adapter: RepositoryAdapter,
        repositoryRecyclerView: RecyclerView
    ) {
        viewState.onShowAdapter()
    }
}