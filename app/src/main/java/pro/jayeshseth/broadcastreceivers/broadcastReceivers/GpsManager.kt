package pro.jayeshseth.broadcastreceivers.broadcastReceivers

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.GnssAntennaInfo
import android.location.GnssCapabilities
import android.location.GnssSignalType
import android.location.LocationManager
import android.location.LocationRequest
import android.location.provider.ProviderProperties
import android.os.Build
import android.os.Build.VERSION_CODES.P
import android.os.CancellationSignal
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import java.util.concurrent.Executor
import java.util.function.Consumer

class GpsManager {
    val isGpsEnabled = mutableStateOf(false)
    val isLocationEnabled = mutableStateOf(false)
    val gnssHardwareModelName = mutableStateOf<String?>("")
    val allProviders = mutableStateOf(listOf<String>())
    val enabledProviders = mutableStateOf(listOf<String>())
    val gnssSignalType = mutableStateOf(listOf<GnssSignalType>())
    val gnssAntennaInfo = mutableStateOf(listOf<GnssAntennaInfo?>(null))
    val gnssCapabilities = mutableStateOf<GnssCapabilities?>(null)
    val gpsProviderProperties = mutableStateOf<ProviderProperties?>(null)

    private val gpsReceiver = object : BroadcastReceiver() {
        @SuppressLint("MissingPermission")
        @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
        override fun onReceive(context: Context?, intent: Intent?) {
            val action = intent?.action
            if (action == LocationManager.PROVIDERS_CHANGED_ACTION) {
                val locationManager =
                    context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                isGpsEnabled.value = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                isLocationEnabled.value = locationManager.isLocationEnabled
                gnssHardwareModelName.value = locationManager.gnssHardwareModelName
                allProviders.value = locationManager.allProviders
                enabledProviders.value = locationManager.getProviders(true)
                gnssSignalType.value = locationManager.gnssCapabilities.gnssSignalTypes
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    fun register(context: Context) {
        val filter = IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION)
        val initialGpsStatus = InitialGpsStatus(context)
        context.registerReceiver(gpsReceiver, filter)
        isGpsEnabled.value = initialGpsStatus.isGpsEnabled
        gnssHardwareModelName.value = initialGpsStatus.gnssHardwareModel
        allProviders.value = initialGpsStatus.allProviders
        isLocationEnabled.value = initialGpsStatus.isLocationEnabled
        enabledProviders.value = initialGpsStatus.enabledProvider
        gnssSignalType.value = initialGpsStatus.gnssSignalTypes
        gnssCapabilities.value = initialGpsStatus.gnssCapabilities
        gpsProviderProperties.value = initialGpsStatus.gpsProviderProperties
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

    @RequiresApi(P)
    val gnssHardwareModel = locationManager.gnssHardwareModelName

    @RequiresApi(P)
    val isLocationEnabled = locationManager.isLocationEnabled

    val enabledProvider = locationManager.getProviders(true)

//    @RequiresApi(S)
//    val gnssAntennaInfos = locationManager.gnssAntennaInfos //TODO

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    val gnssSignalTypes = locationManager.gnssCapabilities.gnssSignalTypes

    @RequiresApi(Build.VERSION_CODES.R)
    val gnssCapabilities = locationManager.gnssCapabilities

    @RequiresApi(Build.VERSION_CODES.S)
    val gpsProviderProperties = locationManager.getProviderProperties(LocationManager.GPS_PROVIDER)
}