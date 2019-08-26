package com.example.githubparser.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
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
import com.example.githubparser.mvp.GraphView
import com.example.githubparser.mvp.presenters.GraphPresenter
import com.example.githubparser.utils.SortMap
import com.example.githubparser.utils.NewStarsgazers
import com.example.githubparser.utils.UsersStorage
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.interfaces.OnChartValueSelectedListener
import com.omegar.mvp.presenter.InjectPresenter
import com.omegar.mvp.presenter.PresenterType
import io.objectbox.kotlin.boxFor
import kotlin.collections.ArrayList


class GraphActivity : BaseActivity(), OnChartValueSelectedListener, GraphView {

    private var entryBarChart: Entry? = null
    private var indexBarchart: Int = 0
    private var stargazersList: List<StargazersList> = emptyList()
    private var repositoryBox = ObjectBox.boxStore.boxFor<Repository>()
    private var ownerNameText: String = ""
    private var repositoryNameText: String = ""
    @InjectPresenter(type = PresenterType.LOCAL)
    lateinit var presenter: GraphPresenter
    private val listStargazers = UsersStorage().getAllStargazersList()
    private var compareBaseStatus = false

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
        var ownerId: Long = 0
        ownerNameText = intent.getStringExtra(EXTRA_OWNER_NAME)
        repositoryNameText = intent.getStringExtra(EXTRA_REPOSITORY_NAME)
        getDataBase().forEach {
            if (it.ownerName == ownerNameText && it.repositoryName == repositoryNameText) {
                ownerId = it.id
            }
        }
        presenter.showListOfStars(ownerId)
        presenter.requestStargazers(ownerId, presenter.getPageCounter())
    }

    override fun onShowRequestStargazers(
        ownerId: Long,
        counterStargazers: Long
    ) {
        if (listStargazers != null && counterStargazers == 1.toLong()) {
            val entries = ArrayList<BarEntry>()
            var count = 0
            var starsCount = 0
            listStargazers.forEach {
                if (it.idRepository == ownerId) {
                    starsCount += it.likes
                    entries.add(BarEntry(it.likes.toFloat(), count))
                    count += 1
                }
            }
            if (entries.size != 0) {
                presenter.resuestStargazersRetrofit(
                    ownerId,
                    ((starsCount / 100) + 1).toLong()
                )
            } else presenter.resuestStargazersRetrofit(ownerId, counterStargazers)
        } else presenter.resuestStargazersRetrofit(ownerId, counterStargazers)
    }

    override fun onShowListOfStars(ownerId: Long) {
        val labels = ArrayList<String>()
        val entries = ArrayList<BarEntry>()
        entries.clear()
        var count = 0
        var starsCount = 0
        UsersStorage().getAllStargazersList().forEach {
            if (it.idRepository == ownerId) {
                starsCount += it.likes
                entries.add(BarEntry(it.likes.toFloat(), count))
                labels.add(it.month + " " + it.year)
                count += 1
            }
        }
        presenter.setBarChartValues(entries, labels)
    }

    override fun onRequestStargazersRetrofit(
        ownerId: Long,
        counterStargazers: Long
    ) {
        presenter.setPageCounter(counterStargazers)
        StargazersApi().getStargazers(ownerNameText, repositoryNameText, counterStargazers.toString())
            .enqueue(object : Callback<List<StargazersList>> {
                override fun onFailure(call: Call<List<StargazersList>>, t: Throwable) {
                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<List<StargazersList>>, response: Response<List<StargazersList>>) {
                    if (response.message() != "Forbidden") {
                        if (response.body() == null && stargazersList.isEmpty()) {
                            presenter.failedRepository()
                        } else {
                            presenter.bodySort(response.body(), ownerId, response)
                        }
                    }
                    if (response.message() == "Forbidden" && stargazersList.isEmpty()) {
                        Toast.makeText(applicationContext, "Превышен лимит запросов", Toast.LENGTH_LONG).show()
                        presenter.showListOfStars(ownerId)
                    }
                    if (response.message() == "Forbidden" && stargazersList.isNotEmpty()) {
                        Toast.makeText(applicationContext, "Превышен лимит запросов", Toast.LENGTH_LONG).show()
                        presenter.addItemDataBase(ownerId, compareBaseStatus)
                    }
                }
            })
    }

    override fun onBodySort(
        body: List<StargazersList>?,
        ownerId: Long,
        response: Response<List<StargazersList>>
    ) {
        if (body != null) {
            stargazersList += body
            presenter.setPageCounter(presenter.getPageCounter() + 1)
        }
        if (body != null && body.size == 100) {
            if (stargazersList.size <= 100) {
                presenter.requestStargazers(ownerId, presenter.getPageCounter())
            } else {
                presenter.addItemDataBase(ownerId, compareBaseStatus)
            }
        }
        if (response.message() == "Forbidden") {
            compareBaseStatus = false
            if (stargazersList.isNotEmpty()) {
                presenter.addItemDataBase(ownerId, compareBaseStatus)
            }

        }
        if (body == null) {
            Toast.makeText(applicationContext, "Загрузка завершена", Toast.LENGTH_LONG).show()
            compareBaseStatus = false
            presenter.setPageCounter(1)
            if (stargazersList.isNotEmpty()) {
                presenter.addItemDataBase(ownerId, compareBaseStatus)
            }
        }

        if (body?.size!! < 100) {
            Toast.makeText(applicationContext, "Загрузка завершена", Toast.LENGTH_LONG).show()
            compareBaseStatus = false
            presenter.setPageCounter(1)
            if (stargazersList.isNotEmpty()) {
                presenter.addItemDataBase(ownerId, compareBaseStatus)
            }
        }
    }

    override fun addItemToDataBase(
        ownerId: Long,
        baseStatus: Boolean
    ) {
        var sortStargazerslist: List<StargazersList> = emptyList()
        val countStars: Int = UsersStorage().getUsers(ownerId)!!.size
        if (!baseStatus) {
            stargazersList?.forEach {
                val stargazer = it
                it.idOwner = ownerId
                if (countStars == 0) {
                    sortStargazerslist += stargazer
                }

                if (countStars < 100 && countStars != 0) {
                    if (!UsersStorage().getUsers(ownerId)!!.contains(stargazer.user.username)) {
                        sortStargazerslist += stargazer
                    }
                }

                if (countStars >= 100) {
                    if (!UsersStorage().getUsers(ownerId)!!.contains(stargazer.user.username)) {
                        sortStargazerslist += stargazer
                    }
                }
            }
        } else stargazersList.forEach {
            it.idOwner = ownerId
            sortStargazerslist += it
        }
        compareBaseStatus = true

        sortStargazerslist?.let {
            val stars = SortMap().getStargazersMap(it)
            NewStarsgazers().sortDataToDatabase(
                ownerNameText, repositoryNameText,
                stars,
                this, ownerId, false
            )
        }

        if (stargazersList.size == 200) {
            stargazersList = emptyList()
            sortStargazerslist = emptyList()
            presenter.showListOfStars(ownerId)
            presenter.requestStargazers(ownerId, presenter.getPageCounter())
        } else {
            presenter.setPageCounter(1)
            stargazersList = emptyList()
            sortStargazerslist = emptyList()
            presenter.showListOfStars(ownerId)
        }
    }

    override fun setBarChart(
        entries: ArrayList<BarEntry>,
        labels: ArrayList<String>
    ) {
        presenter.setLabels(labels)
        var barDataSet = BarDataSet(entries, "Likes")
        barDataSet!!.color = resources.getColor(com.example.githubparser.R.color.colorAccent)
        val data = BarData(labels, barDataSet)
        presenter.setBarChartEntries(barChart, data, entries)
    }

    override fun onNothingSelected() {
        onValueSelected(entryBarChart, indexBarchart)
    }

    override fun onValueSelected(e: Entry?, dataSetIndex: Int) {
        entryBarChart = e
        indexBarchart = dataSetIndex
        val labels = presenter.getLabels()
        barChart.getDataSetByIndex(e!!.xIndex)
        startActivity(createIntent(this, ownerNameText, repositoryNameText, labels[e.xIndex]))
        barChart.highlightValues(null)
    }

    private fun getDataBase(): MutableList<Repository> {
        return repositoryBox.query().build().find()
    }

    override fun onShowMistake() {
        Toast.makeText(applicationContext, "This repository is failed", Toast.LENGTH_LONG).show()
    }

    override fun showBarChart(
        barChart: BarChart,
        data: BarData,
        entries: ArrayList<BarEntry>
    ) {
        barChart.data = data
        barChart.setDrawValueAboveBar(true)
        barChart.isDoubleTapToZoomEnabled = false
        barChart.setTouchEnabled(true)
        barChart.setOnChartValueSelectedListener(this)
        barChart.setDescription("Statistics")
        barChart.animateY(0)
    }
}