package com.example.githubparser.Database

import android.database.sqlite.SQLiteDatabase
import com.example.githubparser.Database.DatabaseRepository.OrmliteRepositories
import com.example.githubparser.Database.DatabaseStars.OrmliteStars
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper
import com.j256.ormlite.support.ConnectionSource
import com.j256.ormlite.table.TableUtils

object DatabaseHelper: OrmLiteSqliteOpenHelper(App.instance, "database.db", null, 1) {
    override fun onCreate(database: SQLiteDatabase?, connectionSource: ConnectionSource?) {
        TableUtils.createTableIfNotExists(connectionSource,
            OrmliteStars::class.java)
        TableUtils.createTableIfNotExists(connectionSource,
            OrmliteRepositories::class.java)
    }

    override fun onUpgrade(database: SQLiteDatabase?, connectionSource: ConnectionSource?, oldVersion: Int,
                           newVersion: Int) {
        TableUtils.dropTable<OrmliteStars, Any>(connectionSource, OrmliteStars::class.java, true)
        onCreate(database, connectionSource)
        TableUtils.dropTable<OrmliteRepositories, Any>(connectionSource, OrmliteRepositories::class.java, true)
        onCreate(database, connectionSource)
    }
}