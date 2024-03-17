package pro.jayeshseth.knowyourhardware.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import pro.jayeshseth.knowyourhardware.locationListeners.FusedLocationUpdatesListener
import pro.jayeshseth.knowyourhardware.ui.composables.DURATION
import pro.jayeshseth.knowyourhardware.ui.composables.InfoCard
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

@Composable
fun LocationScreen(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lastKnownLocation = remember { mutableStateOf(Location("")) }
    val liveLocation = remember { mutableStateOf(Location("")) }
    val trackLiveLocation = remember {
        mutableStateOf(false)
    }
//    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
//    val minimumTimeInterval = remember {
//        mutableStateOf(1)
//    }
    val locationRequestGSM =
        LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, TimeUnit.SECONDS.toMillis(1))
            .build()

//    val locationRequest =
//        android.location.LocationRequest.Builder(TimeUnit.SECONDS.toMillis(1)).build()

//    val locationCallback: LocationCallback = object : LocationCallback() {
//        override fun onLocationResult(result: LocationResult) {
//            Log.d("location", "locations: ${result.locations[0]}")
//            Log.d("location", "last location: ${result.lastLocation}")
//        }
//    }

    if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        // TODO: Consider calling
        //    ActivityCompat#requestPermissions
        // here to request the missing permissions, and then overriding
        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
        //                                          int[] grantResults)
        // to handle the case where the user grants the permission. See the documentation
        // for ActivityCompat#requestPermissions for more details.
        return
    }

    if (trackLiveLocation.value) {
        FusedLocationUpdatesListener(fusedLocationClient, locationRequestGSM) { result ->
            result.locations.map { location ->
                location.let {
                    liveLocation.value = it
                }
            }
        }
    }
//    Log.d("location", "${currentLocation.value.provider}")

//    val locationListener = object : LocationListener {
//        override fun onLocationChanged(p0: Location) {
//            Log.d("location...", "${p0.provider}")
//        }
//
//    }
//
//    fusedLocationClient.requestLocationUpdates(
//        locationRequestGSM,
//        locationCallback,
//        Looper.getMainLooper()
//    )
    fusedLocationClient.lastLocation.addOnSuccessListener {
        lastKnownLocation.value = it
    }
