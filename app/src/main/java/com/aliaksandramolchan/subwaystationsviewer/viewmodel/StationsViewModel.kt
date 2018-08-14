package com.aliaksandramolchan.subwaystationsviewer.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.content.Context
import android.location.LocationManager
import android.view.View
import com.aliaksandramolchan.subwaystationsviewer.model.Station
import com.aliaksandramolchan.subwaystationsviewer.repository.main.StationsRepository
import com.aliaksandramolchan.subwaystationsviewer.repository.main.StationsRepositoryImpl
import com.aliaksandramolchan.subwaystationsviewer.utils.LocationService
import io.reactivex.Observable


class StationsViewModel(application: Application, isConnected: Boolean) : AndroidViewModel(application) {

    var progressBar: Int = View.VISIBLE

    var locationManager: LocationManager
    var locationService: LocationService
    var repository: StationsRepository

    init {
        locationManager = application.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationService = LocationService()
        repository = StationsRepositoryImpl(application, isConnected)
    }

    fun observeProgressBar(): Observable<Int> = Observable.just(progressBar)

    fun observeStations(): Observable<List<Station>> = loadStations()

    private fun loadStations(): Observable<List<Station>> {
        val response = repository.fetchStations()
                .map {
                    it.filter { s ->
                        !s.name.equals("error")
                    }
                }

        progressBar = View.GONE
        return response
    }
}
