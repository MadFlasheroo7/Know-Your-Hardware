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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import pro.jayeshseth.knowyourhardware.R
import pro.jayeshseth.knowyourhardware.locationListeners.FusedLocationUpdatesListener
import pro.jayeshseth.knowyourhardware.locationListeners.GpsLocationUpdatesListener
import pro.jayeshseth.knowyourhardware.ui.additionalInfos.locationInfo.BearingAccuracyInfo
import pro.jayeshseth.knowyourhardware.ui.additionalInfos.locationInfo.BearingInfo
import pro.jayeshseth.knowyourhardware.ui.additionalInfos.locationInfo.ElapsedRealtimeAgeMillisInfo
import pro.jayeshseth.knowyourhardware.ui.additionalInfos.locationInfo.ElapsedRealtimeMillisInfo
import pro.jayeshseth.knowyourhardware.ui.additionalInfos.locationInfo.ElapsedRealtimeNanosInfo
import pro.jayeshseth.knowyourhardware.ui.additionalInfos.locationInfo.ElapsedRealtimeUncertaintyNanoInfo
import pro.jayeshseth.knowyourhardware.ui.additionalInfos.locationInfo.HorizontalAccuracyInfo
import pro.jayeshseth.knowyourhardware.ui.additionalInfos.locationInfo.LastKnownLocationInfo
import pro.jayeshseth.knowyourhardware.ui.additionalInfos.locationInfo.VerticalAccuracyInfo
import pro.jayeshseth.knowyourhardware.ui.composables.DURATION
import pro.jayeshseth.knowyourhardware.ui.composables.InfoCard
import pro.jayeshseth.knowyourhardware.ui.composables.Toggler
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
    val trackLiveLocation = remember { mutableStateOf(false) }
    val showInMinutes = remember { mutableStateOf(false) }
    val showInSeconds = remember { mutableStateOf(false) }
