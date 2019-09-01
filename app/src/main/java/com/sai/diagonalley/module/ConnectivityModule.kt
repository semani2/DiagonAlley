package com.sai.diagonalley.module

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

/**
 * Module to provide helper methods to check the network availability
 */
class ConnectivityModule(private val context: Context) {

    /**
     * Utility function to check if network is available
     *
     * @return Boolean indicating whether network is available or not
     */
     fun isNetworkAvailable(): Boolean {
        val connectivityManager = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager?

        if (connectivityManager != null) {
            if (Build.VERSION.SDK_INT < 23) {
                val networkInfo = connectivityManager.activeNetworkInfo

                if (networkInfo != null) {
                    return networkInfo.isConnected
                            && (networkInfo.type == ConnectivityManager.TYPE_WIFI
                            || networkInfo.type == ConnectivityManager.TYPE_MOBILE)
                }
            } else {
                val activeNetwork = connectivityManager.activeNetwork

                if (activeNetwork != null) {
                    val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)

                    return networkCapabilities!!.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                            || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                }
            }
        }

        return false
    }

}
