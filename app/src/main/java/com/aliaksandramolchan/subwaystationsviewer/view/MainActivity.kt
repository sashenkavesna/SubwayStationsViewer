package com.aliaksandramolchan.subwaystationsviewer.view

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.aliaksandramolchan.subwaystationsviewer.R

class MainActivity : AppCompatActivity(), StationsFragment.OnNetworkChangedListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .add(R.id.activityMain, StationsFragment())
                    .commit()
        }

    }
    override fun checkNetwork(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val info = cm.getActiveNetworkInfo()
        if (info != null && info.isConnected) {
            return true
        }
        return false
    }
}
