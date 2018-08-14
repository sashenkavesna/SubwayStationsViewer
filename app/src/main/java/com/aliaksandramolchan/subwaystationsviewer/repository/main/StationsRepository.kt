package com.aliaksandramolchan.subwaystationsviewer.repository.main

import com.aliaksandramolchan.subwaystationsviewer.model.Station
import io.reactivex.Observable


interface StationsRepository {
    fun fetchStations(): Observable<List<Station>>
}