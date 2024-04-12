package pro.jayeshseth.knowyourhardware.features.gps.locationListeners

import android.Manifest
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.location.LocationRequest
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

@RequiresApi(Build.VERSION_CODES.S)
@RequiresPermission(
    anyOf = [Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION],
)
@Composable
fun LocationManagerUpdatesListener(
    provider: String,
    locationClient: LocationManager,
    locationRequest: LocationRequest,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    onUpdate: (result: Location) -> Unit,
) {
    val context = LocalContext.current
    val currentOnUpdate by rememberUpdatedState(newValue = onUpdate)

    // Whenever on of these parameters changes, dispose and restart the effect.
    DisposableEffect(provider, locationRequest, lifecycleOwner) {
        val locationListener = LocationListener { location -> currentOnUpdate(location) }
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                locationClient.requestLocationUpdates(
                    provider,
                    locationRequest,
                    context.mainExecutor,
                    locationListener
                )
            } else if (event == Lifecycle.Event.ON_STOP) {
                locationClient.removeUpdates(locationListener)
            }
        }

        // Add the observer to the lifecycle
        lifecycleOwner.lifecycle.addObserver(observer)

        // When the effect leaves the Composition, remove the observer
        onDispose {
            locationClient.removeUpdates(locationListener)
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}

@RequiresPermission(
    anyOf = [Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION],
)
@Composable
fun LocationManagerUpdatesListener(
    provider: String,
    locationClient: LocationManager,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    onUpdate: (result: Location) -> Unit,
) {
    val currentOnUpdate by rememberUpdatedState(newValue = onUpdate)

    // Whenever on of these parameters changes, dispose and restart the effect.
    DisposableEffect(lifecycleOwner) {
        val locationListener = LocationListener { location -> currentOnUpdate(location) }
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                locationClient.requestLocationUpdates(
                    provider,
                    3_000,
                    1f,
                    locationListener
                )
            } else if (event == Lifecycle.Event.ON_STOP) {
                locationClient.removeUpdates(locationListener)
            }
        }

        // Add the observer to the lifecycle
        lifecycleOwner.lifecycle.addObserver(observer)

        // When the effect leaves the Composition, remove the observer
        onDispose {
            locationClient.removeUpdates(locationListener)
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}
