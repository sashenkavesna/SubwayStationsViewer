package com.aliaksandramolchan.subwaystationsviewer.repository.remote

import com.aliaksandramolchan.subwaystationsviewer.model.Station
import com.aliaksandramolchan.subwaystationsviewer.network.StationsApi
import io.reactivex.Observable



class RemoteDataSource : RemoteDataRepository {

    lateinit var stationsApi: StationsApi

    override fun fetchFromNetwork(): Observable<List<Station>> {
        return StationsApi.create().getStations()
    }
}

