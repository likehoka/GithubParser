package com.example.githubparser.Database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.githubparser.Database.DatabaseRepository.OrmliteRepositories
import com.example.githubparser.Database.DatabaseRepository.OrmliteRepositoriesDao
import com.example.githubparser.Database.DatabaseStars.OrmliteStars
import com.example.githubparser.Database.DatabaseStars.OrmliteStarsDao
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper
import com.j256.ormlite.support.ConnectionSource
import com.j256.ormlite.table.TableUtils
import java.sql.SQLException

class OrmliteHelper(
    context: Context
) : OrmLiteSqliteOpenHelper(context, "ormlite_database", null, 1) {

    val starsDao = OrmliteStarsDao(connectionSource)
    val repositoriesDao = OrmliteRepositoriesDao(connectionSource)
    override fun onCreate(database: SQLiteDatabase?, connectionSource: ConnectionSource?) {

        try {
            TableUtils.createTable(connectionSource, OrmliteStars::class.java)
            TableUtils.createTable(connectionSource, OrmliteRepositories::class.java)
        } catch (exc: SQLException) {
            exc.printStackTrace()
        }
    }

    override fun onUpgrade(
        database: SQLiteDatabase?,
        connectionSource: ConnectionSource?,
        oldVersion: Int,
        newVersion: Int
    ) {

        try {
            TableUtils.dropTable<OrmliteStars, Long>(connectionSource,
                OrmliteStars::class.java, true)
            TableUtils.dropTable<OrmliteRepositories, Long>(connectionSource,
                OrmliteRepositories::class.java, true)
            onCreate(database, connectionSource)
        } catch (exc: SQLException) {
            exc.printStackTrace()
        }
    }
}