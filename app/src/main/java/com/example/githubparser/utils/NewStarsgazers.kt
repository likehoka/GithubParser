package com.example.githubparser.utils

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.githubparser.Database.objectbox.ObjectBox
import com.example.githubparser.R
import com.example.githubparser.activities.GraphActivity
import com.example.githubparser.model.Stargazers
import io.objectbox.kotlin.boxFor

class NewStarsgazers {

    private var stargazersBox = ObjectBox.boxStore.boxFor<Stargazers>()

    private fun getStargazersList(): MutableList<Stargazers> {
        return stargazersBox.query().build().find()
    }

    fun sortDataToDatabase(
        ownerName: String,
        repositoryName: String,
        map: MutableMap<Int, YearMap>,
        context: Context,
        idOwner: Long,
        statusService: Boolean
    ) {
        map.forEach { year ->
            year.value.monthMap.forEach { yearValue ->
                val stargazer = Stargazers(
                    idRepository = yearValue.value.ownerId,
                    username = yearValue.value.user, likes = yearValue.value.likes,
                    month = yearValue.value.monthName, year = yearValue.value.year.toString(),
                    stringDate = yearValue.value.monthName + " " + yearValue.value.year
                )
                var stargazers: Stargazers? = null
                var stargazersList = UsersStorage().getStargazersList(stargazer.idRepository, stargazer.stringDate)
                if (stargazersList!!.contains(stargazer.stringDate)) {
                    getStargazersList().forEach {
                        if (it.idRepository == idOwner && it.stringDate == stargazer.stringDate) {
                            it.likes += yearValue.value.likes
                            it.username += ", " + yearValue.value.user
                            stargazers = it
                            getStargazersList().remove(it)
                            stargazers?.let { it1 -> UsersStorage().setStargazers(it1) }
                            if (statusService) {
                                callGraphActivity(ownerName, repositoryName, idOwner, context)
                            }
                        }
                    }
                } else {
                    if (statusService) {
                        callGraphActivity(ownerName, repositoryName, idOwner, context)
                    } else  {
                        UsersStorage().setStargazers(stargazer)
                    }
                }
            }
        }
    }

    private fun callGraphActivity(
        owner: String,
        repository: String,
        idOwner: Long,
        context: Context
    ) {
        val resultIntent = Intent(context, GraphActivity::class.java)
        resultIntent.putExtra("ownerName", owner)
        resultIntent.putExtra("repositoryName", repository)

            val resultPendingIntent = PendingIntent.getActivity(
            context, idOwner.toInt(), resultIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val builder = NotificationCompat.Builder(context)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(owner)
            .setContentText(repository)
            .setContentIntent(resultPendingIntent)
            .setAutoCancel(true)
        val notification = builder.build()
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(idOwner.toInt(), notification)
    }

}