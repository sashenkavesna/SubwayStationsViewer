package com.aliaksandramolchan.subwaystationsviewer.view

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import com.aliaksandramolchan.subwaystationsviewer.LOCATION_PERMISSION_REQUEST_CODE
import com.aliaksandramolchan.subwaystationsviewer.LocationService
import com.aliaksandramolchan.subwaystationsviewer.R
import com.aliaksandramolchan.subwaystationsviewer.viewmodel.StationsViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.io.IOException


class StationsFragment : Fragment() {

    private lateinit var viewModel: StationsViewModel
    private lateinit var progressBar: ProgressBar
    private val stationsAdapter: StationsAdapter = StationsAdapter()
    private val compositeDisposable = CompositeDisposable()
    private lateinit var recyclerView: RecyclerView

    private lateinit var locationManager: LocationManager
    private lateinit var locationService: LocationService
    private lateinit var onNetworkChangedListener: OnNetworkChangedListener

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            onNetworkChangedListener = context as OnNetworkChangedListener
        } catch (e: ClassCastException) {
            e.printStackTrace()
        }
        viewModel = StationsViewModel(this.activity!!.application,
                onNetworkChangedListener.checkNetwork())

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        locationManager = this.context!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationService = LocationService()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_stations, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.adapter = stationsAdapter
        progressBar = view.findViewById(R.id.progressBar)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeToProgressBar()
        subscribeToAdapterData()
    }

    private fun subscribeToProgressBar() {
        compositeDisposable.add(viewModel.observeProgressBar()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    progressBar.visibility = it
                })
    }

    private fun subscribeToAdapterData() {
        compositeDisposable.add(viewModel.observeStations()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .retry()
                .subscribe({ list ->
                    if (list.isEmpty())
                        throw IOException()
                    for (station in list)
                        station.distance = locationService.getDistance( station)
                    stationsAdapter.updateStations(list.sortedBy {
                        it.distance
                    })
                    //    loadlocation()

                }, {
                    it.printStackTrace()

                    Toast.makeText(this.context, R.string.empty_db, Toast.LENGTH_SHORT).show()
                }))
    }

    override fun onResume() {
        super.onResume()
        checkLocationPermissions()

    }

    override fun onPause() {
        super.onPause()
        locationManager.removeUpdates(locationService)
    }

    private fun loadlocation() {
        try {
            var provider: String

            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                provider = LocationManager.GPS_PROVIDER
            else if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
                provider = LocationManager.NETWORK_PROVIDER
            else throw IOException()

            locationManager.requestLocationUpdates(provider,
                    0, 1.0f, locationService)

            if (locationManager.getLastKnownLocation(provider) == null) {
                provider = LocationManager.PASSIVE_PROVIDER
                locationManager.requestLocationUpdates(provider,
                        0, 1.0f, locationService)
            }
            while (locationManager.getLastKnownLocation(provider) == null) {

            }
            locationService.currentLocation = locationManager.getLastKnownLocation(provider)

        } catch (e: SecurityException) {
         //   Toast.makeText(this.context, "Please grant location permissions ", Toast.LENGTH_SHORT).show()

            //   loadlocation()
        } catch (e: IOException) {
            Toast.makeText(this.context, R.string.error, Toast.LENGTH_SHORT).show()

        }

    }

    private fun checkLocationPermissions() {
        if (ContextCompat.checkSelfPermission(this.context!!, android.Manifest
                        .permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_PERMISSION_REQUEST_CODE)
            checkLocationPermissions()
        } else loadlocation()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                                            grantResults: IntArray) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.size == 0)

                Toast.makeText(this.context, "Please grant location permissions", Toast.LENGTH_SHORT).show()

        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.clear()
    }


    interface OnNetworkChangedListener {
        fun checkNetwork(): Boolean
    }
}


