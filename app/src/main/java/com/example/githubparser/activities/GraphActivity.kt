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
import com.example.githubparser.activities.StargazersActivity.Companion.createIntent
import com.example.githubparser.api.StargazersList
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.interfaces.OnChartValueSelectedListener
import io.objectbox.kotlin.boxFor
import kotlin.collections.ArrayList


class GraphActivity : BaseActivity(), OnChartValueSelectedListener {

    var notesStargazers = ObjectBox.boxStore.boxFor<Stargazers>()
    var stargazersList: List<StargazersList> = emptyList()
    var sortStargazerslist: List<StargazersList> = emptyList()
    var counterStargazers: Long = 1
    val labels = ArrayList<String>()

    private var ownerNameText: String = ""
    private var repositoryNameText: String = ""

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

        ownerNameText = intent.getStringExtra("ownerName")
        repositoryNameText = intent.getStringExtra("repositoryName")
        Log.d("test", "${ownerNameText}, ${repositoryNameText}")
        fetchStargazers(ownerNameText, repositoryNameText, counterStargazers)
    }


    private fun fetchStargazers(ownerName: String, repositoryName: String, counterStargazers: Long) {
        val notes = getnoteObjectbox()
        if (notes != null) {
            val entries = ArrayList<BarEntry>()
            var count: Int = 0
            var starsCount: Int = 0
            notes.forEach {
                if (it.owner == ownerName && it.repository == repositoryName) {
                    starsCount += it.likes
                    entries.add(BarEntry(it.likes.toFloat(), count))
                    count += 1
                }
            }
            if (entries.size != 0) {
                Log.d("test", "Size entries " + entries.size)
                Log.d("test", "Likes: " + starsCount)
                Log.d("test", "Сработало")
                //Log.d("test", "% ===== " + (starsCount / 100))
                //Вот оно место где будем исходя из ((starsCount / 100)+1) запрашивать в
                // fetchStargazersRetrofit(ownerName, repositoryName, ((starsCount / 100)+1))
                //getallUsers(ownerName, repositoryName)

                fetchStargazersRetrofit(ownerName, repositoryName, ((starsCount / 100) + 1).toLong())
                //setBarchart(entries)
            } else fetchStargazersRetrofit(ownerName, repositoryName, counterStargazers)
        } else fetchStargazersRetrofit(ownerName, repositoryName, counterStargazers)
    }

    private fun showBarOb(ownerName: String, repositoryName: String) {
        val notes = getnoteObjectbox()
        val entries = ArrayList<BarEntry>()
        var count: Int = 0
        var starsCount: Int = 0
        notes.forEach {
            if (it.owner == ownerName && it.repository == repositoryName) {
                starsCount += it.likes
                entries.add(BarEntry(it.likes.toFloat(), count))
                labels.add(it.month + " " + it.year)
                count += 1

            }
        }
        Log.d("test", " SsTars: " + starsCount)
        setBarchart(entries)
    }


    private fun fetchStargazersRetrofit(ownerName: String, repositoryName: String, counterStargazers: Long) {
        this.counterStargazers = counterStargazers
        StargazersApi().getStargazers(ownerName, repositoryName, counterStargazers.toString())
            .enqueue(object : Callback<List<StargazersList>> {
                override fun onFailure(call: Call<List<StargazersList>>, t: Throwable) {
                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<List<StargazersList>>, response: Response<List<StargazersList>>) {
                    Log.d("test", response.body().toString())
                    if (response.body() == null) {
                        Toast.makeText(applicationContext, "This repository is failed", Toast.LENGTH_LONG).show()
                    } else
                        stargazersCounter(response.body(), ownerName, repositoryName)
                }
            })
    }

    private fun stargazersCounter(body: List<StargazersList>?, ownerName: String, repositoryName: String) {
        //Здесь надо сделать обработку getAllUsers.compare(StargazersList)
        stargazersList += body!!
        sortStargazerslist
        if (body.size == 100) {
            counterStargazers += 1
            fetchStargazers(ownerName, repositoryName, counterStargazers)
        } else if (body.size < 100) {
            stargazersList?.forEach {
                val username = it.user.username
                val stargazer = it
                it.owner = ownerName
                it.repository = repositoryName
                counterStargazers = 1
                /*
                val stargazer = Stargazers(owner = it.owner, repository = it.repository, username = it.user.username,
                    stringDate = it.stringDate) //user = listOf(it.user))
                notesStargazers.put(stargazer)
                */
                var statusCompare: Boolean = false
                getallUsers(ownerName, repositoryName).forEach {
                    if (stargazer.user.username == it) {
                        statusCompare = true
                    }
                }
                if (statusCompare == false) {
                    sortStargazerslist += stargazer
                }
            }

            sortStargazerslist?.let {
                showStargazers(it, ownerName, repositoryName)
            }


            /*
            stargazersList?.let {
                showStargazers(it)
            }
            */
        }
    }


    private fun showStargazers(
        stargazer: List<StargazersList>,
        ownerName: String,
        repositoryName: String
    ) {
        val map = mutableMapOf<Int, Year>()

        stargazer.forEach {
            val date = it.getDate()
            val userName = it.user
            Log.d("test", "User: " + userName.username + " Date: " + date)
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
            monthhhh.ownerName = it.owner
            monthhhh.repositoryName = it.repository

            if (monthhhh.users == "") {
                monthhhh.users += userName.username
            } else monthhhh.users += ", " + userName.username


            val sdf = SimpleDateFormat("MMM")
            val currentDate = sdf.format(Date())
        }

        sortData(map, ownerName, repositoryName)
    }

    private fun sortData(
        map: MutableMap<Int, Year>,
        ownerName: String,
        repositoryName: String
    ) {
        map.forEach() {
            val year = it.value
            it.value.monthMap.forEach {
                val month = it.value
                val likes = month.likes
                val owner = month.ownerName
                val repository = month.repositoryName
                val monthName = month.monthName
                val users = it.value.users
                Log.d(
                    "test",
                    "Owner: " + owner + " Year: " + it.value.year + " Month: " + monthName + " Likes: " + likes + " Users: " + it.value.users
                )

                val stargazer = Stargazers(
                    owner = owner, repository = repository, username = users, likes = likes,
                    month = monthName, year = it.value.year.toString(), stringDate = monthName + " " + it.value.year
                )
                val statusCompare: Boolean = false
                getnoteObjectbox().forEach {
                    if (it.owner == stargazer.owner && it.repository == stargazer.repository && it.stringDate == stargazer.stringDate) {
                        it.likes += likes
                        it.username += users
                        statusCompare == true
                    }
                }
                if (statusCompare == false) {
                    setdataObjectbox(stargazer)
                }
            }
        }
        showBarOb(ownerName, repositoryName)
    }


    class Year {
        val monthMap = mutableMapOf<Int, Month>()
    }

    class Month {
        var ownerName: String = ""
        var repositoryName: String = ""
        var year: Int = 0
        var monthName: String = ""
        var likes = 0
        var users: String = ""
    }

    private fun setBarchart(entries: ArrayList<BarEntry>) {
        val barDataSet = BarDataSet(entries, "Likes")
        barDataSet.color = resources.getColor(com.example.githubparser.R.color.colorAccent)
        val data = BarData(labels, barDataSet)
        barChart.data = data // set the data and list of lables into chart
        barChart.setDrawValueAboveBar(true)
        barChart.isDoubleTapToZoomEnabled = false
        barChart.setTouchEnabled(true)
        barChart.setOnChartValueSelectedListener(this)
        if (entries.size > 5) {
            barChart.zoom(1.5F, 0F, 1.5F, 0F)
        }
        barChart.animateY(2000)
    }


    override fun onNothingSelected() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun onValueSelected(e: Entry?, dataSetIndex: Int) {
        Log.d("test", "e = " + e.toString())
        Log.d("test", "e.val = " + e!!.`val`)
        Log.d("test", "e.xIndex = " + e.xIndex)
        Log.d("test", "labels[e.xIndex] = " + labels[e.xIndex])
        barChart.getDataSetByIndex(e.xIndex)
        startActivity(createIntent(this, ownerNameText, repositoryNameText, labels[e.xIndex]))
    }

    private fun setdataObjectbox(stargazer: Stargazers) {
        notesStargazers.put(stargazer)
    }

    private fun getnoteObjectbox(): MutableList<Stargazers> {
        val notes = notesStargazers.query().build().find()
        return notes
    }

    private fun getallUsers(ownerName: String, repositoryName: String): List<String> {
        val notes = getnoteObjectbox()
        var starsCount: Int = 0
        var listValue: List<String> = listOf()
        notes.forEach {
            if (it.owner == ownerName && it.repository == repositoryName) {
                starsCount += it.likes
                val listValueCash: List<String> = it.username.split(",").map { it -> it.trim() }
                listValue += listValueCash
            }
        }
        return listValue
    }


}

