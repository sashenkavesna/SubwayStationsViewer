package com.aliaksandramolchan.subwaystationsviewer.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns


class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        val DATABASE_VERSION = 1
        val DATABASE_NAME = "Stations.db"

        private const val SQL_CREATE_ENTRIES =
                "CREATE TABLE ${DBContract.StationEntry.TABLE_NAME} (" +
                        "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                        "${DBContract.StationEntry.COLUMN_NAME} TEXT," +
                        "${DBContract.StationEntry.COLUMN_LONGITUDE} REAL," +
                        "${DBContract.StationEntry.COLUMN_DISTANCE} REAL," +
                        "${DBContract.StationEntry.COLUMN_LATITUDE} REAL)"

        private val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " +
                DBContract.StationEntry.TABLE_NAME
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

}

