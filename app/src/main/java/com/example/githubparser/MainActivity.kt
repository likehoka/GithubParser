package com.example.githubparser

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubparser.API.APIStargazers
import com.example.githubparser.Adapter.AdapterStargazers
import com.example.githubparser.Model.Stargazers
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        refreshLayout.setOnRefreshListener {
            fetchStargazers()
        }
        fetchStargazers()
    }

    private fun fetchStargazers() {
        refreshLayout.isRefreshing = true
        APIStargazers().getStargazers().enqueue(object : Callback<List<Stargazers>> {
            override fun onFailure(call: Call<List<Stargazers>>, t: Throwable) {
                refreshLayout.isRefreshing = false
                Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
            }
            override fun onResponse(call: Call<List<Stargazers>>, response: Response<List<Stargazers>>) {
                refreshLayout.isRefreshing = false
                val stargazers = response.body()
                stargazers?.let {
                    showStargazers(it)
                }
            }
        })
    }

    private fun showStargazers(stargazer: List<Stargazers>) {
        recyclerViewStargazers.layoutManager = LinearLayoutManager(this)
        recyclerViewStargazers.adapter = AdapterStargazers(stargazer)
    }
}
