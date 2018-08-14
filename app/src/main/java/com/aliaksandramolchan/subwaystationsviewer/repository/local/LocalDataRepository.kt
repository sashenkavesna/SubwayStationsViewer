package com.aliaksandramolchan.subwaystationsviewer.repository.local

import com.aliaksandramolchan.subwaystationsviewer.model.Station
import io.reactivex.Single


interface LocalDataRepository {
    fun fetchFromLocalStorage(): Single<List<Station>>
    fun saveStations(stations: List<Station>)
}