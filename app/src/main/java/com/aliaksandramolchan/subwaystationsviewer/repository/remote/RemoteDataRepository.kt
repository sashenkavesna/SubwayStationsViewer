package com.aliaksandramolchan.subwaystationsviewer.repository.remote

import com.aliaksandramolchan.subwaystationsviewer.model.Station
import io.reactivex.Observable


interface RemoteDataRepository{
    fun fetchFromNetwork(): Observable<List<Station>>
}