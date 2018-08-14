package com.aliaksandramolchan.subwaystationsviewer.viewmodel

import android.arch.lifecycle.ViewModel
import com.aliaksandramolchan.subwaystationsviewer.model.Station
import io.reactivex.Observable


class StationItemViewModel : ViewModel() {
    lateinit var station: Station

    fun bind(station: Station) {
        this.station = station
    }

    fun observeDistance(): Observable<Float> = Observable.just(station.distance)
}