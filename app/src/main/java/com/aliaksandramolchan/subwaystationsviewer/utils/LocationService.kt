package com.aliaksandramolchan.subwaystationsviewer.utils

import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import com.aliaksandramolchan.subwaystationsviewer.model.Station


class LocationService : LocationListener {
    var currentLocation: Location

    init {
        currentLocation = Location(EMPTY_PROVIDER)
        currentLocation.latitude = 0.0
        currentLocation.longitude = 0.0
    }

    companion object {
        fun getDistance(station: Station, fromLocation: Location): Float {
            if (!fromLocation.provider.equals(EMPTY_PROVIDER)) {
                val stationLocation = Location("station location")
                stationLocation.latitude = station.latitude
                stationLocation.longitude = station.longitude

                return fromLocation.distanceTo(stationLocation)
            }
            return 0.0f
        }
    }


    override fun onLocationChanged(location: Location?) {
        currentLocation = location!!

    }

    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
    }

    override fun onProviderEnabled(p0: String?) {
    }

    override fun onProviderDisabled(p0: String?) {
    }

}