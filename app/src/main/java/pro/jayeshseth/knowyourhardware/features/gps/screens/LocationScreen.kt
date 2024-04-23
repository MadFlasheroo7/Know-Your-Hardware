package pro.jayeshseth.knowyourhardware.features.gps.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import pro.jayeshseth.knowyourhardware.R
import pro.jayeshseth.knowyourhardware.features.gps.ForegroundLocationService
import pro.jayeshseth.knowyourhardware.features.gps.LocationScreenViewModel
import pro.jayeshseth.knowyourhardware.features.gps.additionalInfos.LocationDataInfo
import pro.jayeshseth.knowyourhardware.features.gps.locationListeners.FusedLocationProviderUpdatesListener
import pro.jayeshseth.knowyourhardware.features.gps.locationListeners.LocationManagerUpdatesListener
import pro.jayeshseth.knowyourhardware.model.TogglerData
import pro.jayeshseth.knowyourhardware.ui.composables.AdditionalInfoCard
import pro.jayeshseth.knowyourhardware.ui.composables.InfoCardMapper
import pro.jayeshseth.knowyourhardware.ui.composables.LocationInfoCard
import pro.jayeshseth.knowyourhardware.ui.composables.ProviderButton
import pro.jayeshseth.knowyourhardware.ui.composables.Toggler
import pro.jayeshseth.knowyourhardware.ui.screens.PermissionScreen
import pro.jayeshseth.knowyourhardware.utils.isAbove_S
import pro.jayeshseth.knowyourhardware.utils.isAbove_TIRAMISU
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

