package com.aliaksandramolchan.subwaystationsviewer.database

import android.provider.BaseColumns


object DBContract {
    object StationEntry : BaseColumns {
        const val TABLE_NAME = "stations"
        const val COLUMN_NAME = "name"
        const val COLUMN_LATITUDE = "latitude"
        const val COLUMN_LONGITUDE = "longitude"
    }
}