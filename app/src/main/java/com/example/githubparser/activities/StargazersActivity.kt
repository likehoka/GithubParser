package com.example.githubparser.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.githubparser.Database.objectbox.ObjectBox
import com.example.githubparser.R
import com.example.githubparser.adapters.StargazersAdapter
import com.example.githubparser.api.StargazersApi
import com.example.githubparser.api.StargazersList
import com.example.githubparser.model.Stargazers
import io.objectbox.kotlin.boxFor
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class StargazersActivity : BaseActivity() {
    var notesStargazers = ObjectBox.boxStore.boxFor<Stargazers>()

    companion object {

        private const val EXTRA_OWNER_NAME = "ownerName"
        private const val EXTRA_REPOSITORY_NAME = "repositoryName"
        private const val EXTRA_DATE = "stringDate"

        fun createIntent(context: Context, ownerName: String, repositoryName: String, stringDate: String): Intent {
            return Intent(context, StargazersActivity::class.java)
                .putExtra(EXTRA_OWNER_NAME, ownerName)
                .putExtra(EXTRA_REPOSITORY_NAME, repositoryName)
                .putExtra(EXTRA_DATE, stringDate)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        stargazersRecyclerView.layoutManager = LinearLayoutManager(this) as RecyclerView.LayoutManager?
        val ownerNameText = intent.getStringExtra("ownerName")
        val repositoryNameText = intent.getStringExtra("repositoryName")
        val stringDateText = intent.getStringExtra("stringDate")
        Log.d("test", "${ownerNameText}, ${repositoryNameText}, ${stringDateText}")
        showStargazers(ownerNameText, repositoryNameText, stringDateText)
    }

    private fun showStargazers(ownerNameText: String, repositoryNameText: String, stringDateText: String) {
        val notes = notesStargazers.query().build().find()
        var stargazers: Stargazers? = null
        notes.forEach {
            if (it.owner == ownerNameText && it.repository == repositoryNameText && it.stringDate == stringDateText) {
                stargazers = it
            }
        }
        Log.d(
            "test",
            "DB Owner:" + stargazers!!.owner + stargazers!!.repository + " Month/Year" + stargazers!!.month + "/" +
                    stargazers!!.year + " Users: " + stargazers!!.username
        )

        val listValue: List<String> = stargazers!!.username.split(",").map { it -> it.trim() }
        listValue.forEach {
            Log.d("test", "ALL USERS: " + it)

            stargazersRecyclerView.adapter = StargazersAdapter(listValue)
        }
    }


        /*
        private fun fetchStargazers(ownerName: String, repositoryName: String, stringDate: String) {
            refreshLayout.isRefreshing = true
            StargazersApi().getStargazers(ownerName, repositoryName, "1").enqueue(object : Callback<List<StargazersList>> {
                override fun onFailure(call: Call<List<StargazersList>>, t: Throwable) {
                    refreshLayout.isRefreshing = false
                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<List<StargazersList>>, response: Response<List<StargazersList>>) {
                    //Log.d("test", response.body().toString())
                    refreshLayout.isRefreshing = false
                    val stargazers = response.body()
                    stargazers?.let {
                        showStargazers(it)
                    }
                }
            })
        }

        //private fun showStargazers(stargazer: List<StargazersList>) {
        private fun showStargazers(stargazer: List<StargazersList>) {
            val map = mutableMapOf<Int, Year>()

            stargazer.forEach {
                val date = it.getDate()
                //Log.d("test", "date " + date)

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
        */


}