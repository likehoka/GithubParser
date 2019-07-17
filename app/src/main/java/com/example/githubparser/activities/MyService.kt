package com.example.githubparser.activities

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

class MyService : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
        Log.d("test", "onStartCommand Service" )
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("test", "onCreate Service" )

        Thread.sleep(30_000)

        var i: Long = 0
        while (i < 30000) {
            Log.d("test", "I = " + i)
            i++
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("test", "Destroy Service" )
    }

    override fun onBind(intent: Intent): IBinder {
        Log.d("test", "onBind Service" )
        TODO("Return the communication channel to the service.")
    }
}
