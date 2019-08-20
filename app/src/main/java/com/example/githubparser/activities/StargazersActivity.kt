package com.example.githubparser.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubparser.Database.objectbox.ObjectBox
import com.example.githubparser.R
import com.example.githubparser.adapters.StargazersAdapter
import com.example.githubparser.model.Repository
import com.example.githubparser.model.Stargazers
import com.example.githubparser.mvp.ViewStargazersActivity
import com.example.githubparser.mvp.presenters.StargazersActivityPresenter
import com.omegar.mvp.presenter.InjectPresenter
import com.omegar.mvp.presenter.PresenterType
import io.objectbox.kotlin.boxFor
import kotlinx.android.synthetic.main.activity_main.*

class StargazersActivity : BaseActivity(), ViewStargazersActivity {

    var notesStargazers = ObjectBox.boxStore.boxFor<Stargazers>()
    val notes = notesStargazers.query().build().find()
    @InjectPresenter(type = PresenterType.GLOBAL)
    lateinit var stargazersPresenter: StargazersActivityPresenter
    var notesRepository = ObjectBox.boxStore.boxFor<Repository>()
    private var ownerId: Long = 0

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
        stargazersRecyclerView.layoutManager = LinearLayoutManager(this)
        val ownerNameText = intent.getStringExtra("ownerName")
        val repositoryNameText = intent.getStringExtra("repositoryName")
        val stringDateText = intent.getStringExtra("stringDate")

        getDataBase().forEach {
            if (it.ownerName == ownerNameText && it.repositoryName == repositoryNameText) {
                ownerId = it.id
            }
        }

        Log.d("test", "${ownerNameText}, ${repositoryNameText}, ${stringDateText}")
        stargazersPresenter.showStargazers(ownerId, stringDateText)
    }

    private fun getDataBase(): MutableList<Repository> {
        return notesRepository.query().build().find()
    }

    override fun onShowStargazers(ownerId: Long, stringDateText: String) {
        var stargazers: Stargazers? = null
        notes.forEach {
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