package com.example.githubparser.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.webkit.WebView
import com.example.githubparser.R
import com.example.githubparser.mvp.DetailsView
import com.example.githubparser.mvp.presenters.DetailsPresenter
import com.omegar.mvp.presenter.InjectPresenter
import com.omegar.mvp.presenter.ProvidePresenter

class DetailsActivity : BaseActivity(), DetailsView {

    companion object {
        private const val EXTRA_WEBURL = "webUrl"

        fun createIntent(context: Context, webUrl: String): Intent {
            return Intent(context, DetailsActivity::class.java)
                .putExtra(EXTRA_WEBURL, webUrl)
        }

    }

    @InjectPresenter
    lateinit var presenter: DetailsPresenter
    private lateinit var webView: WebView

    @ProvidePresenter
    fun providePresenter(): DetailsPresenter {
        return DetailsPresenter(intent.getStringExtra(EXTRA_WEBURL))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        webView = findViewById(R.id.webView)
    }

    override fun showWebUrl(url: String) {
        webView.loadUrl("https://github.com/$url")
    }

}
