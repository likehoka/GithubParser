package com.example.githubparser.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.githubparser.R
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

class GraphActivity : BaseActivity() {

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
        setContentView(R.layout.activity_graph)

        //test
        val ownerNameText = intent.getStringExtra("ownerName")
        val repositoryNameText = intent.getStringExtra("repositoryName")
        Log.d("test", "${ownerNameText}, ${repositoryNameText}")
        fetchStargazers(ownerNameText, repositoryNameText)
    }

    private fun setBarChart(map: MutableMap<Int, Year>) {
        val entries = ArrayList<BarEntry>()
        var count: Int = 0
        val labels = ArrayList<String>()
        map.forEach(){
            val year = it.value
            //Log.d("test", "year " + map.keys + map.)
            it.value.monthMap.forEach {
                val month = it.value
                val likes = month.likes
                val monthName = month.monthName
                Log.d("test", "month " + monthName + "; likes " + likes + " Year " + month.year)
                entries.add(BarEntry(likes.toFloat(), count))
                labels.add(monthName)
                count += 1
            }
        }
        val barDataSet = BarDataSet(entries, "Cells")
        val data = BarData(labels, barDataSet)
        barChart.data = data // set the data and list of lables into chart


        barChart.setDescription("Set Bar Chart Description")  // set the description
        barChart.setTouchEnabled(true)



        //barChart.setOnCli

        //barDataSet.setColors(ColorTemplate.COLORFUL_COLORS)

        barDataSet.color = resources.getColor(R.color.colorAccent)


        barChart.animateY(2000)
    }

    private fun fetchStargazers(ownerName: String, repositoryName: String) {
        //refreshLayout.isRefreshing = true
        StargazersApi().getStargazers(ownerName, repositoryName).enqueue(object : Callback<List<Stargazers>> {
            override fun onFailure(call: Call<List<Stargazers>>, t: Throwable) {
                Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<List<Stargazers>>, response: Response<List<Stargazers>>) {
                Log.d("test", response.body().toString())
                val stargazers = response.body()
                stargazers?.let {
                    showStargazers(it)
                }
            }
        })
    }

    private fun showStargazers(stargazer: List<Stargazers>) {
        //test
        val map = mutableMapOf<Int, Year>()

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
            //Log.d("test", "Текущая дата:$currentDate")

            Log.d("test", "Year " + year + " Month " + currentDate)//instance.time.month.toString() )
        }

        var count: Int = 0

        map.forEach {
            val year = it.value
            //Log.d("test", "year " + map.keys + map.)
            it.value.monthMap.forEach {
                val month = it.value
                val likes = month.likes
                val monthName = month.monthName
                Log.d("test", "[${count}]")
                Log.d("test", "month " + monthName + "; likes " + likes + " Year " + month.year)
                count +=1
            }
        }
        setBarChart(map)
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
