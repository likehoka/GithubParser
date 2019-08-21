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
    var indexBarchart: Int = 0 //нажатый месяц в barChart
    var stargazersList: List<StargazersList> = emptyList() //общий загружаемый лист из body()
    var counterStargazers: Long = 1 //счетчик страниц retrofit
    var notesRepository = ObjectBox.boxStore.boxFor<Repository>() //
    private var ownerNameText: String = ""
    private var repositoryNameText: String = ""

    var compareBaseStatus = false
    @InjectPresenter(type = PresenterType.GLOBAL)
    lateinit var presenter: GraphActivityPresenter
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
        var ownerId: Long = 0
        ownerNameText = intent.getStringExtra("ownerName")
        repositoryNameText = intent.getStringExtra("repositoryName")
        getDataBase().forEach {
            if (it.ownerName == ownerNameText && it.repositoryName == repositoryNameText) {
                ownerId = it.id
            }
        }
        presenter.showListOfStars(ownerId)
        presenter.requestStargazers(ownerId, counterStargazers)
    }

    override fun onShowRequestStargazers(
        ownerId: Long,
        counterStargazers: Long
    ) {
        if (noteStargazers != null && counterStargazers == "1".toLong()) {
            val entries = ArrayList<BarEntry>()
            var count = 0
            var starsCount = 0
            noteStargazers.forEach {
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
        val labels = ArrayList<String>() //лист месяцев загружаемых в entries
        Log.d("test", " listOfStars(ownerName: String, repositoryName: String)")
        val entries = ArrayList<BarEntry>()
        entries.clear()
        var count = 0
        var starsCount = 0
        UsersGetAll().getStargazersObjectbox().forEach {
            Log.d("test", "OwnerId = " + ownerId)
            if (it.idRepository == ownerId) {
                starsCount += it.likes
                entries.add(BarEntry(it.likes.toFloat(), count))
                labels.add(it.month + " " + it.year)
                count += 1
            }
        }
        Log.d("test", "Label.size: " + labels.size)
        presenter.setBarChartValues(entries, labels)
    }

    override fun onRequestStargazersRetrofit(
        ownerId: Long,
        counterStargazers: Long
    ) {
        this.counterStargazers = counterStargazers
        Log.d("test", "counterStargazers = " + counterStargazers)
        StargazersApi().getStargazers(ownerNameText, repositoryNameText, counterStargazers.toString())
            .enqueue(object : Callback<List<StargazersList>> {
                override fun onFailure(call: Call<List<StargazersList>>, t: Throwable) {
                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<List<StargazersList>>, response: Response<List<StargazersList>>) {
                    Log.d("test", "Text Body: " + response.body().toString())
                    if (response.message() != "Forbidden") {
                        if (response.body() == null && stargazersList.isEmpty()) {
                            Log.d("test", "(response.body() == null && stargazersList.size == 0) ")
                            presenter.failedRepository()
                        } else {
                            Log.d("test", "(response.body() == null && stargazersList.size == 0)  else")
                            presenter.bodySort(response.body(), ownerId, response)
                            //bodySorting(response.body(), ownerNameText, repositoryNameText, ownerId, response)
                        }
                    }
                    if (response.message() == "Forbidden" && stargazersList.isEmpty()) {
                        Log.d("test", "(response.message() == \"Forbidden\" && stargazersList.size == 0)")

                        Toast.makeText(applicationContext, "Превышен лимит запросов", Toast.LENGTH_LONG).show()
                        presenter.showListOfStars(ownerId)
                    }
                    if (response.message() == "Forbidden" && stargazersList.isNotEmpty()) {
                        Log.d("test", "(response.message() == \"Forbidden\" && stargazersList.size != 0)")
                        Toast.makeText(applicationContext, "Превышен лимит запросов", Toast.LENGTH_LONG).show()
                        presenter.addItemDataBase(ownerId, compareBaseStatus)
                        //writeToBase(ownerNameText, repositoryNameText, ownerId, compareBaseStatus)
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
            counterStargazers += 1
            Log.d("test", "if (body != null) counterStargazers " + counterStargazers)
            Log.d("test", "if (body != null)" + " stargazersList.size: " + stargazersList.size)
        }
        if (body != null && body.size == 100) {
            Log.d("test", " if (body != null && body.size == 100)")
            //if (stargazersList.size <= 500) {
            if (stargazersList.size <= 100) {
                presenter.requestStargazers(ownerId, counterStargazers)
            } else {
                Log.d("test", " Загрузка данных")
                //compareBaseStatus = false
                presenter.addItemDataBase(ownerId, compareBaseStatus)
            }

        }
        if (response.message() == "Forbidden") {
            Log.d("test", "response.message() == Forbidden")
            compareBaseStatus = false
            if (stargazersList.isNotEmpty()) {
                presenter.addItemDataBase(ownerId, compareBaseStatus)
            }

        }
        if (body == null) { //body.size < 100) {
            Log.d("test", "(body == null || body.size < 100)")
            Toast.makeText(applicationContext, "Загрузка завершена", Toast.LENGTH_LONG).show()
            compareBaseStatus = false
            counterStargazers = 1
            if (stargazersList.isNotEmpty()) {
                presenter.addItemDataBase(ownerId, compareBaseStatus)
            }
        }

        if (body?.size!! < 100) {
            Log.d("test", "(body == null || body.size < 100)   1")
            Toast.makeText(applicationContext, "Загрузка завершена", Toast.LENGTH_LONG).show()
            compareBaseStatus = false
            counterStargazers = 1
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
        val countStars: Int = UsersGetAll().getallUsersss(ownerId)!!.size
        Log.d("test", "countStars " + countStars)
        Log.d("test", "compareBS " + baseStatus + " stargazersList.size" + stargazersList.size)
        if (!baseStatus) {
            stargazersList?.forEach {
                val stargazer = it
                it.idOwner = ownerId
                if (countStars == 0) {
                    sortStargazerslist += stargazer
                }

                if (countStars < 100 && countStars != 0) {
                    Log.d("test", "if (countStars < 100 && countStars !=0) {")
                    if (UsersGetAll().getallUsersss(ownerId)!!.contains(stargazer.user.username)){
                        Log.d("test", "if(countStars < 100) {")
                    } else sortStargazerslist += stargazer
                }

                if (countStars >= 100) {
                    if (UsersGetAll().getallUsersss(ownerId)!!.contains(stargazer.user.username)){
                        Log.d("test", "if (countStars >= 100) {")
                    } else sortStargazerslist += stargazer
                }
            }
        } else stargazersList.forEach {
            it.idOwner = ownerId
            sortStargazerslist += it
        }

        compareBaseStatus = true

        Log.d("test", "I am here #1, sortStargazerslist.size " + sortStargazerslist.size)


        sortStargazerslist?.let {
            val stars = DistributeStars().distributeStargazers(it)
            Log.d("test", "val stars.size = " + stars.size)
            NewStarsgazers().sortDataToDatabase(
                ownerNameText, repositoryNameText,
                stars,
                this, ownerId, false
            )
        }

        //if (stargazersList.size == 600) {
        if (stargazersList.size == 200) {
            Log.d("test", " (stargazersList.size == 600)")
            stargazersList = emptyList()
            sortStargazerslist = emptyList()
            presenter.showListOfStars(ownerId)
            presenter.requestStargazers(ownerId, counterStargazers)
        } else {
            Log.d("test", " (stargazersList.size == 600) else")
            counterStargazers = 1
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
        var barDataSet = BarDataSet(entries, "Likes")//? = null
        barDataSet!!.color = resources.getColor(com.example.githubparser.R.color.colorAccent)
        val data = BarData(labels, barDataSet)
        presenter.setBarChartEntries(barChart, data, entries)
    }

    override fun onNothingSelected() {
        onValueSelected(entry, indexBarchart)
    }

    override fun onValueSelected(e: Entry?, dataSetIndex: Int) {
        entry = e
        indexBarchart = dataSetIndex
        val labels = presenter.getLabels()
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

    override fun showBarChart(
        barChart: BarChart,
        data: BarData,
        entries: ArrayList<BarEntry>
    ) {
        barChart.data = data // set the data and list of lables into chart
        barChart.setDrawValueAboveBar(true)
        barChart.isDoubleTapToZoomEnabled = false
        barChart.setTouchEnabled(true)
        barChart.setOnChartValueSelectedListener(this)
        //if (entries.size > 5) {
        //    barChart.zoom(1.5F, 0F, 1.5F, 0F)
        //}
        barChart.setDescription("Statistics")
        barChart.animateY(0)
    }
}