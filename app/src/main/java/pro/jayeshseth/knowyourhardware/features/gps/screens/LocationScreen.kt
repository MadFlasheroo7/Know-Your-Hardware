package pro.jayeshseth.knowyourhardware.features.gps.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import pro.jayeshseth.knowyourhardware.R
import pro.jayeshseth.knowyourhardware.features.gps.additionalInfos.LocationDataInfo
import pro.jayeshseth.knowyourhardware.features.gps.broadcastReceiver.GpsManager
import pro.jayeshseth.knowyourhardware.features.gps.locationListeners.FusedLocationProviderUpdatesListener
import pro.jayeshseth.knowyourhardware.features.gps.locationListeners.LocationManagerUpdatesListener
import pro.jayeshseth.knowyourhardware.model.TogglerData
import pro.jayeshseth.knowyourhardware.ui.composables.AdditionalInfoCard
import pro.jayeshseth.knowyourhardware.ui.composables.InfoCardMapper
import pro.jayeshseth.knowyourhardware.ui.composables.LocationInfoCard
import pro.jayeshseth.knowyourhardware.ui.composables.ProviderButton
import pro.jayeshseth.knowyourhardware.ui.composables.Toggler
import pro.jayeshseth.knowyourhardware.ui.screens.PermissionScreen
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

