package pro.jayeshseth.knowyourhardware.ui.screens

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import pro.jayeshseth.knowyourhardware.R
import pro.jayeshseth.knowyourhardware.broadcastReceivers.GpsManager
import pro.jayeshseth.knowyourhardware.locationListeners.FusedLocationProviderUpdatesListener
import pro.jayeshseth.knowyourhardware.locationListeners.LocationManagerUpdatesListener
import pro.jayeshseth.knowyourhardware.ui.additionalInfos.locationInfo.AltitudeInfo
import pro.jayeshseth.knowyourhardware.ui.additionalInfos.locationInfo.BearingAccuracyInfo
import pro.jayeshseth.knowyourhardware.ui.additionalInfos.locationInfo.BearingInfo
import pro.jayeshseth.knowyourhardware.ui.additionalInfos.locationInfo.CurrentLocationProviderInfo
import pro.jayeshseth.knowyourhardware.ui.additionalInfos.locationInfo.ElapsedRealtimeAgeMillisInfo
import pro.jayeshseth.knowyourhardware.ui.additionalInfos.locationInfo.ElapsedRealtimeMillisInfo
import pro.jayeshseth.knowyourhardware.ui.additionalInfos.locationInfo.ElapsedRealtimeNanosInfo
import pro.jayeshseth.knowyourhardware.ui.additionalInfos.locationInfo.ElapsedRealtimeUncertaintyNanoInfo
import pro.jayeshseth.knowyourhardware.ui.additionalInfos.locationInfo.ExtrasInfo
import pro.jayeshseth.knowyourhardware.ui.additionalInfos.locationInfo.HorizontalAccuracyInfo
import pro.jayeshseth.knowyourhardware.ui.additionalInfos.locationInfo.IsLocationMockInfo
import pro.jayeshseth.knowyourhardware.ui.additionalInfos.locationInfo.LastKnownLocationInfo
import pro.jayeshseth.knowyourhardware.ui.additionalInfos.locationInfo.LiveLocationInfo
import pro.jayeshseth.knowyourhardware.ui.additionalInfos.locationInfo.MslAltitudeAccuracyInfo
import pro.jayeshseth.knowyourhardware.ui.additionalInfos.locationInfo.MslAltitudeInfo
import pro.jayeshseth.knowyourhardware.ui.additionalInfos.locationInfo.SpeedAccuracyInfo
import pro.jayeshseth.knowyourhardware.ui.additionalInfos.locationInfo.SpeedInfo
import pro.jayeshseth.knowyourhardware.ui.additionalInfos.locationInfo.VerticalAccuracyInfo
import pro.jayeshseth.knowyourhardware.ui.composables.InfoCard
import pro.jayeshseth.knowyourhardware.ui.composables.LocationInfoCard
import pro.jayeshseth.knowyourhardware.ui.composables.ProviderButton
import pro.jayeshseth.knowyourhardware.ui.composables.Toggler
import pro.jayeshseth.knowyourhardware.utils.formatLocationAge
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

//@Preview
//@OptIn(ExperimentalPermissionsApi::class)
@OptIn(ExperimentalPermissionsApi::class)
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
    val usePassiveProvider = remember { mutableStateOf(false) }
    val useNetworkProvider = remember { mutableStateOf(false) }
    val useGPSProvider = remember { mutableStateOf(false) }
    val useFusedProvider = remember { mutableStateOf(false) }
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

//    val approximateLocationPermissionState =
//        rememberPermissionState(Manifest.permission.ACCESS_COARSE_LOCATION)
//    val preciseLocationPermissionState = rememberMultiplePermissionsState(
//        listOf(
//            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION
//        )
//    )

