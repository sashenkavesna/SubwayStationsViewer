package com.aliaksandramolchan.subwaystationsviewer.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.view.View
import android.widget.Toast
import com.aliaksandramolchan.subwaystationsviewer.R
import com.aliaksandramolchan.subwaystationsviewer.model.Station
import com.aliaksandramolchan.subwaystationsviewer.repository.main.StationsRepository
import com.aliaksandramolchan.subwaystationsviewer.repository.main.StationsRepositoryImpl
import com.aliaksandramolchan.subwaystationsviewer.utils.LocationService
import io.reactivex.Observable
import java.io.IOException


class StationsViewModel(application: Application, isConnected: Boolean) : AndroidViewModel(application) {

    var progressBar: Int = View.VISIBLE

    var repository: StationsRepository

    var locationService: LocationService
    var locationManager: LocationManager

    init {
        repository = StationsRepositoryImpl(application, isConnected)
        locationManager = application.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationService = LocationService()
    }

    fun observeProgressBar(): Observable<Int> = Observable.just(progressBar)

    fun observeStations(): Observable<List<Station>> = loadStations()

    fun observeCurrentLocation(): Observable<Location> = Observable.just(loadLocation())
            .doOnDispose { locationManager.removeUpdates(locationService) }

    private fun loadStations(): Observable<List<Station>> {
        val response = repository.fetchStations()
                .map { it.filter { s -> !s.name.equals("error") } }
                .doOnNext { stations ->
                    for (st in stations)
                        st.distance = LocationService.getDistance(st, locationService.currentLocation)

                }

        progressBar = View.GONE

        return response
    }


    private fun loadLocation(): Location {
        try {
            var provider: String

            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                provider = LocationManager.GPS_PROVIDER
            else if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
                provider = LocationManager.NETWORK_PROVIDER
            else throw IOException()

            locationManager.requestLocationUpdates(provider, 0, 1.0f, locationService)

            if (locationManager.getLastKnownLocation(provider) == null) {
                provider = LocationManager.PASSIVE_PROVIDER
                locationManager.requestLocationUpdates(provider, 0, 1.0f, locationService)
            }

            while (locationManager.getLastKnownLocation(provider) == null) {
            }
            locationService.currentLocation = locationManager.getLastKnownLocation(provider)

        } catch (e: SecurityException) {
            Toast.makeText(getApplication(), R.string.permission_error, Toast.LENGTH_SHORT).show()
            //  throw SecurityException()
        } catch (e: IOException) {
            //  throw IOException()
        }
        return locationService.currentLocation
    }
}
