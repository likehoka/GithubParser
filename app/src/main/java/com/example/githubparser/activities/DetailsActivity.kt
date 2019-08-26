package com.example.githubparser.activities

import android.os.Bundle
import android.webkit.WebView
import com.example.githubparser.R
import com.example.githubparser.mvp.DetailsView
import com.example.githubparser.mvp.presenters.DetailsPresenter
import com.omegar.mvp.presenter.InjectPresenter
import com.omegar.mvp.presenter.PresenterType

class DetailsActivity : BaseActivity(), DetailsView {

    @InjectPresenter(type = PresenterType.GLOBAL)
    lateinit var presenter: DetailsPresenter

    private var webView: WebView? = null
    private var url: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        url = intent.getStringExtra("webUrl")
        webView = findViewById<WebView>(R.id.webView)
        presenter.showWebView()
    }

    override fun onShowWebView() {
        webView.also {
            it!!.loadUrl("https://github.com/$url")
        }
    }

}
