package com.aliaksandramolchan.subwaystationsviewer

import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import com.aliaksandramolchan.subwaystationsviewer.model.Station


class LocationService() : LocationListener {


   /* companion object {
        lateinit var currentLocation: Location

        fun getDistance(currentLocation: Location, station: Station): Float {
            val stationLocation = Location("station location")
            stationLocation.latitude = station.latitude
            stationLocation.longitude = station.longitude
            return currentLocation.distanceTo(stationLocation)
        }
    }*/

    lateinit var currentLocation: Location

    fun getDistance(station: Station): Float {
        val stationLocation = Location("station location")
        stationLocation.latitude = station.latitude
        stationLocation.longitude = station.longitude
        return currentLocation.distanceTo(stationLocation)
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