//
//    locationManager.requestLocationUpdates(
//        LocationManager.GPS_PROVIDER,
//        locationRequest,
//        context.mainExecutor,
//        locationListener
//    )
    val timestamp = SimpleDateFormat(
        "yyyy-MM-dd HH:mm:ss",
        Locale.getDefault()
    ).format(Date(lastKnownLocation.value.time))
    val liveLocationTimestamp = SimpleDateFormat(
        "yyyy-MM-dd HH:mm:ss",
        Locale.getDefault()
    ).format(Date(liveLocation.value.time))
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
            .padding(horizontal = 16.dp)
            .verticalScroll(
                rememberScrollState()
            )
            .systemBarsPadding()
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Track Live Location")
            Spacer(modifier = Modifier.padding(8.dp))
            Switch(
                checked = trackLiveLocation.value,
                onCheckedChange = {trackLiveLocation.value = it},
                thumbContent = {
                    Icon(imageVector = Icons.Rounded.LocationOn, contentDescription = "", modifier = Modifier.size(SwitchDefaults.IconSize))
                }
            )
        }

        LocationInfoCard(
            title = "Last known location",
            latitude = "${lastKnownLocation.value.latitude}",
            longitude = "${lastKnownLocation.value.longitude}",
            time = timestamp
        )
        LocationInfoCard(
            title = "Live location",
            latitude = "${liveLocation.value.latitude}",
            longitude = "${liveLocation.value.longitude}",
            time = liveLocationTimestamp
        )
        InfoCard(
            title = "Location Horizontal Accuracy",
            info = "${liveLocation.value.accuracy}"
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            InfoCard(
                title = "Location Vertical Accuracy",
                info = "${liveLocation.value.verticalAccuracyMeters}"
            )
        } else {
            Text(
                text = "Location Vertical Accuracy Unavailable for API 26 and below",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
        InfoCard(
            title = "Location Bearing",
            info = "${liveLocation.value.bearing}"
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            InfoCard(
                title = "Location Bearing Accuracy in Degrees",
                info = "${liveLocation.value.bearingAccuracyDegrees}"
            )
        } else {
            Text(
                text = "Location Bearing Accuracy Unavailable for API 26 and below",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            InfoCard(
                title = "Location Elapsed Realtime in Millis",
                info = "${liveLocation.value.elapsedRealtimeMillis}"
            )
        } else {
            Text(
                text = "Location Elapsed time in Millis Information Unavailable for API 33 and below",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
        InfoCard(
            title = "Location Elapsed Realtime in Nanos",
            info = "${liveLocation.value.elapsedRealtimeNanos}"
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            InfoCard(
                title = "Location Elapsed Realtime uncertainty in Nanos",
                info = "${liveLocation.value.elapsedRealtimeUncertaintyNanos}"
            )
        } else {
            Text(
                text = "Location Elapsed Time Uncertainty in Nanos Information Unavailable for API 29 and below",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            InfoCard(
                title = "Location Elapsed Realtime Age in Millis",
                info = "${liveLocation.value.elapsedRealtimeAgeMillis}"
            )
        } else {
            Text(
                text = "Location Elapsed Realtime Age in Millis Information Unavailable for API 33 and below",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
        InfoCard(
            title = "Altitude",
            info = "${liveLocation.value.altitude}"
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            InfoCard(
                title = "MSL Altitude",
                info = if (liveLocation.value.hasMslAltitude()) "${liveLocation.value.mslAltitudeMeters}" else "Unavailable"
            )
            InfoCard(
                title = "MSL Altitude Accuracy",
                info = if (liveLocation.value.hasMslAltitudeAccuracy()) "${liveLocation.value.mslAltitudeAccuracyMeters}" else "Unavailable"
            )
        } else {
            Text(
                text = "MSL Altitude Information Unavailable for API 34 and below",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }


        InfoCard(
            title = "Current Location Provider",
            info = "${liveLocation.value.provider}"
        )
        InfoCard(
            title = "Speed",
            info = "${liveLocation.value.speed}"
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            InfoCard(
                title = "Speed Accuracy",
                info = "${liveLocation.value.speedAccuracyMetersPerSecond}"
            )
        } else {
            Text(
                text = "Speed Accuracy Information Unavailable for API 26 and below",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
        InfoCard(
            title = "Location Extras",
            info = "${liveLocation.value.extras}"
        )
//        InfoCard(
//            title = "is moving",
//            info = ""
//        )
//        InfoCard(
//            title = "Moving Location accuracy",
//            info = ""
//        )
//        InfoCard(
//            title = "facing",
//            info = ""
//        )
        Text(
            text = "Location Capabilities",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        InfoCard(
            title = "Has Horizontal Location Accuracy",
            info = "${liveLocation.value.hasAccuracy()}"
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            InfoCard(
                title = "Has Vertical Accuracy",
                info = "${liveLocation.value.hasVerticalAccuracy()}"
            )
        } else {
            Text(
                text = "Vertical Accuracy Information Unavailable for API 26 and below",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
        InfoCard(
            title = "Has Altitude",
            info = "${liveLocation.value.hasAltitude()}"
        )
        InfoCard(
            title = "Has Bearing",
            info = "${liveLocation.value.hasBearing()}"
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            InfoCard(
                title = "Has Bearing Accuracy",
                info = "${liveLocation.value.hasBearingAccuracy()}"
            )
        } else {
            Text(
                text = "Bearing Accuracy Information Unavailable for API 26 and below",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            InfoCard(
                title = "Has Elapsed Realtime Uncertainty in Nanos",
                info = "${liveLocation.value.hasElapsedRealtimeUncertaintyNanos()}"
            )
        } else {
            Text(
                text = "Location Elapsed Time Uncertainty in Nanos Information Unavailable for API 29 and below",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            InfoCard(
                title = "Has MSL Altitude",
                info = "${liveLocation.value.hasMslAltitude()}"
            )
            InfoCard(
                title = "Has MSL Altitude Accuracy",
                info = "${liveLocation.value.hasMslAltitudeAccuracy()}"
            )
        } else {
            Text(
                text = "MSL Altitude Information Unavailable for API 34 and below",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
        InfoCard(
            title = "Has Speed",
            info = "${liveLocation.value.hasSpeed()}"
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            InfoCard(
                title = "Has Speed Accuracy",
                info = "${liveLocation.value.hasSpeedAccuracy()}"
            )
        } else {
            Text(
                text = "Speed Accuracy Information Unavailable for API 26 and below",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}


@Composable
fun LocationInfoCard(
    title: String,
    latitude: String,
    longitude: String,
    modifier: Modifier = Modifier,
    time: String? = null,
    enabled: Boolean = false,
    additionalInfoContent: @Composable (ColumnScope.() -> Unit) = {}
) {
    val isVisible = remember { mutableStateOf(false) }
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .fillMaxWidth()
            .clickable(
                enabled = enabled,
                onClick = { isVisible.value = !isVisible.value }
            )
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "$title :", fontWeight = FontWeight.Bold, modifier = Modifier.weight(0.5f))
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Text(text = "Latitude - $latitude")
                Text(text = "Longitude - $longitude")
                if (time != null) {
                    Text(text = "Time - $time")
                }
            }
        }
        AnimatedVisibility(
            visible = isVisible.value,
            enter = expandVertically(tween(DURATION, easing = FastOutSlowInEasing)),
            exit = shrinkVertically(
                animationSpec = tween(
                    DURATION,
                    easing = FastOutSlowInEasing
                )
            ) + fadeOut()
        ) {
            additionalInfoContent()
        }
    }
}