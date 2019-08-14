package com.example.githubparser.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.githubparser.Database.objectbox.ObjectBox
import com.example.githubparser.api.StargazersApi
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import kotlinx.android.synthetic.main.activity_graph.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.githubparser.activities.StargazersActivity.Companion.createIntent
import com.example.githubparser.api.StargazersList
import com.example.githubparser.model.Repository
import com.example.githubparser.mvp.ViewGraphActivity
import com.example.githubparser.mvp.presenters.GraphActivityPresenter
import com.example.githubparser.utils.DistributeStars
import com.example.githubparser.utils.NewStarsgazers
import com.example.githubparser.utils.UsersGetAll
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.interfaces.OnChartValueSelectedListener
import com.omegar.mvp.presenter.InjectPresenter
import com.omegar.mvp.presenter.PresenterType
import io.objectbox.kotlin.boxFor
import kotlin.collections.ArrayList


class GraphActivity : BaseActivity(), OnChartValueSelectedListener, ViewGraphActivity {

    var entry: Entry? = null
    var indexBarchart: Int = 0
    var stargazersList: List<StargazersList> = emptyList()
    private var sortStargazerslist: List<StargazersList> = emptyList()
    var counterStargazers: Long = 1
    private val labels = ArrayList<String>()
    var notesRepository = ObjectBox.boxStore.boxFor<Repository>()
    private var ownerNameText: String = ""
    private var repositoryNameText: String = ""
    private var ownerId: Long = 0
    var compareBaseStatus = false
    @InjectPresenter(type = PresenterType.GLOBAL)
    lateinit var graphActivityPresenter: GraphActivityPresenter
    private val noteStargazers = UsersGetAll().getStargazersObjectbox()


