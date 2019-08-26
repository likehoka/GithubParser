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

    private var notesStargazers = ObjectBox.boxStore.boxFor<Stargazers>()

    private fun getnoteObjectbox(): MutableList<Stargazers> {
        return notesStargazers.query().build().find()
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
                var listStringDate = UsersGetAll().getStargazersList(stargazer.idRepository, stargazer.stringDate)
                if (listStringDate!!.contains(stargazer.stringDate)) {
                    getnoteObjectbox().forEach {
                        if (it.idRepository == idOwner && it.stringDate == stargazer.stringDate) {
                            it.likes += yearValue.value.likes
                            it.username += ", " + yearValue.value.user
                            stargazers = it
                            getnoteObjectbox().remove(it)
                            stargazers?.let { it1 -> UsersGetAll().setStargazers(it1) }
                            if (statusService) {
                                callGraphActivity(ownerName, repositoryName, idOwner, context)
                            }
                        }
                    }
                } else {
                    if (statusService) {
                        callGraphActivity(ownerName, repositoryName, idOwner, context)
                    } else  {
                        UsersGetAll().setStargazers(stargazer)
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