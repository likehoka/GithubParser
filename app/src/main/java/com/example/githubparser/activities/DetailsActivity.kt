package com.example.githubparser.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.webkit.WebView
import com.example.githubparser.R
import com.example.githubparser.mvp.DetailsView
import com.example.githubparser.mvp.presenters.DetailsPresenter
import com.omegar.mvp.presenter.InjectPresenter

class DetailsActivity : BaseActivity(), DetailsView {

    @InjectPresenter
    lateinit var presenter: DetailsPresenter
    private lateinit var webView: WebView
    private var url: String? = ""

    companion object {
        private const val EXTRA_WEBURL = "webUrl"
        fun createIntent(
            context: Context,
            webUrl: String
        ): Intent {
            return Intent(context, DetailsActivity::class.java)
                .putExtra(EXTRA_WEBURL, webUrl)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        url = intent.getStringExtra(EXTRA_WEBURL)
        webView = findViewById<WebView>(R.id.webView)
        presenter.showWebView()
    }

    override fun onShowWebView() {
        webView.also {
            it!!.loadUrl("https://github.com/$url")
        }
    }

}
