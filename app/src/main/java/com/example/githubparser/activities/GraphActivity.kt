package com.example.githubparser.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.githubparser.Database.objectbox.ObjectBox
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import kotlinx.android.synthetic.main.activity_graph.*
import com.example.githubparser.activities.StargazersActivity.Companion.createIntent
import com.example.githubparser.api.StargazersList
import com.example.githubparser.model.Repository
import com.example.githubparser.model.Stargazers
import com.example.githubparser.mvp.GraphView
import com.example.githubparser.mvp.presenters.GraphPresenter
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.interfaces.OnChartValueSelectedListener
import com.omegar.mvp.presenter.InjectPresenter
import com.omegar.mvp.presenter.PresenterType
import io.objectbox.kotlin.boxFor
import retrofit2.Response
import kotlin.collections.ArrayList


class GraphActivity : BaseActivity(), OnChartValueSelectedListener, GraphView {

    private var entryBarChart: Entry? = null
    private var indexBarchart: Int = 0
    private var repositoryBox = ObjectBox.boxStore.boxFor<Repository>()
    private var ownerNameText: String = ""
    private var repositoryNameText: String = ""
    @InjectPresenter(type = PresenterType.LOCAL)
    lateinit var presenter: GraphPresenter

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
        presenter.setContext(this)
        presenter.showListOfStars(ownerId)
        presenter.requestStargazers(ownerId, presenter.getPageCounter(), ownerNameText, repositoryNameText)
    }

    private fun getDataBase(): MutableList<Repository> {
        return repositoryBox.query().build().find()
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

    override fun limitRequest() {
        Toast.makeText(applicationContext, "Превышен лимит запросов", Toast.LENGTH_LONG).show()
    }

    override fun loadingComplete() {
        Toast.makeText(applicationContext, "Загрузка завершена", Toast.LENGTH_LONG).show()
    }

    override fun setStargazersCounter(
        ownerId: Long,
        toLong: Long,
        ownerNameText: String,
        repositoryNameText: String
    ) {
        presenter.requestStargazersRetrofit(ownerId,
            toLong,
            ownerNameText,
            repositoryNameText)
    }

    override fun setPage(counter: Long) {
        presenter.setPageCounter(counter)
    }

    override fun callBodySort(
        body: List<StargazersList>?,
        ownerId: Long,
        response: Response<List<StargazersList>>,
        ownerNameText: String,
        repositoryNameText: String
    ) {
        presenter.bodySort(body, ownerId, response, ownerNameText, repositoryNameText)
    }

    override fun addItemBase(
        ownerId: Long,
        compareBaseStatus: Boolean,
        ownerNameText: String,
        repositoryNameText: String
    ) {
        presenter.addItemDataBase(ownerId, compareBaseStatus, ownerNameText, repositoryNameText)
    }

    override fun requestStargazers(
        ownerId: Long,
        pageCounter: Long,
        ownerNameText: String,
        repositoryNameText: String
    ) {
        presenter.requestStargazers(ownerId, pageCounter, ownerNameText, repositoryNameText)
    }
}