@SuppressLint("MissingPermission")
@Composable
fun LocationScreen(
    gpsManager: GpsManager,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lastKnownLocation = remember { mutableStateOf(Location("")) }
    val liveLocation = remember { mutableStateOf(Location("")) }
    val trackLiveLocation = remember { mutableStateOf(false) }
    val showInMinutes = remember { mutableStateOf(false) }
    val showInSeconds = remember { mutableStateOf(false) }
    val useFusedGMSProvider = remember { mutableStateOf(true) }
    val isFusedProviderClientAvailable = remember { mutableStateOf(true) }
    val speedInKms = remember { mutableStateOf(false) }
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    val timeInterval = TimeUnit.SECONDS.toMillis(1)
    val locationRequestGSM =
        LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, timeInterval)
            .build()

    val selectedProvider = remember { mutableStateOf("FusedGMS") }

    val speed = when {
        speedInKms.value -> {
            "${liveLocation.value.speed * 3.6} Kph"
        }

        else -> {
            "${liveLocation.value.speed} m/s"
        }
    }

    val fusedLocationProviderClientAvailability =
        GoogleApiAvailability.getInstance().checkApiAvailability(fusedLocationClient)
    fusedLocationProviderClientAvailability.addOnSuccessListener {
        isFusedProviderClientAvailable.value = true
    }.addOnFailureListener {
        isFusedProviderClientAvailable.value = false
    }
    when {
        trackLiveLocation.value -> {
            if (useFusedGMSProvider.value) {
                FusedLocationProviderUpdatesListener(
                    fusedLocationClient,
                    locationRequestGSM
                ) { result ->
                    result.locations.map { location ->
                        location.let {
                            liveLocation.value = it
                        }
                    }
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    val locationRequest =
                        android.location.LocationRequest.Builder(timeInterval).build()
                    LocationManagerUpdatesListener(
                        provider = selectedProvider.value,
                        locationClient = locationManager,
                        locationRequest = locationRequest
                    ) {
                        liveLocation.value = it
                    }
                } else {
                    LocationManagerUpdatesListener(
                        provider = selectedProvider.value,
                        locationClient = locationManager
                    ) {
                        liveLocation.value = it
                    }
                }

            }
        }
    }

    val lastKnownLocationTimestamp = SimpleDateFormat(
        "yyyy-MM-dd HH:mm:ss",
        Locale.getDefault()
    ).format(Date(lastKnownLocation.value.time))
    val liveLocationTimestamp = SimpleDateFormat(
        "yyyy-MM-dd HH:mm:ss",
        Locale.getDefault()
    ).format(Date(liveLocation.value.time))

    val elapsedTimeInMinutes = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
            TimeUnit.MILLISECONDS.toMinutes(liveLocation.value.elapsedRealtimeMillis)
        }

        else -> {
            "Unavailable"
        }
    }
    val elapsedTimeInSeconds = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
            TimeUnit.MILLISECONDS.toSeconds(liveLocation.value.elapsedRealtimeMillis)
        }

        else -> {
            "Unavailable"
        }
    }
    val elapsedTimeMillis = when {
        showInMinutes.value -> {
            "$elapsedTimeInMinutes Min(s)"
        }

        showInSeconds.value -> {
            "$elapsedTimeInSeconds Sec(s)"
        }

        else -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                "${liveLocation.value.elapsedRealtimeMillis}"
            } else {
                "Unavailable"
            }
        }
    }
    val elapsedTimeNanos = when {
        showInMinutes.value -> {
            "$elapsedTimeInMinutes Min(s)"
        }

        showInSeconds.value -> {
            "$elapsedTimeInSeconds Sec(s)"
        }

        else -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                "${liveLocation.value.elapsedRealtimeNanos}"
            } else {
                "Unavailable"
            }
        }
    }
    val permissions = remember {
        mutableListOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
        )
    }

    val locationDataInfo = LocationDataInfo(
        isFromGms = useFusedGMSProvider.value && trackLiveLocation.value,
        liveLocation = liveLocation.value,
        lastKnownLocation = lastKnownLocation.value,
        liveLocationTimestamp = liveLocationTimestamp,
        lastKnownLocationTimestamp = lastKnownLocationTimestamp,
        elapsedTimeNanos = elapsedTimeNanos,
        elapsedTimeMillis = elapsedTimeMillis,
        speed = speed
    )

    val togglerList = mutableListOf(
        TogglerData(
            title = "Track Live Location",
            checked = trackLiveLocation.value,
            onCheckedChanged = { trackLiveLocation.value = it },
            icon = R.drawable.round_location_on,
            enabled = true
        ),
        TogglerData(
            title = "Elapsed time in seconds",
            checked = showInSeconds.value,
            onCheckedChanged = {
                showInSeconds.value = it
                showInMinutes.value = false
            },
            icon = R.drawable.round_access_time_filled_24,
            enabled = trackLiveLocation.value
        ),
        TogglerData(
            title = "Elapsed time in minutes",
            checked = showInMinutes.value,
            onCheckedChanged = {
                showInMinutes.value = it
                showInSeconds.value = false
            },
            icon = R.drawable.round_access_time_filled_24,
            enabled = trackLiveLocation.value
        ),
        TogglerData(
            title = "Speed in KPH",
            checked = speedInKms.value,
            onCheckedChanged = {
                speedInKms.value = it
            },
            icon = R.drawable.speed,
            enabled = trackLiveLocation.value
        )
    )

    LaunchedEffect(selectedProvider.value) {
        if (useFusedGMSProvider.value) {
            fusedLocationClient.lastLocation.addOnSuccessListener { loc ->
                if (loc != null) {
                    lastKnownLocation.value = loc
                }
            }
        } else {
            locationManager.getLastKnownLocation(selectedProvider.value)
                .let {
                    if (it != null) {
                        lastKnownLocation.value = it
                    }
                }
        }
    }

    if (!isLocationPermissionGranted(context)) {
        PermissionScreen(
            context = context,
            permissions = permissions,
            title = "Location Permission",
            rationale = "Precise location access is necessary for this feature",
            permanentlyDeclinedRationale = "Seems like the permission has been denied permanently, kindly grant it from system settings",
            icon = {
                Icon(
                    imageVector = Icons.Rounded.LocationOn,
                    contentDescription = "",
                    modifier = Modifier.size(100.dp)
                )
            }
        )
    } else {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = modifier
                .padding(horizontal = 16.dp)
        ) {
            item { Spacer(modifier = Modifier.statusBarsPadding()) }
            items(togglerList) {
                Toggler(
                    title = it.title,
                    checked = it.checked,
                    onCheckedChanged = it.onCheckedChanged,
                    icon = it.icon,
                    enabled = it.enabled
                )
            }
            item {
                ProviderButton(
                    text = "Fused (Google Play Services)",
                    onClick = {
                        selectedProvider.value = "FusedGMS"
                        useFusedGMSProvider.value = true
                    },
                    isSelected = selectedProvider.value == "FusedGMS"
                )
            }
            item {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Max)
                ) {
                    gpsManager.allProviders.value.forEach { provider ->
                        ProviderButton(
                            text = provider.replaceFirstChar { it.uppercase() },
                            isSelected = selectedProvider.value == provider,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxSize(),
                            onClick = {
                                useFusedGMSProvider.value = false
                                selectedProvider.value = provider
                            }
                        )
                    }
                }
            }
            items(locationDataInfo.locationInfoList) { info ->
                LocationInfoCard(
                    title = info.title,
                    latitude = info.latitude,
                    longitude = info.longitude,
                    time = info.time,
                    enabled = info.enabled,
                    additionalInfoContent = {
                        if (info.additionalInfo != null) {
                            AdditionalInfoCard(
                                primaryInfo = info.additionalInfo.primaryInfo,
                                secondaryInfo = info.additionalInfo.secondaryInfo,
                                source = info.additionalInfo.source
                            )
                        }
                    }
                )
            }
            items(locationDataInfo.locationDataInfoList) {
                InfoCardMapper(it)
            }
            item {
                Text(
                    text = "Location Capabilities",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            items(locationDataInfo.liveLocationCapabilities) {
                InfoCardMapper(it)
            }
            item { Spacer(modifier = Modifier.navigationBarsPadding()) }
        }
    }
}

fun isLocationPermissionGranted(context: Context): Boolean {
    return ActivityCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
}