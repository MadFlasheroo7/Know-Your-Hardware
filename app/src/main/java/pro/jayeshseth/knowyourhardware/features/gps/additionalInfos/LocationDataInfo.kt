package pro.jayeshseth.knowyourhardware.features.gps.additionalInfos

import android.location.Location
import android.os.Build
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import pro.jayeshseth.knowyourhardware.model.InfoCardData
import pro.jayeshseth.knowyourhardware.model.LocationInfoCardData
import pro.jayeshseth.knowyourhardware.utils.additionalInfo
import pro.jayeshseth.knowyourhardware.utils.formatLocationAge
import pro.jayeshseth.knowyourhardware.utils.isAbove_O
import pro.jayeshseth.knowyourhardware.utils.isAbove_Q
import pro.jayeshseth.knowyourhardware.utils.isAbove_S
import pro.jayeshseth.knowyourhardware.utils.isAbove_TIRAMISU
import pro.jayeshseth.knowyourhardware.utils.isAbove_UPSIDE_DOWN_CAKE






class LocationDataInfo(
    isFromGms: Boolean,
    liveLocation: Location,
    lastKnownLocation: Location,
    liveLocationTimestamp: String?,
    lastKnownLocationTimestamp: String?,
    elapsedTimeMillis: String,
    elapsedTimeNanos: String,
    speed: String,
) {
    private val currentLocationProvider =
        if (isFromGms) "${liveLocation.provider} (GMS)" else "${liveLocation.provider}"
    private val isLocationMock = if (isAbove_S) "${liveLocation.isMock}" else ""
    private val hasAccuracy =
        if (liveLocation.hasAccuracy()) "${liveLocation.accuracy}" else "Unavailable"
    private val hasVerticalAccuracy =
        if (isAbove_O) if (liveLocation.hasVerticalAccuracy()) "${liveLocation.verticalAccuracyMeters}" else "Unavailable" else ""
    private val hasBearing =
        if (liveLocation.hasBearing()) "${liveLocation.bearing}" else "Unavailable"
    private val hasBearingAccuracy =
        if (isAbove_O) if (liveLocation.hasBearingAccuracy()) "${liveLocation.bearingAccuracyDegrees}" else "Unavailable" else ""
    private val elapsedRealtimeUncertaintyNanos =
        if (isAbove_Q) if (liveLocation.hasElapsedRealtimeUncertaintyNanos()) "${liveLocation.elapsedRealtimeUncertaintyNanos}" else "Unavailable" else ""
    private val elapsedRealtimeAgeMillis =
        if (isAbove_TIRAMISU) formatLocationAge(liveLocation.elapsedRealtimeAgeMillis) else ""
    private val altitudeInfo = if (liveLocation.hasAltitude()) "${liveLocation.altitude}" else "Unavailable"
    private val mslAltitudeInfo =
        if (isAbove_UPSIDE_DOWN_CAKE) if (liveLocation.hasMslAltitude()) "${liveLocation.mslAltitudeMeters}" else "Unavailable" else ""
    private val mslAltitudeAccuracyInfo =
        if (isAbove_UPSIDE_DOWN_CAKE) if (liveLocation.hasMslAltitudeAccuracy()) "${liveLocation.mslAltitudeAccuracyMeters}" else "Unavailable" else ""
    private val speedAccuracyInfo =
        if (isAbove_O) if (liveLocation.hasSpeedAccuracy()) "${liveLocation.speedAccuracyMetersPerSecond}" else "Unavailable" else ""

    val locationInfoList: List<LocationInfoCardData> by lazy {
        mutableListOf(
            LocationInfoCardData(
                title = "Last Known Location",
                latitude = lastKnownLocation.latitude.toString(),
                longitude = lastKnownLocation.longitude.toString(),
                time = lastKnownLocationTimestamp,
                additionalInfo = LocationInfoCardData.AdditionalInfo(
                    primaryInfo = "Returns the last saved location by the provider",
                    secondaryInfo = additionalInfo(
                        info = "Once a location service is created it caches the location. Calling it doesn't activate sensor to retrieve location thus doesn't affect battery but it also be null in certain situations" +
                                "\n- device location turned off since it clears the cache" +
                                "\n- if the device never recorded it location or the device got factory reset" +
                                "\n- (if fetched using fused location client) if the google play services had restarted"
                    ),
                    source = "https://developer.android.com/develop/sensors-and-location/location/retrieve-current#last-known"
                )
            ),
            LocationInfoCardData(
                title = "Live Location",
                latitude = liveLocation.latitude.toString(),
                longitude = liveLocation.longitude.toString(),
                time = liveLocationTimestamp,
                additionalInfo = LocationInfoCardData.AdditionalInfo(
                    primaryInfo = "Returns the live location of the device.",
                    secondaryInfo = additionalInfo(
                        info = "live location is updated every second and location capabilities changes based on providers",
                        usage = "Location is updated until the screen is in composition."
                    ),
                    source = "https://developer.android.com/reference/android/location/LocationManager#requestLocationUpdates(java.lang.String,%20android.location.LocationRequest,%20java.util.concurrent.Executor,%20android.location.LocationListener)"
                )
            )
        )
    }

    val locationDataInfoList: List<InfoCardData> by lazy {
        mutableListOf(
            InfoCardData(
                title = "Current Location Provider",
                info = currentLocationProvider,
                additionalInfo = InfoCardData.AdditionalInfo(
                    primaryInfo = "Returns the provider being used to fetch the live location.",
                    secondaryInfo = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.ExtraBold,
                                fontStyle = FontStyle.Italic
                            )
                        ) {
                            append("\nFused: ")
                        }
                        append("Fused location provider combines inputs from all the providers to provide an optimal accuracy and battery consumption")
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.ExtraBold,
                                fontStyle = FontStyle.Italic
                            )
                        ) {
                            append("\nGPS: ")
                        }
                        append("GPS (Global Positioning System) uses the dedicated hardware in the device to communicate with the satellite to provide the location. Works well in open sky and battery consumption is high.")
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.ExtraBold,
                                fontStyle = FontStyle.Italic
                            )
                        ) {
                            append("\nNetwork: ")
                        }
                        append("Uses WIFI or Cell towers to find out estimated location battery consumption is pretty low but at the cost of accuracy.")
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.ExtraBold,
                                fontStyle = FontStyle.Italic
                            )
                        ) {
                            append("\nPassive: ")
                        }
                        append("Passive provider doesn't actively request location updates but rather relies on other providers to give the location updates. Consumes little to no battery since it doesn't actively request location updates")
                    },
                    source = "https://developer.android.com/reference/android/location/Location#getProvider()"
                )
            ),
            InfoCardData(
                title = "Is Location Mock",
                info = isLocationMock,
                additionalInfo = InfoCardData.AdditionalInfo(
                    primaryInfo = "Returns the true if the fetched location is mock or fake.",
                    secondaryInfo = additionalInfo("Useful to check if the location fetched is provided by android framework or not"),
                    source = "https://developer.android.com/reference/android/location/Location#isMock()"
                )
            ),
            InfoCardData(
                title = "Location Horizontal Accuracy",
                info = hasAccuracy,
                additionalInfo = InfoCardData.AdditionalInfo(
                    primaryInfo = "Returns the accuracy of the location on a horizontal plane",
                    secondaryInfo = additionalInfo(
                        "this returns the accuracy in meters that means the reported location is in the radius of the reported meters",
                        "Note: accuracy reported at is 68th percentile confidence level."
                    ),
                    source = "https://developer.android.com/reference/android/location/Location.html#getAccuracy()"
                )
            ),
            InfoCardData(
                title = "Location Vertical Accuracy",
                info = hasVerticalAccuracy,
                additionalInfo = InfoCardData.AdditionalInfo(
                    primaryInfo = "Returns the accuracy of the location on a Vertical plane",
                    secondaryInfo = additionalInfo(
                        "this returns the accuracy in meters that means the reported location falls within \"Altitude\"",
                        "Note: accuracy reported at is 68th percentile confidence level."
                    ),
                    source = "https://developer.android.com/reference/android/location/Location.html#getVerticalAccuracyMeters()"
                )
            ),
            InfoCardData(
                title = "Location Bearing",
                info = hasBearing,
                additionalInfo = InfoCardData.AdditionalInfo(
                    primaryInfo = "Returns the bearing of the location in degrees",
                    secondaryInfo = additionalInfo(
                        "This refers to horizontal direction of the travel of the device. It's measured in degrees ranging from 0 (North) to  360 (Full Circle)",
                    ),
                    source = "https://developer.android.com/reference/android/location/Location.html#getBearing()"
                )
            ),
            InfoCardData(
                title = "Location Bearing Accuracy in Degrees",
                info = hasBearingAccuracy,
                additionalInfo = InfoCardData.AdditionalInfo(
                    primaryInfo = "Returns the accuracy of the bearing of the current location in degrees.",
                    secondaryInfo = additionalInfo(
                        info = "",
                        usage = "Note: accuracy reported at is 68th percentile confidence level.",
                    ),
                    source = "https://developer.android.com/reference/android/location/Location.html#getBearingAccuracyDegrees()"
                )
            ),
            InfoCardData(
                title = "Location Elapsed Realtime in Millis",
                info = elapsedTimeMillis,
                minSdk = Build.VERSION_CODES.TIRAMISU,
                additionalInfo = InfoCardData.AdditionalInfo(
                    primaryInfo = "Returns the elapsed time of the location elapsed realtime since system boot in milli seconds.",
                    secondaryInfo = additionalInfo(
                        info = "",
                        usage = "Similar to Elapsed realtime in nanos.",
                    ),
                    source = "https://developer.android.com/reference/android/location/Location.html#getElapsedRealtimeMillis()"
                )
            ),
            InfoCardData(
                title = "Location Elapsed Realtime in Nanos",
                info = elapsedTimeNanos,
                additionalInfo = InfoCardData.AdditionalInfo(
                    primaryInfo = "Returns the elapsed time of the location elapsed realtime since system boot in nano seconds.",
                    secondaryInfo = additionalInfo(
                        info = "This is more reliable than `time` since its not affected deep sleep.",
                        usage = "Similar to Elapsed realtime in millis.",
                    ),
                    source = "https://developer.android.com/reference/android/location/Location.html#getElapsedRealtimeNanos()"
                )
            ),
            InfoCardData(
                title = "Location Elapsed Realtime uncertainty in Nanos",
                info = elapsedRealtimeUncertaintyNanos,
                minSdk = Build.VERSION_CODES.Q,
                additionalInfo = InfoCardData.AdditionalInfo(
                    primaryInfo = "Returns the uncertainty or accuracy of \"ElapsedRealtimeMillis\"",
                    secondaryInfo = additionalInfo(
                        "",
                        usage = "Note: accuracy reported at is 68th percentile confidence level.",
                    ),
                    source = "https://developer.android.com/reference/android/location/Location.html#getElapsedRealtimeUncertaintyNanos()"
                )
            ),
            InfoCardData(
                title = "Location Elapsed Realtime Age in Millis",
                info = elapsedRealtimeAgeMillis,
                minSdk = Build.VERSION_CODES.TIRAMISU,
                additionalInfo = InfoCardData.AdditionalInfo(
                    primaryInfo = "Returns the age of this location in milli seconds with respect to time elapsed.",
                    source = "https://developer.android.com/reference/android/location/Location.html#getElapsedRealtimeAgeMillis()"
                )
            ),
            InfoCardData(
                title = "Altitude",
                info = altitudeInfo,
                additionalInfo = InfoCardData.AdditionalInfo(
                    primaryInfo = "Returns the altitude of the location.",
                    secondaryInfo = additionalInfo("this returns altitude of this location in meters above the WGS84 (World Geodetic System 1984) reference ellipsoid."),
                    source = "https://developer.android.com/reference/android/location/Location.html#getAltitude()"
                )
            ),
            InfoCardData(
                title = "MSL Altitude",
                info = mslAltitudeInfo,
                minSdk = Build.VERSION_CODES.UPSIDE_DOWN_CAKE,
                additionalInfo = InfoCardData.AdditionalInfo(
                    primaryInfo = "Returns the Mean Sea Level altitude of this location in meters.",
                    secondaryInfo = additionalInfo("the altitude of this location above sea level"),
                    source = "https://developer.android.com/reference/android/location/Location#getMslAltitudeMeters()"
                )
            ),
            InfoCardData(
                title = "MSL Altitude Accuracy",
                info = mslAltitudeAccuracyInfo,
                minSdk = Build.VERSION_CODES.UPSIDE_DOWN_CAKE,
                additionalInfo = InfoCardData.AdditionalInfo(
                    primaryInfo = "Returns the accuracy of Mean Sea Level altitude of this location in meters.",
                    secondaryInfo = additionalInfo(
                        "",
                        "Note: accuracy reported at is 68th percentile confidence level."
                    ),
                    source = "https://developer.android.com/reference/android/location/Location#getMslAltitudeAccuracyMeters()"
                )
            ),
            InfoCardData(
                title = "Speed",
                info = if (liveLocation.hasSpeed()) speed else "Unavailable",
                additionalInfo = InfoCardData.AdditionalInfo(
                    primaryInfo = "Returns the speed at the time of this location in meters per second.",
                    source = "https://developer.android.com/reference/android/location/Location#getSpeed()"
                )
            ),
            InfoCardData(
                title = "Speed Accuracy",
                info = speedAccuracyInfo,
                additionalInfo = InfoCardData.AdditionalInfo(
                    primaryInfo = "Returns the accuracy of the speed fetched at the time of the location in meters per second.",
                    secondaryInfo = additionalInfo("", usage = "Note: accuracy reported at is 68th percentile confidence level."),
                    source = "https://developer.android.com/reference/android/location/Location#getSpeedAccuracyMetersPerSecond()"
                )
            ),
            InfoCardData(
                title = "Location Extras",
                info = "${liveLocation.extras}",
                additionalInfo = InfoCardData.AdditionalInfo(
                    primaryInfo = "Returns an optional bundle of additional information associated with this location. The keys and values within the bundle are determined by the location provider.",
                    source = "https://developer.android.com/reference/android/location/Location#getExtras()"
                )
            )
        )
    }

    val liveLocationCapabilities: List<InfoCardData> by lazy {
        mutableListOf(
            InfoCardData(
                title = "Has Horizontal Location Accuracy",
                info = "${liveLocation.hasAccuracy()}"
            ),
            InfoCardData(
                title = "Has Vertical Location Accuracy",
                info = if (isAbove_O) "${liveLocation.hasVerticalAccuracy()}" else "",
                minSdk = Build.VERSION_CODES.O
            ),
            InfoCardData(
                title = "Has Altitude",
                info = "${liveLocation.hasAltitude()}"
            ),
            InfoCardData(
                title = "Has Bearing",
                info = "${liveLocation.hasBearing()}",
            ),
            InfoCardData(
                title = "Has Bearing Accuracy",
                info = if (isAbove_O) "${liveLocation.hasBearingAccuracy()}" else "",
                minSdk = Build.VERSION_CODES.O
            ),
            InfoCardData(
                title = "Has Elapsed Realtime Uncertainty in Nanos",
                info = if (isAbove_Q) "${liveLocation.hasElapsedRealtimeUncertaintyNanos()}" else "",
                minSdk = Build.VERSION_CODES.Q
            ),
            InfoCardData(
                title = "Has MSL Altitude",
                info = if (isAbove_UPSIDE_DOWN_CAKE) "${liveLocation.hasMslAltitude()}" else "",
                minSdk = Build.VERSION_CODES.UPSIDE_DOWN_CAKE
            ),
            InfoCardData(
                title = "Has MSL Altitude Accuracy",
                info = if (isAbove_UPSIDE_DOWN_CAKE) "${liveLocation.hasMslAltitudeAccuracy()}" else "",
                minSdk = Build.VERSION_CODES.UPSIDE_DOWN_CAKE
            ),
            InfoCardData(
                title = "Has Speed",
                info = "${liveLocation.hasSpeed()}"
            ),
            InfoCardData(
                title = "Has Speed",
                info = if (isAbove_O) "${liveLocation.hasSpeedAccuracy()}" else "",
                minSdk = Build.VERSION_CODES.O
            )
        )
    }
}