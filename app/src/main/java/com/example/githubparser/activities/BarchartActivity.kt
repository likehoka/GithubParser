package com.example.githubparser.activities

import android.os.Bundle
import com.example.githubparser.R
import kotlinx.android.synthetic.main.activity_barchart.*

class BarchartActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_barchart)
        bar_chart_vertical
    }
}
