package com.example.teamprojectlogin

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.location.LocationManagerCompat.requestLocationUpdates

class LocationProvider(val context: Context) : LocationListener {

    private var location: Location? = null
    private var locationManager: LocationManager? = null

    init {
        getLocation()
    }

    private fun getLocation() {
        try {
            locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

            val isGPSEnabled = locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val isNetworkEnabled = locationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

            if (!isGPSEnabled && !isNetworkEnabled) {
                return
            } else {
                val hasFineLocationPermission = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
                val hasCoarseLocationPermission = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )

                if (hasFineLocationPermission != PackageManager.PERMISSION_GRANTED ||
                    hasCoarseLocationPermission != PackageManager.PERMISSION_GRANTED
                ) return

                if (isNetworkEnabled) {
                    locationManager?.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        1000L,
                        1f,
                        this
                    )
                    location =
                        locationManager?.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                }

                if (isGPSEnabled) {
                    locationManager?.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        1000L,
                        1f,
                        this
                    )
                    location =
                        locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onLocationChanged(location: Location) {
        this.location = location
    }

    fun getLocationLatitude(): Double {
        return location?.latitude ?: 0.0
    }

    fun getLocationLongitude(): Double {
        return location?.longitude ?: 0.0
    }

}