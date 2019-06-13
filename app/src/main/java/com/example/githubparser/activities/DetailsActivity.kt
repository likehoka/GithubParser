package com.example.githubparser.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import com.example.githubparser.R

class DetailsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val result = intent.getStringExtra("webUrl")
        val webView = findViewById<WebView>(R.id.webView)
        webView.loadUrl(result)
    }
}
