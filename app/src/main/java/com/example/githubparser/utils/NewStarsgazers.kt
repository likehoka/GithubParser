package com.example.githubparser.utils

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import com.example.githubparser.Database.objectbox.ObjectBox
import com.example.githubparser.R
import com.example.githubparser.activities.GraphActivity
import com.example.githubparser.model.Stargazers
import io.objectbox.kotlin.boxFor

class NewStarsgazers {

    var notesStargazers = ObjectBox.boxStore.boxFor<Stargazers>()

    private fun getnoteObjectbox(): MutableList<Stargazers> {
        return notesStargazers.query().build().find()
    }

    fun sortDataToDatabase(
        map: MutableMap<Int, MyClassYear>,
        context: Context,
        idOwner: Long,
        statusService: Boolean = false//,
        //ownerName: String,
        //repositoryName: String
    ) {
        map.forEach() {
            val year = it.value
            it.value.monthMap.forEach {
                val month = it.value
                val likes = month.likes
                val owner = month.ownerName
                val repository = month.repositoryName
                val monthName = month.monthName
                val users = it.value.users
                Log.d(
                    "test",
                    "Owner: " + owner + " Year: " + it.value.year + " Month: " + monthName + " Likes: " + likes + " Users: " + it.value.users
                )

                val stargazer = Stargazers(
                    owner = owner, repository = repository, username = users, likes = likes,
                    month = monthName, year = it.value.year.toString(), stringDate = monthName + " " + it.value.year
                )
                var statusCompare: Boolean = false
                var stargazers: Stargazers? = null
                getnoteObjectbox().forEach {
                    if (it.owner == stargazer.owner && it.repository == stargazer.repository && it.stringDate == stargazer.stringDate) {
                        it.likes += likes
                        it.username += ", $users"
                        Log.d("test", "Условие выполнено")
                        stargazers = it
                        statusCompare = true
                        getnoteObjectbox().remove(it)
                    }
                }
                if (statusCompare){
                    stargazers?.let { it1 -> UsersGetAll().setStargazersObjectbox(it1) }
                    if (statusService) {



                        val resultIntent = Intent(context, GraphActivity::class.java)
                        resultIntent.putExtra("ownerName", stargazers!!.owner)
                        resultIntent.putExtra("repositoryName", stargazers!!.repository)

                        val resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT)

                        val builder = NotificationCompat.Builder(context)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle(stargazer.owner)
                            .setContentText(stargazer.repository)
                            .setContentIntent(resultPendingIntent)
                            //.setAutoCancel(true)
                        val notification = builder.build()
                        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                        notificationManager.notify(idOwner.toInt(), notification)
                    }
                }

                if (!statusCompare) {
                    UsersGetAll().setStargazersObjectbox(stargazer)
                }
            }
        }
    }

}