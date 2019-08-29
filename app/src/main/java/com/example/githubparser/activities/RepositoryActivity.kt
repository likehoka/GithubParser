package com.example.githubparser.activities


import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.example.githubparser.R
import com.example.githubparser.adapters.RepositoryAdapter
import com.example.githubparser.model.Repository
import com.example.githubparser.mvp.RepositoryView
import com.example.githubparser.mvp.presenters.RepositoryPresenter
import com.example.githubparser.utils.MyWorkService
import com.example.githubparser.utils.repositoryutils.RepositoryBase
import com.omega_r.base.components.OmegaActivity
import com.omegar.mvp.presenter.InjectPresenter

import kotlinx.android.synthetic.main.activity_repository.*
import kotlinx.android.synthetic.main.content_repository.*
import kotlinx.android.synthetic.main.layout_dialog.view.*
import java.util.concurrent.TimeUnit

class RepositoryActivity : OmegaActivity(), RepositoryView, RepositoryAdapter.Callback {

    @InjectPresenter
    override lateinit var presenter: RepositoryPresenter
    private val adapter = RepositoryAdapter(this)
    private var view: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repository)
        setSupportActionBar(toolbar)
        //serviceManager()
        repositoryRecyclerView.layoutManager = LinearLayoutManager(this)
        repositoryRecyclerView.adapter = adapter
        presenter.showAdapter(RepositoryBase().getRepositoriesList())
        fab.setOnClickListener {
            presenter.requestOpenAddDialog()
        }
    }

    override fun onShowAdapter(dataBase: List<Repository>) {
        if (RepositoryBase().getRepositoriesList().isNotEmpty()) {
            addAllItemRepository(dataBase)
        }
    }

    private fun addAllItemRepository(notes: List<Repository>) {
        adapter.repositoryList = notes
        adapter.refreshAdapter()
    }

    override fun removeList(list: List<Repository>, repository: Repository) {
        adapter.repositoryList = list
        adapter.refreshAdapter()
        RepositoryBase().removeRepository(repository)
    }

    override fun setList(list: List<Repository>,repository: Repository) {
        adapter.repositoryList = list
        adapter.refreshAdapter()
        RepositoryBase().putRepository(repository)
    }

    override fun onDeleteClicked(repository: Repository) {
        presenter.removeAdapterItem(repository, adapter)
    }

    override fun onOpenClicked(repository: Repository) {
        startActivity(GraphActivity.createIntent(this, repository.ownerName, repository.repositoryName))
    }

    override fun onShowAlertDialogText(ownerText: String, repositoryText: String) {
        view?.ownerEditText?.setText(ownerText)
        view?.repositoryEditText?.setText(repositoryText)
    }

    override fun onCloseAlertDialog(dialog: DialogInterface) {
        dialog.dismiss()
        dialog.cancel()
    }

    override fun showAddDialog() {
        view = LayoutInflater.from(this).inflate(R.layout.layout_dialog, null)
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Github Parser")
        builder.setView(view)
        builder.setPositiveButton("Ok") { dialog, _ ->
            val owner = view?.ownerEditText?.text.toString()
            val repositoryName = view?.repositoryEditText?.text.toString()
            val repository = Repository(ownerName = owner, repositoryName = repositoryName)
            if(RepositoryBase().compareRepositories(owner, repositoryName, RepositoryBase().getRepositoriesList())) {
                presenter.addAdapterItem(repository, adapter)
            } else  Toast.makeText(this, "This repository has already been recorded", Toast.LENGTH_LONG).show()
            presenter.closeAlertDialog(dialog)
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            presenter.closeAlertDialog(dialog)
        }
        builder.show()
    }

    override fun onDestroy() {
            val ownerText = view?.ownerEditText?.text.toString()
            val repositoryText = view?.repositoryEditText?.text.toString()
            presenter.showTextAlertDialog(ownerText, repositoryText)
        super.onDestroy()
    }

    private fun serviceManager() {
        val myWorkRequest = PeriodicWorkRequest.Builder(MyWorkService::class.java, 30, TimeUnit.MINUTES).build()
        WorkManager.getInstance().enqueue(myWorkRequest)
    }
}



