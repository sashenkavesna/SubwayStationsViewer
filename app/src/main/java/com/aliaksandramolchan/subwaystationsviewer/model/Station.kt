package com.aliaksandramolchan.subwaystationsviewer.model

data class Station(val name: String, val latitude: Double, val longitude: Double) {
    var distance: Float = 0.0f
}