    companion object {
        private const val EXTRA_OWNER_NAME = "ownerName"
        private const val EXTRA_REPOSITORY_NAME = "repositoryName"
        fun createIntent(
            context: Context,
            ownerName: String,
            repositoryName: String
        ): Intent {
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

        getDataBase().forEach {
            if (it.ownerName == ownerNameText && it.repositoryName == repositoryNameText) {
                ownerId = it.id
            }
        }
        showBarOb(ownerNameText, repositoryNameText)
        fetchStargazers(ownerNameText, repositoryNameText, ownerId, counterStargazers)
    }


    private fun fetchStargazers(ownerName: String, repositoryName: String, idOwner: Long, counterStargazers: Long) {
        if (noteStargazers != null && counterStargazers == "1".toLong()) {
            val entries = ArrayList<BarEntry>()
            var count = 0
            var starsCount = 0
            noteStargazers.forEach {
                if (it.owner == ownerName && it.repository == repositoryName) {
                    starsCount += it.likes
                    entries.add(BarEntry(it.likes.toFloat(), count))
                    count += 1
                }
            }

            if (entries.size != 0) {
                fetchStargazersRetrofit(ownerName, repositoryName, idOwner, ((starsCount / 100) + 1).toLong())
            } else fetchStargazersRetrofit(ownerName, repositoryName, idOwner, counterStargazers)
        } else fetchStargazersRetrofit(ownerName, repositoryName, idOwner, counterStargazers)
    }


    private fun showBarOb(ownerName: String, repositoryName: String) {

        Log.d("test", " showBarOb(ownerName: String, repositoryName: String)")
        val entries = ArrayList<BarEntry>()
        entries.clear()
        var count = 0
        var starsCount = 0
        labels.clear()
        UsersGetAll().getStargazersObjectbox().forEach {
            if (it.owner == ownerName && it.repository == repositoryName) {
                starsCount += it.likes
                entries.add(BarEntry(it.likes.toFloat(), count))
                labels.add(it.month + " " + it.year)
                count += 1
            }
        }
        setBarchart(entries)
    }


    private fun fetchStargazersRetrofit(
        ownerName: String,
        repositoryName: String,
        idOwner: Long,
        counterStargazers: Long
    ) {
        this.counterStargazers = counterStargazers
        Log.d("test", "counterStargazers = " + counterStargazers)
        StargazersApi().getStargazers(ownerName, repositoryName, counterStargazers.toString())
            .enqueue(object : Callback<List<StargazersList>> {
                override fun onFailure(call: Call<List<StargazersList>>, t: Throwable) {
                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<List<StargazersList>>, response: Response<List<StargazersList>>) {
                    Log.d("test", "Text Body: " + response.body().toString())
                    if (response.message() != "Forbidden") {
                        if (response.body() == null && stargazersList.size == 0) {
                            Log.d("test", "(response.body() == null && stargazersList.size == 0) ")
                            graphActivityPresenter.failedRepository()
                        } else {
                            Log.d("test", "(response.body() == null && stargazersList.size == 0)  else")
                            stargazersCounter(response.body(), ownerName, repositoryName, idOwner, response)

                        }
                    }

                    if (response.message() == "Forbidden" && stargazersList.size == 0) {
                        Log.d("test", "(response.message() == \"Forbidden\" && stargazersList.size == 0)")
                        Toast.makeText(applicationContext, "Превышен лимит запросов", Toast.LENGTH_LONG).show()
                        showBarOb(ownerName, repositoryName)
                    }

                    if (response.message() == "Forbidden" && stargazersList.size != 0) {
                        Log.d("test", "(response.message() == \"Forbidden\" && stargazersList.size != 0)")
                        //stargazersCounter(response.body(), ownerName, repositoryName, idOwner, response)
                        Toast.makeText(applicationContext, "Превышен лимит запросов", Toast.LENGTH_LONG).show()
                        //showBarOb(ownerName, repositoryName)
                        writeToBase(ownerName, repositoryName, idOwner, compareBaseStatus)
                    }
                }
            })
    }

    private fun stargazersCounter(
        body: List<StargazersList>?,
        ownerName: String,
        repositoryName: String,
        idOwner: Long,
        response: Response<List<StargazersList>>
    ) {
        if (body != null) {
            stargazersList += body
            counterStargazers += 1
            Log.d("test", "if (body != null) counterStargazers " + counterStargazers)
            Log.d("test", "if (body != null)" + " stargazersList.size: " + stargazersList.size)
        }

        //sortStargazerslist
        if (body != null && body.size == 100) {
            Log.d("test", " if (body != null && body.size == 100)")
            if (stargazersList.size <= 500) {
                fetchStargazers(ownerName, repositoryName, idOwner, counterStargazers)
            } else {
                Toast.makeText(applicationContext, "Загрузка данных", Toast.LENGTH_LONG).show()
                Log.d("test", " Загрузка данных")
                //compareBaseStatus = false
                writeToBase(ownerName, repositoryName, idOwner, compareBaseStatus)
            }

        }
        if (response.message() == "Forbidden") {
            Log.d("test", "response.message() == Forbidden")
            compareBaseStatus = false
            if (stargazersList.isNotEmpty()){
                writeToBase(ownerName, repositoryName, idOwner, compareBaseStatus)
            }

        }
        if (body == null){ //body.size < 100) {
            Log.d("test", "(body == null || body.size < 100)")
            Toast.makeText(applicationContext, "Загрузка завершена", Toast.LENGTH_LONG).show()
            compareBaseStatus = false
            counterStargazers = 1
            if (stargazersList.isNotEmpty()) {
                writeToBase(ownerName, repositoryName, idOwner, compareBaseStatus)
            }
        }

        if (body?.size!! < 100) {
            Log.d("test", "(body == null || body.size < 100)   1")
            Toast.makeText(applicationContext, "Загрузка завершена", Toast.LENGTH_LONG).show()
            compareBaseStatus = false
            counterStargazers = 1
            if (stargazersList.isNotEmpty()) {
                writeToBase(ownerName, repositoryName, idOwner, compareBaseStatus)
            }

        }
    }

    fun writeToBase(
        ownerName: String,
        repositoryName: String,
        idOwner: Long,
        compareBS: Boolean
    ) {
        val allUsersDB = UsersGetAll().getallUserss(ownerName, repositoryName)
        val countStars: Int = allUsersDB.size
        //sortStargazerslist = emptyList()
        Log.d("test", "countStars " + countStars)
        Log.d("test", "compareBS " + compareBS +" stargazersList.size" + stargazersList.size)
        if (!compareBS){
            stargazersList?.forEach {
                val stargazer = it
                it.owner = ownerName
                it.repository = repositoryName
                //counterStargazers = 1
                var statusCompare: Boolean = false
//то условие
                if(countStars < 100) {
                    UsersGetAll().getallUserss(ownerName, repositoryName).forEach {
                        if (stargazer.user.username == it) {
                            statusCompare = true
                        }
                    }
                }

                if (countStars >= 100) {

                    (allUsersDB.subList(countStars - 100, countStars)).forEach {
                        if (stargazer.user.username == it) {
                            statusCompare = true
                        }
                    }
                }
                Log.d("test", " statusCompare " + statusCompare)
                if (!statusCompare) {
                    sortStargazerslist += stargazer
                }
            }
        } else stargazersList.forEach {
            it.owner = ownerName
            it.repository = repositoryName
            sortStargazerslist += it
        }

        compareBaseStatus = true

        Log.d("test", "I am here #1, sortStargazerslist.size " + sortStargazerslist.size)

        sortStargazerslist?.let {
            NewStarsgazers().sortDataToDatabase(
                DistributeStars().distributeStargazers(it),
                this, idOwner, false
            )
        }
        if (stargazersList.size == 600) {
            Log.d("test", " (stargazersList.size == 600)")
            stargazersList = emptyList()
            sortStargazerslist = emptyList()
            showBarOb(ownerName, repositoryName)
            fetchStargazers(ownerName, repositoryName, idOwner, counterStargazers)
        } else {
            Log.d("test", " (stargazersList.size == 600) else")
            counterStargazers = 1
            stargazersList = emptyList()
            sortStargazerslist = emptyList()
            showBarOb(ownerName, repositoryName)
        }
    }

    private fun setBarchart(entries: ArrayList<BarEntry>) {
        var barDataSet: BarDataSet//? = null
        barDataSet = BarDataSet(entries, "Likes")
        barDataSet!!.color = resources.getColor(com.example.githubparser.R.color.colorAccent)
        val data = BarData(labels, barDataSet)
        graphActivityPresenter.setBarChartEntries(barChart, data, entries)
    }

    override fun onNothingSelected() {
        onValueSelected(entry, indexBarchart)
    }

    override fun onValueSelected(e: Entry?, dataSetIndex: Int) {
        entry = e
        indexBarchart = dataSetIndex
        Log.d("test", "e = " + e.toString())
        Log.d("test", "e.val = " + e!!.`val`)
        Log.d("test", "e.xIndex = " + e.xIndex)
        Log.d("test", "labels[e.xIndex] = " + labels[e.xIndex])
        barChart.getDataSetByIndex(e.xIndex)
        startActivity(createIntent(this, ownerNameText, repositoryNameText, labels[e.xIndex]))
        barChart.highlightValues(null)
    }

    private fun getDataBase(): MutableList<Repository> {
        return notesRepository.query().build().find()
    }

    override fun onShowMistake() {
        Toast.makeText(applicationContext, "This repository is failed", Toast.LENGTH_LONG).show()
    }

    override fun showBarChart(barChart: BarChart, data: BarData, entries: ArrayList<BarEntry>) {
        barChart.data = data // set the data and list of lables into chart
        barChart.setDrawValueAboveBar(true)
        barChart.isDoubleTapToZoomEnabled = false
        barChart.setTouchEnabled(true)
        barChart.setOnChartValueSelectedListener(this)
        //barChart.
        //if (entries.size > 5) {
        //barChart.zoom(1.5F, 0F, 1.5F, 0F)
        //}
        barChart.animateY(0)
    }
}