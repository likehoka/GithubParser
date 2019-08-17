package com.example.githubparser.activities

import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.*
import com.example.githubparser.Database.objectbox.ObjectBox
import com.example.githubparser.R
import com.example.githubparser.adapters.RepositoryAdapter
import com.example.githubparser.model.Repository
import com.example.githubparser.mvp.ViewRepositoryActivity
import com.example.githubparser.mvp.presenters.RepositoryActivityPresenter
import com.example.githubparser.utils.MyWorker
import com.omegar.mvp.presenter.InjectPresenter
import com.omegar.mvp.presenter.PresenterType
import com.omegar.mvp.presenter.ProvidePresenter
import com.omegar.mvp.presenter.ProvidePresenterTag
import io.objectbox.kotlin.boxFor

import kotlinx.android.synthetic.main.activity_repository.*
import kotlinx.android.synthetic.main.content_repository.*
import kotlinx.android.synthetic.main.layout_dialog.view.*
import java.util.concurrent.TimeUnit

class RepositoryActivity : BaseActivity(), ViewRepositoryActivity {
    @InjectPresenter(type = PresenterType.GLOBAL)
    lateinit var repositoryActivityPresenter: RepositoryActivityPresenter

    @ProvidePresenterTag(presenterClass = RepositoryActivityPresenter::class, type = PresenterType.GLOBAL)
    fun provideDialogPresenterTag(): String = "Hello"

    @ProvidePresenter(type = PresenterType.GLOBAL)
    fun provideDialogPresenter() = RepositoryActivityPresenter()

    private val adapter = RepositoryAdapter()
    var notesRepository = ObjectBox.boxStore.boxFor<Repository>()
    private lateinit var subView: View
    private var statusAlertDialog: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repository)
        setSupportActionBar(toolbar)
        repositoryActivityPresenter.showAdapter(adapter, repositoryRecyclerView)
        fab.setOnClickListener {
            repositoryActivityPresenter.showAlertDialog()
        }
    }

    override fun onCloseAlertDialog(dialog: DialogInterface) {
        dialog.dismiss()
        dialog.cancel()
        statusAlertDialog()
    }

    override fun onShowAlertDialog() {
        statusAlertDialog = true
        fetchRepos()
    }

    override fun onShowAlertDialogText(ownerText: String, repositoryText: String) {
        subView.ownerEditText.setText(ownerText)
        subView.repositoryEditText.setText(repositoryText)
    }

    override fun statusAlertDialog() {
        statusAlertDialog = false
    }

    //Вызываем workManager для просмотра результата каждые N минут
    private fun workManager() {
        val myWorkRequest = PeriodicWorkRequest.Builder(MyWorker::class.java, 30, TimeUnit.MINUTES).build()
        WorkManager.getInstance().enqueue(myWorkRequest)
    }

    private fun getDataBase(): MutableList<Repository> {
        return notesRepository.query().build().find()
    }

    override fun onDestroy() {
        if (statusAlertDialog){
            val ownerText = subView.ownerEditText.text.toString()
            val repositoryText = subView.repositoryEditText.text.toString()
            repositoryActivityPresenter.showTextAlertDialog(ownerText, repositoryText)
            Log.d("test", "onDestroy() statusAlertDialog == true")
        } else Log.d("test", "onDestroy() statusAlertDialog == false")
        super.onDestroy()
    }

    override fun onShowAdapter() {
        if (getDataBase().size != 0 || getDataBase().size != null) {
            adapter.addAllItemRepository(getDataBase())
        }
        //workManager()
        repositoryRecyclerView.layoutManager = LinearLayoutManager(this) as RecyclerView.LayoutManager?
        repositoryRecyclerView.adapter = adapter
    }

    private fun fetchRepos() {
        val inflater = LayoutInflater.from(this)
        subView = inflater.inflate(R.layout.layout_dialog, null)
        var builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Github Parser")
        builder.setView(subView)
        builder.setOnDismissListener { repositoryActivityPresenter.statusCloseAlertDialog() }
        builder.setPositiveButton("Ok") { dialog, which ->
            val ownerText = subView.ownerEditText.text.toString()
            val repositoryText = subView.repositoryEditText.text.toString()
            if (compareDataBase(ownerText, repositoryText, getDataBase())) {
                val repository = Repository(ownerName = ownerText, repositoryName = repositoryText)
                Log.d("test", "$ownerText,$repositoryText")
                adapter.addItemRepository(repository)
                notesRepository.put(repository)
            } else {
                Toast.makeText(this, "This repository has already been recorded", Toast.LENGTH_LONG).show()
                Log.d("test", "This repository has already been recorded")
            }
            repositoryActivityPresenter.closeAlertDialog(dialog)
        }
        builder.setNegativeButton("Cancel") { dialog, which ->
            repositoryActivityPresenter.closeAlertDialog(dialog)
            Toast.makeText(this, "We are press Cancel", Toast.LENGTH_LONG).show()
        }
        builder.show()
    }

    private fun compareDataBase(
        ownerText: String,
        repositoryText: String,
        dataBase: MutableList<Repository>
    ): Boolean {
        var status: Boolean = true // when the status is true we write the base
        dataBase.forEach {
            if (it.ownerName == ownerText && it.repositoryName == repositoryText) {
                status = false
            }
        }
        return status
    }
}
