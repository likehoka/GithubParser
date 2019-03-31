package com.example.githubparser.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import com.example.githubparser.R

class DetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val intent = intent
        val result = intent.getStringExtra("webUrl")
        val webView = findViewById<WebView>(R.id.webview)
        webView.loadUrl(result)
    }
}
