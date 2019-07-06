package com.example.githubparser.Database.objectbox

import android.content.Context
import android.util.Log
import com.example.githubparser.BuildConfig
import com.example.githubparser.model.MyObjectBox
import io.objectbox.BoxStore

object ObjectBox {
    lateinit var boxStore: BoxStore
        private set

    fun init(context: Context) {
        boxStore = MyObjectBox.builder().androidContext(context.applicationContext).build()
    }
}