package com.example.githubparser.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubparser.Database.objectbox.ObjectBox
import com.example.githubparser.R
import com.example.githubparser.adapters.StargazersAdapter
import com.example.githubparser.model.Repository
import com.example.githubparser.model.Stargazers
import com.example.githubparser.mvp.StargazersView
import com.example.githubparser.mvp.presenters.StargazersPresenter
import com.omegar.mvp.presenter.InjectPresenter
import io.objectbox.kotlin.boxFor
import kotlinx.android.synthetic.main.activity_main.*

class StargazersActivity : BaseActivity(), StargazersView {

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

    private var stargazersBox = ObjectBox.boxStore.boxFor<Stargazers>()
    private val stargazersList = stargazersBox.query().build().find()
    @InjectPresenter
    lateinit var presenter: StargazersPresenter
    private var repositoryBox = ObjectBox.boxStore.boxFor<Repository>()
    private var ownerId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        stargazersRecyclerView.layoutManager = LinearLayoutManager(this)
        val ownerNameText = intent.getStringExtra(EXTRA_OWNER_NAME)
        val repositoryNameText = intent.getStringExtra(EXTRA_REPOSITORY_NAME)
        val stringDateText = intent.getStringExtra(EXTRA_DATE)

        repositoryBox.query().build().find().forEach {
            if (it.ownerName == ownerNameText && it.repositoryName == repositoryNameText) {
                ownerId = it.id
            }
        }
        presenter.showStargazers(ownerId, stringDateText)
    }

    override fun onShowStargazers(ownerId: Long, stringDateText: String) {
        var stargazers: Stargazers? = null
        stargazersList.forEach {
            if (it.idRepository == ownerId  && it.stringDate == stringDateText) {
                stargazers = it
            }
        }
        val listValue: List<String> = stargazers!!.username.split(",").map { it -> it.trim() }
        listValue.forEach {
            stargazersRecyclerView.adapter = StargazersAdapter(listValue)
        }
    }
}