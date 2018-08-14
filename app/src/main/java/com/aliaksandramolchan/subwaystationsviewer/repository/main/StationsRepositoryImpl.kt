package com.aliaksandramolchan.subwaystationsviewer.repository.main

import android.content.Context
import com.aliaksandramolchan.subwaystationsviewer.database.DBHelper
import com.aliaksandramolchan.subwaystationsviewer.model.Station
import com.aliaksandramolchan.subwaystationsviewer.repository.local.LocalDataRepository
import com.aliaksandramolchan.subwaystationsviewer.repository.local.LocalDataSource
import com.aliaksandramolchan.subwaystationsviewer.repository.remote.RemoteDataRepository
import com.aliaksandramolchan.subwaystationsviewer.repository.remote.RemoteDataSource
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers


class StationsRepositoryImpl(private val context: Context, private val isConnected: Boolean) : StationsRepository {

    private val remoteDataRepository: RemoteDataRepository = RemoteDataSource()
    private val localDataRepository: LocalDataRepository = LocalDataSource(DBHelper(context))

    override fun fetchStations(): Observable<List<Station>> {
        if (isConnected) {
            return fetchFromNetwork()
        } else
            return fetchFromLocal()
    }

    private fun fetchFromLocal(): Observable<List<Station>> {
        return localDataRepository.fetchFromLocalStorage()
                .doOnError { }
                .toObservable()
    }

    private fun fetchFromNetwork(): Observable<List<Station>> {
        return remoteDataRepository.fetchFromNetwork()
                .doOnNext {
                    storeInLocal(it)
                }
    }

    private fun storeInLocal(stations: List<Station>) {
        Observable.fromCallable {
            localDataRepository.fetchFromLocalStorage()
            localDataRepository.saveStations(stations)
        }
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe()

    }

}



