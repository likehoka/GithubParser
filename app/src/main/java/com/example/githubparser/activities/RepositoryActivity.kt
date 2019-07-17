package com.example.githubparser.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.githubparser.Database.objectbox.ObjectBox
import com.example.githubparser.R
import com.example.githubparser.adapters.RepositoryAdapter
import com.example.githubparser.model.Repository
import io.objectbox.kotlin.boxFor

import kotlinx.android.synthetic.main.activity_repository.*
import kotlinx.android.synthetic.main.content_repository.*
import kotlinx.android.synthetic.main.layout_dialog.view.*

class RepositoryActivity : BaseActivity() {


    override fun onDestroy() {
        val intent = Intent(this, MyService::class.java)
        if (this != null) {
            this.startService(intent)
        }
        Log.d("test", "Destroy Service" )
        super.onDestroy()
    }

    private val adapter = RepositoryAdapter()
    var notesRepository = ObjectBox.boxStore.boxFor<Repository>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repository)
        setSupportActionBar(toolbar)

        val intent = Intent(this, MyService::class.java)
        if (this != null) {
            this.startService(intent)
        }



        if (getDataBase().size != 0 || getDataBase().size != null) {
            adapter.addAllItemRepository(getDataBase())
        }
        repositoryRecyclerView.layoutManager = LinearLayoutManager(this) as RecyclerView.LayoutManager?
        repositoryRecyclerView.adapter = adapter
        fab.setOnClickListener {
            fetchRepos()
        }
    }

    private fun getDataBase(): MutableList<Repository> {
        val notes = notesRepository.query().build().find()
        Log.d("test", "Size = " + notes.size.toString())
        Log.d("test", "This is notes Log:")
        notes.forEach {
            Log.d("test", "id = " + it.id + " owner = " + it.ownerName +
                    " repository = " + it.repositoryName)
        }
        return notes
    }

    private fun compareDataBase(ownerText: String, repositoryText: String): Boolean {
        val notes = notesRepository.query().build().find()
        var status: Boolean = true // when the status is true we write the base

        notes.forEach {
            if (it.ownerName == ownerText && it.repositoryName == repositoryText) {
                status = false
            }
        }
        return status
    }

    private fun fetchRepos() {
        val inflater = LayoutInflater.from(this)
        val subView = inflater.inflate(R.layout.layout_dialog, null)
        val builder = AlertDialog.Builder(this)

        builder.setTitle("Title AlertDialog")
        builder.setMessage("Message AlertDialog")
        builder.setView(subView)
        builder.setPositiveButton("Ok") { dialog, which ->
            val ownerText = subView.ownerEditText.text.toString()
            val repositoryText = subView.repositoryEditText.text.toString()

            if (compareDataBase(ownerText, repositoryText)) {
                val repository = Repository(ownerName = ownerText, repositoryName = repositoryText)
                Log.d("test", "$ownerText,$repositoryText")
                adapter.addItemRepository(repository)
                notesRepository.put(repository)
            } else {
                Toast.makeText(this, "This repository has already been recorded", Toast.LENGTH_LONG).show()
                Log.d("test", "This repository has already been recorded")
            }
        }
        subView.ownerEditText.setText("Omega-R")
        subView.repositoryEditText.setText("OmegaRecyclerView")

        builder.setNegativeButton("Cancel") { dialog, which ->
            Toast.makeText(this, "We are press Cancel", Toast.LENGTH_LONG).show()
        }
        builder.show()
    }
}