@SuppressLint("MissingPermission")
@Composable
fun LocationScreen(
    viewModel: LocationScreenViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    val locationRequestGSM =
        LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, viewModel.timeInterval.longValue)
            .build()
    val dateTimeFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val useFusedGMSProvider = remember { mutableStateOf(true) }
    val isFusedProviderClientAvailable = remember { mutableStateOf(true) }
    val permissions = remember {
        mutableListOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
//            Manifest.permission.ACCESS_BACKGROUND_LOCATION
        )
    }
    val foregroundLiveLocation by ForegroundLocationService.liveLocation.collectAsState()
    var isForegroundServiceRunning by remember {
        mutableStateOf(ForegroundLocationService.isServiceRunning.value)
    }


    val fusedLocationProviderClientAvailability =
        GoogleApiAvailability.getInstance().checkApiAvailability(fusedLocationClient)
    fusedLocationProviderClientAvailability.addOnSuccessListener {
        isFusedProviderClientAvailable.value = true
    }.addOnFailureListener {
        isFusedProviderClientAvailable.value = false
    }

    if (viewModel.trackLiveLocation.value) {
        if (useFusedGMSProvider.value) {
            FusedLocationProviderUpdatesListener(
                fusedLocationClient,
                locationRequestGSM
            ) { result ->
                result.locations.map { location ->
                    location.let {
                        viewModel.liveLocation.value = it
                    }
                }
            }
        } else {
            if (isAbove_S) {
                val locationRequest =
                    android.location.LocationRequest.Builder(viewModel.timeInterval.longValue)
                        .build()
                LocationManagerUpdatesListener(
                    provider = viewModel.selectedProvider.value,
                    locationClient = locationManager,
                    locationRequest = locationRequest
                ) {
                    viewModel.liveLocation.value = it
                }
            } else {
                LocationManagerUpdatesListener(
                    provider = viewModel.selectedProvider.value,
                    locationClient = locationManager
                ) {
                    viewModel.liveLocation.value = it
                }
            }

        }
    }

    val lastKnownLocationTimestamp =
        dateTimeFormat.format(Date(viewModel.lastKnownLocation.value.time))
    val liveLocationTimestamp = dateTimeFormat.format(Date(viewModel.liveLocation.value.time))

    val elapsedTimeInMinutes = remember(viewModel.liveLocation.value) {
        when {
            isAbove_TIRAMISU -> {
                TimeUnit.MILLISECONDS.toMinutes(viewModel.liveLocation.value.elapsedRealtimeMillis)
            }

            else -> {
                "Unavailable"
            }
        }
    }
    val elapsedTimeInSeconds = remember(viewModel.liveLocation.value) {
        when {
            isAbove_TIRAMISU -> {
                TimeUnit.MILLISECONDS.toSeconds(viewModel.liveLocation.value.elapsedRealtimeMillis)
            }

            else -> {
                "Unavailable"
            }
        }
    }

    val elapsedTimeMillis = when {
        viewModel.showInMinutes.value -> {
            "$elapsedTimeInMinutes Min(s)"
        }

        viewModel.showInSeconds.value -> {
            "$elapsedTimeInSeconds Sec(s)"
        }

        else -> {
            if (isAbove_TIRAMISU) {
                "${viewModel.liveLocation.value.elapsedRealtimeMillis}"
            } else {
                "Unavailable"
            }
        }
    }
    val elapsedTimeNanos = when {
        viewModel.showInMinutes.value -> {
            "$elapsedTimeInMinutes Min(s)"
        }

        viewModel.showInSeconds.value -> {
            "$elapsedTimeInSeconds Sec(s)"
        }

        else -> {
            if (isAbove_TIRAMISU) {
                "${viewModel.liveLocation.value.elapsedRealtimeNanos}"
            } else {
                "Unavailable"
            }
        }
    }
    val speed = when {
        viewModel.speedInKms.value -> {
            "${viewModel.liveLocation.value.speed * 3.6} Kph"
        }

        else -> {
            "${viewModel.liveLocation.value.speed} m/s"
        }
    }

    val togglerList = mutableListOf(
        TogglerData(
            title = "Track Live Location",
            checked = viewModel.trackLiveLocation.value,
            onCheckedChanged = { viewModel.trackLiveLocation.value = it },
            icon = R.drawable.round_location_on,
            enabled = true
        ),
        TogglerData(
            title = "Track Live Location (Foreground Service)",
            checked = isForegroundServiceRunning,
            onCheckedChanged = {
                isForegroundServiceRunning = it
            },
            icon = R.drawable.round_location_on,
            enabled = true
        ),
        TogglerData(
            title = "Track Live Location (Background Service)",
            checked = false,
            onCheckedChanged = {},
            icon = R.drawable.round_location_on,
            enabled = false
        ),
        TogglerData(
            title = "Elapsed time in seconds",
            checked = viewModel.showInSeconds.value,
            onCheckedChanged = {
                viewModel.showInSeconds.value = it
                viewModel.showInMinutes.value = false
            },
            icon = R.drawable.round_access_time_filled_24,
            enabled = viewModel.trackLiveLocation.value
        ),
        TogglerData(
            title = "Elapsed time in minutes",
            checked = viewModel.showInMinutes.value,
            onCheckedChanged = {
                viewModel.showInMinutes.value = it
                viewModel.showInSeconds.value = false
            },
            icon = R.drawable.round_access_time_filled_24,
            enabled = viewModel.trackLiveLocation.value
        ),
        TogglerData(
            title = "Speed in KPH",
            checked = viewModel.speedInKms.value,
            onCheckedChanged = {
                viewModel.speedInKms.value = it
            },
            icon = R.drawable.speed,
            enabled = viewModel.trackLiveLocation.value
        )
    )

    LaunchedEffect(viewModel.selectedProvider.value) {
        if (useFusedGMSProvider.value) {
            fusedLocationClient.lastLocation.addOnSuccessListener { loc ->
                if (loc != null) {
                    viewModel.lastKnownLocation.value = loc
                }
            }
        } else {
            locationManager.getLastKnownLocation(viewModel.selectedProvider.value)
                .let {
                    if (it != null) {
                        viewModel.lastKnownLocation.value = it
                    }
                }
        }
    }

    LaunchedEffect(isForegroundServiceRunning, foregroundLiveLocation) {
        val intent = Intent(context, ForegroundLocationService::class.java).apply {
            action = if (isForegroundServiceRunning) {
                ForegroundLocationService.START_TRACKING
            } else {
                ForegroundLocationService.STOP_TRACKING
            }
        }
        if (isForegroundServiceRunning) {
            viewModel.updateLiveLocation(foregroundLiveLocation)
            ContextCompat.startForegroundService(context, intent)
        } else {
            context.stopService(intent)
        }
    }

    val locationDataInfo = LocationDataInfo(
        isFromGms = useFusedGMSProvider.value && viewModel.trackLiveLocation.value,
        liveLocation = viewModel.liveLocation.value,
        lastKnownLocation = viewModel.lastKnownLocation.value,
        liveLocationTimestamp = liveLocationTimestamp,
        lastKnownLocationTimestamp = lastKnownLocationTimestamp,
        elapsedTimeNanos = elapsedTimeNanos,
        elapsedTimeMillis = elapsedTimeMillis,
        speed = speed
    )

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
                    icon = painterResource(id = it.icon),
                    enabled = it.enabled
                )
            }
            item {
                ProviderButton(
                    text = "Fused (Google Play Services)",
                    onClick = {
                        viewModel.selectedProvider.value = "FusedGMS"
                        useFusedGMSProvider.value = true
                    },
                    isSelected = viewModel.selectedProvider.value == "FusedGMS"
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
                    viewModel.allProviders.value.forEach { provider ->
                        ProviderButton(
                            text = provider.replaceFirstChar { it.uppercase() },
                            isSelected = viewModel.selectedProvider.value == provider,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxSize(),
                            onClick = {
                                useFusedGMSProvider.value = false
                                viewModel.selectedProvider.value = provider
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