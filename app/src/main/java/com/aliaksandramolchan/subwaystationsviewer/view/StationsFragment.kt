package com.aliaksandramolchan.subwaystationsviewer.view

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import com.aliaksandramolchan.subwaystationsviewer.R
import com.aliaksandramolchan.subwaystationsviewer.utils.LOCATION_PERMISSION_REQUEST_CODE
import com.aliaksandramolchan.subwaystationsviewer.viewmodel.StationsViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.io.IOException


class StationsFragment : Fragment() {

    lateinit var viewModel: StationsViewModel

    private lateinit var progressBar: ProgressBar
    private lateinit var recyclerView: RecyclerView
    private lateinit var onNetworkChangedListener: OnNetworkChangedListener

    val stationsAdapter: StationsAdapter = StationsAdapter()

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private var disposable: Disposable? = null

    interface OnNetworkChangedListener {
        fun checkNetwork(): Boolean
    }

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

        if (isLocationPermissionGranted()) {
            requestPermissions(arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_PERMISSION_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (permissions.size > 0) {
                if (permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    subscribeToLocation()
                }
            }
        }
    }


    private fun subscribeToLocation() {
        disposable = viewModel.observeCurrentLocation()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    stationsAdapter.updateStations(it)
                }, {
                    if (it is SecurityException) {
                        Toast.makeText(this.context, R.string.permission_error, Toast.LENGTH_SHORT).show()
                    } else if (it is IOException) {
                        Toast.makeText(this.context, R.string.provider_error, Toast.LENGTH_SHORT).show()
                    }
                })
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
                .subscribe({ stations ->

                    if (stations.isEmpty())
                        throw IOException()

                    stationsAdapter.updateStations(stations.sortedBy { it.distance })

                }, {
                    Toast.makeText(this.context, R.string.networ_error, Toast.LENGTH_SHORT).show()
                }))


    }

    override fun onResume() {
        super.onResume()

        if (isLocationPermissionGranted()) {
            Toast.makeText(this.context, R.string.permission_error, Toast.LENGTH_SHORT).show()
        } else subscribeToLocation()

    }

    private fun isLocationPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(this.context!!, android.Manifest
                .permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
    }


    override fun onPause() {
        super.onPause()

        disposable?.dispose()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.clear()
    }

}


