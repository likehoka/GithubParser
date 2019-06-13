package com.example.githubparser.activities

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubparser.R
import com.example.githubparser.adapters.RepositoryAdapter
import com.example.githubparser.api.UserApi
import com.example.githubparser.model.Repository

import kotlinx.android.synthetic.main.activity_repository.*
import kotlinx.android.synthetic.main.content_repository.*
import kotlinx.android.synthetic.main.layout_dialog.view.*
class RepositoryActivity : BaseActivity() {

    private val adapter = RepositoryAdapter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repository)
        setSupportActionBar(toolbar)
        repositoryRecyclerView.layoutManager = LinearLayoutManager(this)
        repositoryRecyclerView.adapter = adapter
        fab.setOnClickListener {
            fetchRepos()
        }
    }

    private fun fetchRepos() {
        val inflater = LayoutInflater.from(this)
        val subView = inflater.inflate(R.layout.layout_dialog, null)
        val builder = AlertDialog.Builder(this)

        builder.setTitle("Title AlertDialog")
        builder.setMessage("Message AlertDialog")
        builder.setView(subView)
        builder.setPositiveButton("Ok") { dialog, which ->
            Toast.makeText(this, "We are press OK", Toast.LENGTH_LONG).show()
            val ownerText = subView.ownerEditText.text.toString()
            val repositoryText = subView.repositoryEditText.text.toString()
            val repository = Repository(ownerName = ownerText, repositoryName = repositoryText)
            Log.d("test", "$ownerText,$repositoryText")
            adapter.addItemRepository(repository)
        }
        subView.ownerEditText.setText("Omega-R")
        subView.repositoryEditText.setText("OmegaRecyclerView")
        builder.setNegativeButton("Cancel") { dialog, which ->
            Toast.makeText(this, "We are press Cancel", Toast.LENGTH_LONG).show()
        }
        builder.show()
    }

    private fun getuserData() {
        //UserApi.get
    }

}

