package com.example.githubparser.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubparser.R
import com.example.githubparser.adapters.StargazersAdapter
import com.example.githubparser.api.StargazersApi
import com.example.githubparser.api.StargazersList
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class StargazersActivity : BaseActivity() {

    companion object {

        private const val EXTRA_OWNER_NAME = "ownerName"
        private const val EXTRA_REPOSITORY_NAME = "repositoryName"

        fun createIntent(context: Context, ownerName: String, repositoryName: String): Intent {
            return Intent(context, StargazersActivity::class.java)
                .putExtra(EXTRA_OWNER_NAME, ownerName)
                .putExtra(EXTRA_REPOSITORY_NAME, repositoryName)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        stargazersRecyclerView.layoutManager = LinearLayoutManager(this)
        val ownerNameText = intent.getStringExtra(EXTRA_OWNER_NAME)
        val repositoryNameText = intent.getStringExtra(EXTRA_REPOSITORY_NAME)

        Log.d("mLog", "${ownerNameText}, ${repositoryNameText}")

        refreshLayout.setOnRefreshListener {
            fetchStargazers(ownerNameText, repositoryNameText)
        }
        fetchStargazers(ownerNameText, repositoryNameText)
    }

    private fun fetchStargazers(ownerName: String, repositoryName: String) {
        refreshLayout.isRefreshing = true
        StargazersApi().getStargazers(ownerName, repositoryName, "0").enqueue(object : Callback<List<StargazersList>> {
            override fun onFailure(call: Call<List<StargazersList>>, t: Throwable) {
                refreshLayout.isRefreshing = false
                Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<List<StargazersList>>, response: Response<List<StargazersList>>) {
                Log.d("mLog", response.body().toString())
                refreshLayout.isRefreshing = false
                val stargazers = response.body()
                stargazers?.let {
                    showStargazers(it)
                }
            }
        })
    }

    private fun showStargazers(stargazer: List<StargazersList>) {
        val map = mutableMapOf<Int, Year>()

        stargazer.forEach {
            val date = it.getDate()
            Log.d("test", "date " + date)

            val instance = Calendar.getInstance()
            instance.time = date
            val year = instance.get(Calendar.YEAR)
            val month = instance.get(Calendar.MONTH)

            var yearrr = map[year]
            if (yearrr == null) {
                yearrr = Year()
                map.put(year, yearrr)
            }
            var monthhhh = yearrr.monthMap[month]
            if (monthhhh == null) {
                monthhhh = Month()
                yearrr.monthMap[month] = monthhhh
            }
            monthhhh.likes += 1
        }

        map.forEach {
            val year = it.value
            Log.d("counter", "year " + year)
            it.value.monthMap.forEach {
                val month = it.value
                val likes = month.likes
                Log.d("counter", "month " + month + "; likes " + likes)
            }
        }
        stargazersRecyclerView.adapter = StargazersAdapter(stargazer)
    }

    class Year {
        val monthMap = mutableMapOf<Int, Month>()
    }
    class Month {
        var likes = 0
    }

}
