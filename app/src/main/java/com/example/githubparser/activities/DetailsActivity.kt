package com.example.githubparser.activities

import android.os.Bundle
import android.webkit.WebView
import com.example.githubparser.R
import com.example.githubparser.mvp.ViewDetailsActivity
import com.example.githubparser.mvp.presenters.DetailsActivityPresenter
import com.omegar.mvp.presenter.InjectPresenter
import com.omegar.mvp.presenter.PresenterType

class DetailsActivity : BaseActivity(), ViewDetailsActivity {

    @InjectPresenter(type = PresenterType.GLOBAL)
    lateinit var detailsActivityPresenter: DetailsActivityPresenter

    var webView: WebView? = null
    var result: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        result = intent.getStringExtra("webUrl")
        webView = findViewById<WebView>(R.id.webView)
        detailsActivityPresenter.showWebView()
    }

    override fun onShowWebView() {
        webView.also {
            it!!.loadUrl("https://github.com/$result")
        }
    }

}
