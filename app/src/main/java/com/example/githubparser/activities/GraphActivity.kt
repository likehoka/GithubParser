package com.example.githubparser.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.githubparser.api.StargazersApi
import com.example.githubparser.model.Stargazers
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import kotlinx.android.synthetic.main.activity_graph.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import com.example.githubparser.Database.objectbox.ObjectBox
import com.example.githubparser.api.StargazersList
import io.objectbox.kotlin.boxFor


class GraphActivity : BaseActivity() {
    var notesStargazers = ObjectBox.boxStore.boxFor<Stargazers>()
    var stargazersList: List<StargazersList> = emptyList()
    var counterStargazers: Long = 0

    companion object {
        private const val EXTRA_OWNER_NAME = "ownerName"
        private const val EXTRA_REPOSITORY_NAME = "repositoryName"

        fun createIntent(context: Context, ownerName: String, repositoryName: String): Intent {
            return Intent(context, GraphActivity::class.java)
                .putExtra(EXTRA_OWNER_NAME, ownerName)
                .putExtra(EXTRA_REPOSITORY_NAME, repositoryName)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.githubparser.R.layout.activity_graph)

        val ownerNameText = intent.getStringExtra("ownerName")
        val repositoryNameText = intent.getStringExtra("repositoryName")
        Log.d("test", "${ownerNameText}, ${repositoryNameText}")
        fetchStargazers(ownerNameText, repositoryNameText, counterStargazers)
    }

    private fun fetchStargazers(ownerName: String, repositoryName: String, counterStargazers: Long) {
        //refreshLayout.isRefreshing = true
        var count: String = ""
        count == counterStargazers.toString()
        StargazersApi().getStargazers(ownerName, repositoryName, count).enqueue(object : Callback<List<StargazersList>> {
            override fun onFailure(call: Call<List<StargazersList>>, t: Throwable) {
                Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<List<StargazersList>>, response: Response<List<StargazersList>>) {
                Log.d("test", response.body().toString())
                stargazersCounter(response.body(), ownerName, repositoryName)
            }
        })
    }

    private fun stargazersCounter(body: List<StargazersList>?,ownerName: String,repositoryName: String
    ) {

        //val stargazersList = body
        stargazersList += body!!

        if (body.size == 100) {
            counterStargazers++
            Log.d("test", "stargazersList.size = " + stargazersList.size)
            fetchStargazers(ownerName, repositoryName, counterStargazers)
        } else {
            stargazersList?.forEach {
                it.owner = ownerName
                it.repository = repositoryName

                val stargazer = Stargazers(owner = it.owner, repository = it.repository, username = it.user.username,
                    stringDate = it.stringDate) //user = listOf(it.user))
                Log.d("test", "Owner: " + it.owner + " Repository: " + it.repository +" User: " + it.user +  " Date: " + it.getDate())
                notesStargazers.put(stargazer)
            }
            val notes = notesStargazers.query().build().find()
            notes.forEach {
                Log.d("test", "Owner: " + it.owner +" Rep: " + it.repository + " Date: " + it.stringDate + " Username: " + it.username)
            }

            stargazersList?.let {
                showStargazers(it)
            }
        }
    }


    private fun showStargazers(stargazer: List<StargazersList>) {
        val map = mutableMapOf<Int, Year>()

        Log.d("mLog:", "StargazersList Size is " + stargazer.size)

        stargazer.forEach {
            val date = it.getDate()
            //Log.d("test", "date " + date )

            val instance = Calendar.getInstance()
            instance.time = date
            val year = instance.get(Calendar.YEAR)
            val month = instance.get(Calendar.MONTH)

            var yearrr = map[year]
            if (yearrr == null) {
                yearrr = Year()
                map.put(year, yearrr)  //ключ значение
            }
            var monthhhh = yearrr.monthMap[month]
            if (monthhhh == null) {
                monthhhh = Month()
                yearrr.monthMap[month] = monthhhh
            }
            monthhhh.likes += 1
            monthhhh.monthName = (instance.time.month + 1).toString()
            monthhhh.year = year

            val sdf = SimpleDateFormat("MMM")
            val currentDate = sdf.format(Date())
        }

        var count: Int = 0

        map.forEach {
            val year = it.value
            //Log.d("test", "year " + map.keys + map.)
            it.value.monthMap.forEach {
                val month = it.value
                val likes = month.likes
                val monthName = month.monthName
                count +=1
            }
        }
        setBarChart(map)
    }

    private fun setBarChart(map: MutableMap<Int, Year>) {
        val entries = ArrayList<BarEntry>()
        var count: Int = 0
        val labels = ArrayList<String>()
        map.forEach(){
            val year = it.value
            it.value.monthMap.forEach {
                val month = it.value
                val likes = month.likes
                val monthName = month.monthName
                entries.add(BarEntry(likes.toFloat(), count))
                labels.add(monthName)
                count += 1
            }
        }
        val barDataSet = BarDataSet(entries, "Cells")
        val data = BarData(labels, barDataSet)
        barChart.data = data // set the data and list of lables into chart

        if (entries.size > 5) {
            barChart.setDescription("Set Bar Chart Description")  // set the description
            barChart.setTouchEnabled(true)
            barChart.zoom(2F,0F,2F,0F)

            barChart.isDoubleTapToZoomEnabled = false
        } else {barChart.setDescription("Set Bar Chart Description")  // set the description
            barChart.setTouchEnabled(true)

            barChart.isDoubleTapToZoomEnabled = false
        }

        //barDataSet.setColors(ColorTemplate.COLORFUL_COLORS)
        barDataSet.color = resources.getColor(com.example.githubparser.R.color.colorAccent)


        barChart.animateY(2000)
    }

    class Year {
        val monthMap = mutableMapOf<Int, Month>()
    }

    class Month {
        var year: Int = 0
        var monthName: String = ""
        var likes = 0
    }

}
