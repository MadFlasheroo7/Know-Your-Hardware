package pro.jayeshseth.knowyourhardware.features.gps.broadcastReceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.GnssAntennaInfo
import android.location.GnssCapabilities
import android.location.LocationManager
import android.location.provider.ProviderProperties
import android.os.Build
import android.os.Build.VERSION_CODES.P
import android.os.Build.VERSION_CODES.S
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf

class GpsManager {
    val isGpsEnabled = mutableStateOf(false)

    @RequiresApi(P)
    val isLocationEnabled = mutableStateOf(false)

    @RequiresApi(P)
    val gnssHardwareModelName = mutableStateOf<String?>("")
    val gnssHardwareYear = mutableStateOf<Int?>(0)
    val allProviders = mutableStateOf(listOf<String>())
    val enabledProviders = mutableStateOf(listOf<String>())

    @RequiresApi(S)
    var gnssAntennaInfo: MutableList<GnssAntennaInfo>? = null
    val gnssCapabilities = mutableStateOf<GnssCapabilities?>(null)

    @RequiresApi(S)
    val gpsProviderProperties = mutableStateOf<ProviderProperties?>(null)

    private val gpsReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val action = intent?.action
            if (action == LocationManager.PROVIDERS_CHANGED_ACTION) {
                val locationManager =
                    context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                isGpsEnabled.value = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                @RequiresApi(P)
                isLocationEnabled.value = locationManager.isLocationEnabled
                @RequiresApi(P)
                gnssHardwareModelName.value = locationManager.gnssHardwareModelName
                allProviders.value = locationManager.allProviders
                enabledProviders.value = locationManager.getProviders(true)
                @RequiresApi(Build.VERSION_CODES.R)
                gnssCapabilities.value = locationManager.gnssCapabilities
                @RequiresApi(S)
                gpsProviderProperties.value =
                    locationManager.getProviderProperties(LocationManager.FUSED_PROVIDER)
                @RequiresApi(S)
                gnssAntennaInfo = locationManager.gnssAntennaInfos
            }
        }
    }

    fun register(context: Context) {
        val filter = IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION)
        val initialGpsStatus = InitialGpsStatus(context)
        context.registerReceiver(gpsReceiver, filter)
        isGpsEnabled.value = initialGpsStatus.isGpsEnabled
        @RequiresApi(P)
        gnssHardwareModelName.value = initialGpsStatus.gnssHardwareModel
        allProviders.value = initialGpsStatus.allProviders
        @RequiresApi(P)
        isLocationEnabled.value = initialGpsStatus.isLocationEnabled == true
        enabledProviders.value = initialGpsStatus.enabledProvider
        @RequiresApi(Build.VERSION_CODES.R)
        gnssCapabilities.value = initialGpsStatus.gnssCapabilities
        @RequiresApi(S)
        gpsProviderProperties.value = initialGpsStatus.gpsProviderProperties
        @RequiresApi(S)
        gnssAntennaInfo = initialGpsStatus.gnssAntennaInfos
        @RequiresApi(P)
        gnssHardwareYear.value = initialGpsStatus.gnssHardwareYear
    }

    fun unregister(context: Context) {
        context.unregisterReceiver(gpsReceiver)
    }
}

private class InitialGpsStatus(context: Context) {
    private val locationManager =
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    val allProviders = locationManager.allProviders

    val gnssHardwareModel = if (Build.VERSION.SDK_INT >= P) {
        locationManager.gnssHardwareModelName
    } else {
        null
    }

    val gnssHardwareYear = if (Build.VERSION.SDK_INT >= P) {
        locationManager.gnssYearOfHardware
    } else {
        null
    }

    val isLocationEnabled = if (Build.VERSION.SDK_INT >= P) {
        locationManager.isLocationEnabled
    } else {
        null
    }

    val enabledProvider: MutableList<String> = locationManager.getProviders(true)

    val gnssAntennaInfos: MutableList<GnssAntennaInfo>? = if (Build.VERSION.SDK_INT >= S) {
        locationManager.gnssAntennaInfos
    } else {
        null
    }

    val gnssCapabilities = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        locationManager.gnssCapabilities
    } else {
        null
    }

    val gpsProviderProperties = if (Build.VERSION.SDK_INT >= S) {
        locationManager.getProviderProperties(LocationManager.FUSED_PROVIDER)
    } else {
        null
    }
}