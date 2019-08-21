package com.example.githubparser.activities


import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.example.githubparser.Database.objectbox.ObjectBox
import com.example.githubparser.R
import com.example.githubparser.adapters.RepositoryAdapter
import com.example.githubparser.model.Repository
import com.example.githubparser.mvp.RepositoryView
import com.example.githubparser.mvp.presenters.RepositoryActivityPresenter
import com.example.githubparser.utils.MyWorker
import com.example.githubparser.utils.repositoryutils.CompareRepository
import com.example.githubparser.utils.repositoryutils.RepositoryBase
import com.omega_r.base.components.OmegaActivity
import com.omegar.mvp.presenter.InjectPresenter

import kotlinx.android.synthetic.main.activity_repository.*
import kotlinx.android.synthetic.main.content_repository.*
import kotlinx.android.synthetic.main.layout_dialog.view.*
import java.util.concurrent.TimeUnit

class RepositoryActivity : OmegaActivity(), RepositoryView, RepositoryAdapter.Callback {

    private lateinit var subView: View
    @InjectPresenter
    override lateinit var presenter: RepositoryActivityPresenter
    private val adapter = RepositoryAdapter(this)
    var view: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repository)
        setSupportActionBar(toolbar)
        repositoryRecyclerView.layoutManager = LinearLayoutManager(this)
        repositoryRecyclerView.adapter = adapter
        presenter.showAdapter(RepositoryBase().getDataBase())
        val inflater = LayoutInflater.from(this)
        subView = inflater.inflate(R.layout.layout_dialog, null)
        fab.setOnClickListener {
            presenter.requestOpenAddDialog()
        }
    }

    override fun onShowAdapter(dataBase: List<Repository>) {
        if (RepositoryBase().getDataBase().isNotEmpty()) {
            addAllItemRepository(dataBase)
        }
    }

    private fun addAllItemRepository(notes: List<Repository>) {
        adapter.list = notes
        adapter.refreshAdapter()
    }

    override fun removeList(list: List<Repository>, repository: Repository) {
        adapter.list = list
        adapter.refreshAdapter()
        RepositoryBase().deleteData(repository)
    }

    override fun setList(list: List<Repository>,repository: Repository
    ) {
        adapter.list = list
        adapter.refreshAdapter()
        RepositoryBase().putDataBase(repository)
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

            if(CompareRepository().compareDataBase(owner, repositoryName, RepositoryBase().getDataBase())) {
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
        workManager()
        super.onDestroy()
    }

    private fun workManager() {
        val myWorkRequest = PeriodicWorkRequest.Builder(MyWorker::class.java, 30, TimeUnit.MINUTES).build()
        WorkManager.getInstance().enqueue(myWorkRequest)
    }
}



