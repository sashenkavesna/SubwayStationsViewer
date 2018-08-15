package com.aliaksandramolchan.subwaystationsviewer.view

import android.location.Location
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.aliaksandramolchan.subwaystationsviewer.R
import com.aliaksandramolchan.subwaystationsviewer.model.Station
import com.aliaksandramolchan.subwaystationsviewer.utils.LocationService
import com.aliaksandramolchan.subwaystationsviewer.viewmodel.StationItemViewModel


class StationsAdapter() : RecyclerView.Adapter<StationsAdapter.ViewHolder>() {
    private var stations: List<Station> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_station, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return stations.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.bind(stations[p1])
    }

    fun updateStations(stations: List<Station>) {
        this.stations = stations
        notifyDataSetChanged()
    }

    fun updateStations(location: Location) {
        if (!stations.isEmpty()) {

            val newList: MutableList<Station> = ArrayList()
            newList.addAll(stations)

            for (station in newList) {
                station.distance = LocationService.getDistance(station, location)
            }

            newList.sortBy { it.distance }

            stations = newList.toList()
            notifyDataSetChanged()
        }
    }

    class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        var viewModel: StationItemViewModel? = null
        val nameView = view.findViewById<TextView>(R.id.stationName)

        fun bind(station: Station) {
            if (viewModel == null) {
                viewModel = StationItemViewModel()
                view.setOnClickListener {
                    viewModel!!.observeDistance().subscribe { d ->
                        if (d == 0.0f) {
                            Toast.makeText(view.context,
                                    R.string.permission_error, Toast.LENGTH_SHORT)
                                    .show()
                        } else Toast.makeText(view.context,
                                "Distance is " + d.toString() + " m", Toast.LENGTH_SHORT)
                                .show()
                    }
                }
            }
            viewModel!!.bind(station)
            nameView.text = station.name
        }
    }
}