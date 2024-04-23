package pro.jayeshseth.knowyourhardware.features.gps

import android.annotation.SuppressLint
import android.location.Location
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

interface LocationRepository {
    fun getForegroundLocation(): Flow<Location>

    fun stopForegroundLocationUpdates(callback: LocationCallback)

}

class LocationRepositoryImpl(
    private val fusedLocationClient: FusedLocationProviderClient
) : LocationRepository {

    private val locationRequest =
        LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, TimeUnit.SECONDS.toMillis(1))
            .build()

    @SuppressLint("MissingPermission")
    override fun getForegroundLocation(): Flow<Location> {
        return callbackFlow {

            val foregroundLocationCallback: LocationCallback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    result.locations.map { location ->
                        location?.let {
                            launch {
                                send(it)
                            }
                        }
                    }
                }
            }
            fusedLocationClient.requestLocationUpdates(
                locationRequest, foregroundLocationCallback, Looper.getMainLooper(),
            )
            awaitClose {
                stopForegroundLocationUpdates(foregroundLocationCallback)
            }
        }
    }

    override fun stopForegroundLocationUpdates(callback: LocationCallback) {
        fusedLocationClient.removeLocationUpdates(callback)
    }
}