//    if (ActivityCompat.checkSelfPermission(
//            context,
//            Manifest.permission.ACCESS_FINE_LOCATION
//        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//            context,
//            Manifest.permission.ACCESS_COARSE_LOCATION
//        ) != PackageManager.PERMISSION_GRANTED
//    ) {
//        // TODO: Consider calling
//        //    ActivityCompat#requestPermissions
//        // here to request the missing permissions, and then overriding
//        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//        //                                          int[] grantResults)
//        // to handle the case where the user grants the permission. See the documentation
//        // for ActivityCompat#requestPermissions for more details.
//
//        return
//    }
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
            }
            if (useFusedProvider.value) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    val locationRequest =
                        android.location.LocationRequest.Builder(timeInterval).build()
                    LocationManagerUpdatesListener(
                        provider = LocationManager.FUSED_PROVIDER,
                        locationClient = locationManager,
                        locationRequest = locationRequest
                    ) {
                        liveLocation.value = it
                    }
                }
            }
            if (useGPSProvider.value) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    val locationRequest =
                        android.location.LocationRequest.Builder(timeInterval).build()
                    LocationManagerUpdatesListener(
                        provider = LocationManager.GPS_PROVIDER,
                        locationClient = locationManager,
                        locationRequest = locationRequest
                    ) {
                        liveLocation.value = it
                    }
                } else {
                    LocationManagerUpdatesListener(
                        provider = LocationManager.GPS_PROVIDER,
                        locationClient = locationManager
                    ) {
                        liveLocation.value = it
                    }
                }
            }
            if (useNetworkProvider.value) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    val locationRequest =
                        android.location.LocationRequest.Builder(timeInterval).build()
                    LocationManagerUpdatesListener(
                        provider = LocationManager.NETWORK_PROVIDER,
                        locationClient = locationManager,
                        locationRequest = locationRequest
                    ) {
                        liveLocation.value = it
                    }
                } else {
                    LocationManagerUpdatesListener(
                        provider = LocationManager.PASSIVE_PROVIDER,
                        locationClient = locationManager
                    ) {
                        liveLocation.value = it
                    }
                }
            }
            if (usePassiveProvider.value) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    val locationRequest =
                        android.location.LocationRequest.Builder(timeInterval).build()
                    LocationManagerUpdatesListener(
                        provider = LocationManager.GPS_PROVIDER,
                        locationClient = locationManager,
                        locationRequest = locationRequest
                    ) {
                        liveLocation.value = it
                    }
                } else {
                    LocationManagerUpdatesListener(
                        provider = LocationManager.GPS_PROVIDER,
                        locationClient = locationManager
                    ) {
                        liveLocation.value = it
                    }
                }
            }
        }
    }

    when {
        useFusedProvider.value -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                locationManager.getLastKnownLocation(LocationManager.FUSED_PROVIDER)
                    .let {
                        if (it != null) {
                            lastKnownLocation.value = it
                        }
                    }
            }
        }

        useGPSProvider.value -> {
            locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                .let {
                    if (it != null) {
                        lastKnownLocation.value = it
                    }
                }
        }

        useNetworkProvider.value -> {
            locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                .let {
                    if (it != null) {
                        lastKnownLocation.value = it
                    }
                }
        }

        usePassiveProvider.value -> {
            locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER)
                .let {
                    if (it != null) {
                        lastKnownLocation.value = it
                    }
                }
        }

        useFusedGMSProvider.value -> {
            fusedLocationClient.lastLocation.addOnSuccessListener { loc ->
                if (loc != null) {
                    lastKnownLocation.value = loc
                }
            }
        }

        else -> {
            fusedLocationClient.lastLocation.addOnSuccessListener { loc ->
                if (loc != null) {
                    lastKnownLocation.value = loc
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
    val permissions = listOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
    )
//    if (!preciseLocationPermissionState.allPermissionsGranted){
//        Column(
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center
//        ) {
//            Button(onClick = {
////                if (!preciseLocationPermissionState.shouldShowRationale){
////                    context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
////                } else {
//                    preciseLocationPermissionState.launchMultiplePermissionRequest()
////                }
//            }) {
//                if (preciseLocationPermissionState.allPermissionsGranted){
//                    Text(text = "Thank you")
//                } else if (preciseLocationPermissionState.shouldShowRationale){
//                    Text(text = "GIVE Permission")
//                } else {
//                    Text(text = "r u kidding me")
//                }
//            }
//        }
//    } else {
//
//    }
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
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = modifier
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
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
                drawResIcon = R.drawable.round_access_time_filled_24,
                enabled = trackLiveLocation.value
            )
            Toggler(
                title = "Elapsed time in minutes",
                checked = showInMinutes.value,
                onCheckedChanged = {
                    showInMinutes.value = it
                    showInSeconds.value = false
                },
                drawResIcon = R.drawable.round_access_time_filled_24,
                enabled = trackLiveLocation.value
            )
            Toggler(
                title = "Speed in KPH",
                checked = speedInKms.value,
                onCheckedChanged = {
                    speedInKms.value = it
                },
                drawResIcon = R.drawable.speed,
                enabled = trackLiveLocation.value
            )
            ProviderButton(
                text = "Fused (Google Play Services)",
                onClick = {
                    selectedProvider.value = "FusedGMS"
                    useFusedGMSProvider.value = true
                    usePassiveProvider.value = false
                    useGPSProvider.value = false
                    useFusedProvider.value = false
                    useNetworkProvider.value = false
                },
                isSelected = selectedProvider.value == "FusedGMS"
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Max)
            ) {
                gpsManager.allProviders.value.map { provider ->
                    ProviderButton(
                        text = provider.replaceFirstChar { it.uppercase() },
                        isSelected = selectedProvider.value == provider,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize(),
                        onClick = {
                            selectedProvider.value = provider
                            when (provider) {
                                "passive" -> {
                                    usePassiveProvider.value = true
                                    useGPSProvider.value = false
                                    useFusedProvider.value = false
                                    useNetworkProvider.value = false
                                    useFusedGMSProvider.value = false
                                }

                                "gps" -> {
                                    useGPSProvider.value = true
                                    usePassiveProvider.value = false
                                    useFusedProvider.value = false
                                    useNetworkProvider.value = false
                                    useFusedGMSProvider.value = false
                                }

                                "fused" -> {
                                    useFusedProvider.value = true
                                    usePassiveProvider.value = false
                                    useGPSProvider.value = false
                                    useNetworkProvider.value = false
                                    useFusedGMSProvider.value = false
                                }

                                "network" -> {
                                    useNetworkProvider.value = true
                                    usePassiveProvider.value = false
                                    useGPSProvider.value = false
                                    useFusedProvider.value = false
                                    useFusedGMSProvider.value = false
                                }
                            }
                        }
                    )
                }
            }
            InfoCard(
                title = "Current Location Provider",
                info = if (useFusedGMSProvider.value && trackLiveLocation.value) "${liveLocation.value.provider} (GMS)" else "${liveLocation.value.provider}",
                enabled = true,
                additionalInfoContent = { CurrentLocationProviderInfo() }
            )
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                InfoCard(
                    title = "Is Location Mock",
                    info = "${liveLocation.value.isMock}",
                    enabled = true,
                    additionalInfoContent = { IsLocationMockInfo() }
                )
            } else {
                Text(
                    text = "Is Location Mock Information Unavailable for API 30 and below",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
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
                time = liveLocationTimestamp,
                enabled = true,
                additionalInfoContent = { LiveLocationInfo() }
            )
            InfoCard(
                title = "Location Horizontal Accuracy",
                info = if (liveLocation.value.hasAccuracy()) "${liveLocation.value.accuracy}" else "Unavailable",
                enabled = true,
                additionalInfoContent = { HorizontalAccuracyInfo() }
            )
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                InfoCard(
                    title = "Location Vertical Accuracy",
                    info = if (liveLocation.value.hasVerticalAccuracy()) "${liveLocation.value.verticalAccuracyMeters}" else "Unavailable",
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
                info = if (liveLocation.value.hasAltitude()) "${liveLocation.value.altitude}" else "Unavailable",
                enabled = true,
                additionalInfoContent = { AltitudeInfo() }
            )
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                InfoCard(
                    title = "MSL Altitude",
                    info = if (liveLocation.value.hasMslAltitude()) "${liveLocation.value.mslAltitudeMeters}" else "Unavailable",
                    enabled = true,
                    additionalInfoContent = { MslAltitudeInfo() }
                )
                InfoCard(
                    title = "MSL Altitude Accuracy",
                    info = if (liveLocation.value.hasMslAltitudeAccuracy()) "${liveLocation.value.mslAltitudeAccuracyMeters}" else "Unavailable",
                    enabled = true,
                    additionalInfoContent = { MslAltitudeAccuracyInfo() }
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
                title = "Speed",
                info = if (liveLocation.value.hasSpeed()) speed else "Unavailable",
                enabled = true,
                additionalInfoContent = { SpeedInfo() }
            )
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                InfoCard(
                    title = "Speed Accuracy",
                    info = if (liveLocation.value.hasSpeedAccuracy()) "${liveLocation.value.speedAccuracyMetersPerSecond}" else "Unavailable",
                    enabled = true,
                    additionalInfoContent = { SpeedAccuracyInfo() }
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
                info = "${liveLocation.value.extras}",
                enabled = true,
                additionalInfoContent = { ExtrasInfo() }
            )
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
}