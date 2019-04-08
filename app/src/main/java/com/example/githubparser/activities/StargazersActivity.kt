package com.example.githubparser.activities

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubparser.R
import com.example.githubparser.api.StargazersApi
import com.example.githubparser.adapters.StargazersAdapter
import com.example.githubparser.model.Stargazers
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StargazersActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intent = intent
        val ownerNameText = intent.getStringExtra("ownerName")
        val repositoryNameText = intent.getStringExtra("repositoryName")

        Log.d("mLog", "${ownerNameText}, ${repositoryNameText}")

        refreshLayout.setOnRefreshListener {
            fetchStargazers()
        }
        fetchStargazers()
    }

    private fun fetchStargazers() {
        refreshLayout.isRefreshing = true
        StargazersApi().getStargazers().enqueue(object : Callback<List<Stargazers>> {
            override fun onFailure(call: Call<List<Stargazers>>, t: Throwable) {
                refreshLayout.isRefreshing = false
                Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<List<Stargazers>>, response: Response<List<Stargazers>>) {
                Log.d("mLog", response.body().toString())
                refreshLayout.isRefreshing = false
                val stargazers = response.body()
                stargazers?.let {
                    showStargazers(it)
                }
            }
        })
    }

    private fun showStargazers(stargazer: List<Stargazers>) {
        stargazersRecyclerView.layoutManager = LinearLayoutManager(this)
        stargazersRecyclerView.adapter = StargazersAdapter(stargazer)
    }
}
