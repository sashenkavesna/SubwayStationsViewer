package com.aliaksandramolchan.subwaystationsviewer.network

import com.aliaksandramolchan.subwaystationsviewer.utils.BASE_URL
import com.aliaksandramolchan.subwaystationsviewer.model.Station
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET


interface StationsApi {
    @GET("/BeeWhy/metro/stations")
    fun getStations(): Observable<List<Station>>

    companion object {
        fun create(): StationsApi {

            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(
                            RxJava2CallAdapterFactory.create())
                    .addConverterFactory(
                            GsonConverterFactory.create())
                    .baseUrl(BASE_URL)
                    .build()
            return retrofit.create(StationsApi::class.java)
        }
    }
}