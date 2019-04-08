package com.example.githubparser.activities

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.githubparser.R
import com.example.githubparser.adapters.RepositoryAdapter
import com.example.githubparser.model.Repository

import kotlinx.android.synthetic.main.activity_repository.*
import kotlinx.android.synthetic.main.content_repository.*
import kotlinx.android.synthetic.main.layout_dialog.view.*

class RepositoryActivity : AppCompatActivity() {

    val adapter = RepositoryAdapter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repository)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            repositoryRecyclerView.layoutManager = LinearLayoutManager(this)
            fetchRepos(repositoryRecyclerView)
        }
    }

    private fun fetchRepos(repositoryRecyclerView: RecyclerView) {
        val inflater = LayoutInflater.from(this)
        val subView = inflater.inflate(R.layout.layout_dialog, null)
        val builder = AlertDialog.Builder(this@RepositoryActivity)

        builder.setTitle("Title AlertDialog")
        builder.setMessage("Message AlertDialog")
        builder.setView(subView)
        builder.setPositiveButton("Ok") {
            dialog, which ->  Toast.makeText(this, "We are press OK", Toast.LENGTH_LONG).show()
            val ownerText = subView.ownerEditText.text.toString()
            val repositoryText = subView.repositoryEditText.text.toString()
            val repository = Repository(ownerName = ownerText, repositoryName = repositoryText)

            repositoryRecyclerView.layoutManager = LinearLayoutManager(this)
            repositoryRecyclerView.adapter = adapter
            Log.d("mLog", "${ownerText},${repositoryText}")
            adapter.itemRepositoryList = listOf(Repository())
            adapter.addItemRepository(repository)
        }

        builder.setNegativeButton("Cancel") {
            dialog, which ->  Toast.makeText(this, "We are press Cancel", Toast.LENGTH_LONG).show()
        }
        builder.show()
    }

}

