package com.aliaksandramolchan.subwaystationsviewer.repository.local

import android.content.ContentValues
import android.provider.BaseColumns
import com.aliaksandramolchan.subwaystationsviewer.database.DBContract
import com.aliaksandramolchan.subwaystationsviewer.database.DBHelper
import com.aliaksandramolchan.subwaystationsviewer.model.Station
import io.reactivex.Single


class LocalDataSource(private val dbHelper: DBHelper) : LocalDataRepository {

    override fun saveStations(stations: List<Station>) {
        if (selectStations().isEmpty())
            insertStations(stations)
        else updateStations(stations)
    }

    override fun fetchFromLocalStorage(): Single<List<Station>> {
        val station = selectStations()
        return Single.just(station)
    }

    private fun insertStations(stations: List<Station>) {
        val db = dbHelper.writableDatabase
        for (station in stations) {
            val values = ContentValues().apply {
                put(DBContract.StationEntry.COLUMN_NAME, station.name)
                put(DBContract.StationEntry.COLUMN_LONGITUDE, station.longitude)
                put(DBContract.StationEntry.COLUMN_LATITUDE, station.latitude)
            }
            db.insert(DBContract.StationEntry.TABLE_NAME, null, values)

        }
        db.close()
    }

    private fun updateStations(stations: List<Station>) {
        val db = dbHelper.writableDatabase

        stations.forEachIndexed { index, station ->

            val values = ContentValues().apply {
                put(DBContract.StationEntry.COLUMN_NAME, station.name)
                put(DBContract.StationEntry.COLUMN_LONGITUDE, station.longitude)
                put(DBContract.StationEntry.COLUMN_LATITUDE, station.latitude)
            }

            val selection = "${BaseColumns._ID} LIKE ?"
            val args = arrayOf(index.toString())

            db.update(DBContract.StationEntry.TABLE_NAME, values, selection, args)
        }
        db.close()
    }

    private fun selectStations(): List<Station> {
        val db = dbHelper.readableDatabase

        val cursor = db.query(
                DBContract.StationEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        )

        val stations = mutableListOf<Station>()

        with(cursor) {
            while (this.moveToNext()) {
                val name = getString(getColumnIndexOrThrow(DBContract.StationEntry.COLUMN_NAME))
                val longitude = getDouble(getColumnIndexOrThrow(DBContract.StationEntry.COLUMN_LONGITUDE))
                val latitude = getDouble(getColumnIndexOrThrow(DBContract.StationEntry.COLUMN_LATITUDE))
                stations.add(Station(name, latitude, longitude))
            }
        }
        cursor.close()
        db.close()
        return stations
    }

}