//    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
//    val minimumTimeInterval = remember {
//        mutableStateOf(1)
//    }
    val locationRequestGSM =
        LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, TimeUnit.SECONDS.toMillis(1))
            .build()

    val locationRequest =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            android.location.LocationRequest.Builder(TimeUnit.SECONDS.toMillis(1)).build()
        } else {
            TODO("VERSION.SDK_INT < S")
        }

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
//        FusedLocationUpdatesListener(fusedLocationClient, locationRequestGSM) { result ->
//            result.locations.map { location ->
//                location.let {
//                    liveLocation.value = it
//                }
//            }
//        }

        GpsLocationUpdatesListener(locationRequest = locationRequest) {
            it.let {
                    liveLocation.value = it
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
//    Log.d("last loc", "${fusedLocationClient.lastLocation}")

//    fusedLocationClient.lastLocation.addOnSuccessListener { loc ->
//        loc.let {
//            lastKnownLocation.value = it
//        }
//    }
//
//    locationManager.requestLocationUpdates(
//        LocationManager.GPS_PROVIDER,
//        locationRequest,
//        context.mainExecutor,
//        locationListener
//    )
    val lastKnownLocationTimestamp = SimpleDateFormat(
        "yyyy-MM-dd HH:mm:ss",
        Locale.getDefault()
    ).format(Date(lastKnownLocation.value.time))
    val liveLocationTimestamp = SimpleDateFormat(
        "yyyy-MM-dd HH:mm:ss",
        Locale.getDefault()
    ).format(Date(liveLocation.value.time))


    val elapsedTimeInMinutes = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        TimeUnit.MILLISECONDS.toMinutes(liveLocation.value.elapsedRealtimeMillis)
    } else {
        "Unavailable"
    }
    val elapsedTimeInSeconds = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        TimeUnit.MILLISECONDS.toSeconds(liveLocation.value.elapsedRealtimeMillis)
    } else {
        "Unavailable"
    }
    val elapsedTimeMillis = if (showInMinutes.value) {
        "$elapsedTimeInMinutes Min(s)"
    } else if (showInSeconds.value) {
        "$elapsedTimeInSeconds Sec(s)"
    } else {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            "${liveLocation.value.elapsedRealtimeMillis}"
        } else {
            "Unavailable"
        }
    }
    val elapsedTimeNanos = if (showInMinutes.value) {
        "$elapsedTimeInMinutes Min(s)"
    } else if (showInSeconds.value) {
        "$elapsedTimeInSeconds Sec(s)"
    } else {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            "${liveLocation.value.elapsedRealtimeNanos}"
        } else {
            "Unavailable"
        }
    }
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
            .padding(horizontal = 16.dp)
            .verticalScroll(
                rememberScrollState()
            )
            .systemBarsPadding()
    ) {
        Toggler(
            title = "Track Live Location",
            checked = trackLiveLocation.value,
            onCheckedChanged = { trackLiveLocation.value = it },
            imageVector = Icons.Rounded.LocationOn
        )
        Toggler(
            title = "Elapsed time in seconds",
            checked = showInSeconds.value,
            onCheckedChanged = {
                showInSeconds.value = it
                showInMinutes.value = false
            },
            painter = painterResource(R.drawable.round_access_time_filled_24),
            enabled = trackLiveLocation.value
        )
        Toggler(
            title = "Elapsed time in minutes",
            checked = showInMinutes.value,
            onCheckedChanged = {
                showInMinutes.value = it
                showInSeconds.value = false
            },
            painter = painterResource(R.drawable.round_access_time_filled_24),
            enabled = trackLiveLocation.value
        )
        LocationInfoCard(
            title = "Last known location",
            latitude = "${lastKnownLocation.value.latitude}",
            longitude = "${lastKnownLocation.value.longitude}",
            time = lastKnownLocationTimestamp,
            enabled = true,
            additionalInfoContent = { LastKnownLocationInfo() }
        )
        LocationInfoCard(
            title = "Live location",
            latitude = "${liveLocation.value.latitude}",
            longitude = "${liveLocation.value.longitude}",
            time = liveLocationTimestamp
        )
        InfoCard(
            title = "Location Horizontal Accuracy",
            info = "${liveLocation.value.accuracy}",
            enabled = true,
            additionalInfoContent = { HorizontalAccuracyInfo() }
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            InfoCard(
                title = "Location Vertical Accuracy",
                info = "${liveLocation.value.verticalAccuracyMeters}",
                enabled = true,
                additionalInfoContent = { VerticalAccuracyInfo() }
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
            info = if (liveLocation.value.hasBearing()) "${liveLocation.value.bearing}" else "Unavailable",
            enabled = true,
            additionalInfoContent = { BearingInfo() }
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            InfoCard(
                title = "Location Bearing Accuracy in Degrees",
                info = if (liveLocation.value.hasBearingAccuracy()) "${liveLocation.value.bearingAccuracyDegrees}" else "Unavailable",
                enabled = true,
                additionalInfoContent = { BearingAccuracyInfo() }
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
                info = elapsedTimeMillis,
                enabled = true,
                additionalInfoContent = { ElapsedRealtimeMillisInfo() }
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
            info = elapsedTimeNanos,
            enabled = true,
            additionalInfoContent = { ElapsedRealtimeNanosInfo() }
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            InfoCard(
                title = "Location Elapsed Realtime uncertainty in Nanos",
                info = if (liveLocation.value.hasElapsedRealtimeUncertaintyNanos()) "${liveLocation.value.elapsedRealtimeUncertaintyNanos}" else "Unavailable",
                enabled = true,
                additionalInfoContent = { ElapsedRealtimeUncertaintyNanoInfo() }
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
                info = formatLocationAge(liveLocation.value.elapsedRealtimeAgeMillis),
                enabled = true,
                additionalInfoContent = { ElapsedRealtimeAgeMillisInfo() }
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
            info = if (liveLocation.value.hasAltitude()) "${liveLocation.value.altitude}" else "Unavailable"
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
            info = if (liveLocation.value.hasSpeed()) "${liveLocation.value.speed}" else "Unavailable"
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            InfoCard(
                title = "Speed Accuracy",
                info = if (liveLocation.value.hasSpeedAccuracy()) "${liveLocation.value.speedAccuracyMetersPerSecond}" else "Unavailable"
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

fun formatLocationAge(ageInMilliseconds: Long): String {
    val seconds = ageInMilliseconds / 1000
    val minutes = seconds / 60

    return if (minutes < 1) {
        "$ageInMilliseconds ms (very recent)"
    } else if (minutes < 60) {
        "$minutes minute(s) ago"
    } else {
        val hours = minutes / 60
        "$hours hour(s) ago"
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
                    .weight(1.5f)
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