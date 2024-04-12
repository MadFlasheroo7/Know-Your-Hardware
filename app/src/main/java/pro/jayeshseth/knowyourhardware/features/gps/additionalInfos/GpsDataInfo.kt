package pro.jayeshseth.knowyourhardware.features.gps.additionalInfos

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import pro.jayeshseth.knowyourhardware.features.gps.broadcastReceiver.GpsManager
import pro.jayeshseth.knowyourhardware.model.InfoCardData
import pro.jayeshseth.knowyourhardware.utils.additionalInfo
import pro.jayeshseth.knowyourhardware.utils.isAbove_P
import pro.jayeshseth.knowyourhardware.utils.isAbove_S
import pro.jayeshseth.knowyourhardware.utils.isAbove_UPSIDE_DOWN_CAKE

class GpsDataInfo(gpsManager: GpsManager) {
    private val gpsState = if (gpsManager.isGpsEnabled.value) "Enabled" else "Disabled"
    private val location =
        if (isAbove_P) if (gpsManager.isLocationEnabled.value) "Enabled" else "Disabled" else ""
    private val allProviders = gpsManager.allProviders.value
    private val enabledProviders = gpsManager.enabledProviders.value
    private val providerProperties =
        if (isAbove_S) "${gpsManager.gpsProviderProperties.value}" else ""
    private val gnssHardware = if (isAbove_P) "${gpsManager.gnssHardwareModelName.value}" else ""
    private val gnssHardwareYear = "${gpsManager.gnssHardwareYear.value}"
    private val gnssAntennaInfo = if (isAbove_S) "${gpsManager.gnssAntennaInfo}" else ""
    private val gnssSignalTypes =
        if (isAbove_UPSIDE_DOWN_CAKE) "${gpsManager.gnssCapabilities.value?.gnssSignalTypes}" else ""

    val gpsDataInfoList: List<InfoCardData> by lazy {
        mutableListOf(
            InfoCardData(
                title = "Gps State",
                info = gpsState,
            ),
            InfoCardData(
                title = "Location",
                info = location,
                minSdk = Build.VERSION_CODES.P
            ),
            InfoCardData(
                title = "All Providers",
                info = "$allProviders"
            ),
            InfoCardData(
                title = "Enabled Providers",
                info = "$enabledProviders"
            ),
            InfoCardData(
                title = "Provider Properties",
                info = providerProperties,
                minSdk = Build.VERSION_CODES.S
            )
        )
    }

    val gnssDataInfoList: List<InfoCardData> by lazy {
        mutableListOf(
            InfoCardData(
                title = "Gnss Hardware",
                info = gnssHardware,
                minSdk = Build.VERSION_CODES.P
            ),
            InfoCardData(
                title = "Gnss Hardware Year",
                info = gnssHardwareYear,
            ),
            InfoCardData(
                title = "Gnss Antenna",
                info = gnssAntennaInfo,
                minSdk = Build.VERSION_CODES.S
            ),
            InfoCardData(
                title = "Gnss Signal Types",
                info = gnssSignalTypes,
                minSdk = Build.VERSION_CODES.UPSIDE_DOWN_CAKE
            )
        )
    }

    @delegate:RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    val gnssCapabilitiesList: List<InfoCardData> by lazy {
        mutableListOf(
            InfoCardData(
                title = "Has Low Power Mode",
                info = "${gpsManager.gnssCapabilities.value?.hasLowPowerMode()}",
                additionalInfo = InfoCardData.AdditionalInfo(
                    primaryInfo = "Returns True if GNSS Chip supports \"Low Power Mode\"",
                    secondaryInfo = additionalInfo(
                        info = "This is a power mode defined in GNSS HAL. When activated the chip focuses on preserving the battery at the cost of accuracy, it completely avoids using GPS and relies largely on Cell towers.You can activate low power mode using",
                        usage = "Priority.PRIORITY_LOW_POWER"
                    ),
                    source = "https://developer.android.com/develop/sensors-and-location/location/battery#understand"
                )
            ),
            InfoCardData(
                title = "Has Antenna Info",
                info = "${gpsManager.gnssCapabilities.value?.hasAntennaInfo()}",
                additionalInfo = InfoCardData.AdditionalInfo(
                    primaryInfo = "Returns True if GNSS chip contains information about the antenna",
                    secondaryInfo = additionalInfo(
                        info = "when available GNSS provides information like Phase Center Offset (POC) and corrections on a spherical mapping the information is listened to using",
                        usage = "GnssAntennaInfo.Listener"
                    ),
                    source = "https://developer.android.com/reference/android/location/GnssAntennaInfo"
                )
            ),
            InfoCardData(
                title = "Has Geofencing",
                info = "${gpsManager.gnssCapabilities.value?.hasGeofencing()}",
                additionalInfo = InfoCardData.AdditionalInfo(
                    primaryInfo = "Returns True if GNSS chip supports geofencing",
                    secondaryInfo = additionalInfo(
                        info = "when available GNSS provides ability to create virtual boundaries in real world usually with technologies like cell towers and GPS hardware one can create a geofence using",
                        usage = "Geofence.Builder()"
                    ),
                    source = "https://developer.android.com/develop/sensors-and-location/location/geofencing"
                )
            ),
            InfoCardData(
                title = "Has Msa (Mobile Station Assisted Assistance)",
                info = "${gpsManager.gnssCapabilities.value?.hasMsa()}",
                additionalInfo = InfoCardData.AdditionalInfo(
                    primaryInfo = "Returns True if GNSS Chip support for MSA (Mobile Station Assisted Assistance)",
                    secondaryInfo = additionalInfo(
                        info = "When available MSA helps with faster positioning and improved accuracy with reduced power consumption.",
                    ),
                    source = "https://en.wikipedia.org/wiki/Assisted_GNSS"
                )
            ),
            InfoCardData(
                title = "Has Msb (Mobile Station Based Assistance)",
                info = "${gpsManager.gnssCapabilities.value?.hasMsb()}",
                additionalInfo = InfoCardData.AdditionalInfo(
                    primaryInfo = "Returns True if GNSS Chip support for MSB (Mobile Station Based Assistance)",
                    secondaryInfo = additionalInfo(
                        info = "It is pretty similar to Msa with the key difference being MSB can be used even in remote conditions since it doesn't rely on external sources like cell towers",
                    ),
                    source = "https://en.wikipedia.org/wiki/Assisted_GNSS"
                )
            ),
            InfoCardData(
                title = "Has Measurement",
                info = "${gpsManager.gnssCapabilities.value?.hasMeasurements()}",
                additionalInfo = InfoCardData.AdditionalInfo(
                    primaryInfo = "Returns True if GNSS chip has the ability to output raw GNSS measurements",
                    secondaryInfo = additionalInfo(
                        info = "when available GNSS provides information like Satellite corrections, SNR (signal to noise ratio), doppler shifts and more., the information can be listened using",
                        usage = "LocationManager.registerGnssMeasurementsCallback()"
                    ),
                    source = "https://developer.android.com/reference/android/location/GnssMeasurement"
                )
            ),
            InfoCardData(
                title = "Has Measurement Corrections",
                info = "${gpsManager.gnssCapabilities.value?.hasMeasurementCorrections()}",
                additionalInfo = InfoCardData.AdditionalInfo(
                    primaryInfo = "Returns True if GNSS Chip supports measurement corrections",
                    source = "https://developer.android.com/reference/android/location/GnssCapabilities"
                )
            ),
            InfoCardData(
                title = "Has Measurement Corrections Excess Path Length",
                info = "${gpsManager.gnssCapabilities.value?.hasMeasurementCorrectionsExcessPathLength()}",
                additionalInfo = InfoCardData.AdditionalInfo(
                    primaryInfo = "Returns True if GNSS Chip supports per satellite excess-path-length measurement corrections",
                    source = "https://developer.android.com/reference/android/location/GnssCapabilities"
                )
            ),
            InfoCardData(
                title = "Has Measurement Corrections LOS Satellite",
                info = "${gpsManager.gnssCapabilities.value?.hasMeasurementCorrectionsLosSats()}",
                additionalInfo = InfoCardData.AdditionalInfo(
                    primaryInfo = "Returns True if GNSS Chip supports line-of-sight satellite identification measurement corrections",
                    source = "https://developer.android.com/reference/android/location/GnssCapabilities"
                )
            ),
            InfoCardData(
                title = "Has Measurement Corrections Reflecting Plane",
                info = "${gpsManager.gnssCapabilities.value?.hasMeasurementCorrectionsReflectingPlane()}",
                additionalInfo = InfoCardData.AdditionalInfo(
                    primaryInfo = "Returns True if GNSS Chip supports reflecting plane measurement corrections",
                    source = "https://developer.android.com/reference/android/location/GnssCapabilities"
                )
            ),
            InfoCardData(
                title = "Has Measurement Corrections Vectors",
                info = "${gpsManager.gnssCapabilities.value?.hasMeasurementCorrelationVectors()}",
                additionalInfo = InfoCardData.AdditionalInfo(
                    primaryInfo = "Returns True if GNSS Chip supports correlation vectors as part of measurements outputs",
                    source = "https://developer.android.com/reference/android/location/GnssCapabilities"
                )
            ),
            InfoCardData(
                title = "Has Navigation Messages",
                info = "${gpsManager.gnssCapabilities.value?.hasNavigationMessages()}",
                additionalInfo = InfoCardData.AdditionalInfo(
                    primaryInfo = "Returns True if GNSS Chip supports navigation messages",
                    secondaryInfo = additionalInfo(
                        info = "Tells weather the device has the capability to send and receive information from satellite." +
                                "This gives chip the ability to clock correction, compute satellite clock, time correction and more." +
                                "you can register to its callback using ",
                        usage = "LocationManager.registerGnssNavigationMessageCallback()"
                    ),
                    source = "https://developer.android.com/reference/android/location/GnssCapabilities"
                )
            ),
            InfoCardData(
                title = "Has On Demand Time",
                info = "${gpsManager.gnssCapabilities.value?.hasOnDemandTime()}",
                additionalInfo = InfoCardData.AdditionalInfo(
                    primaryInfo = "Returns True if GNSS chip requests periodic time signal injection from the platform in addition to on-demand and occasional time updates",
                    secondaryInfo = additionalInfo(
                        info = "when available GNSS provides ability to obtain accurate GNSS time information for various purposes, such as precise timing synchronization, time stamping data, or ensuring accurate timekeeping in applications"
                    ),
                    source = "https://developer.android.com/reference/android/location/GnssCapabilities#hasOnDemandTime()"
                )
            ),
            InfoCardData(
                title = "Has Multi band Acquisition Power",
                info = "${gpsManager.gnssCapabilities.value?.hasPowerMultibandAcquisition()}",
                additionalInfo = InfoCardData.AdditionalInfo(
                    primaryInfo = "Returns True if GNSS chip measuring multi-band acquisition power",
                    secondaryInfo = additionalInfo(
                        info = "Refers to the ability of chip to acquire information from multiple satellite constellations like GPS (US), GLONASS (Russia), Galileo (Europe), BeiDou (China) and more"
                    ),
                    source = "https://developer.android.com/reference/android/location/GnssCapabilities#hasPowerMultibandTracking()"
                )
            ),
            InfoCardData(
                title = "Has Multi band Tracking Power",
                info = "${gpsManager.gnssCapabilities.value?.hasPowerMultibandTracking()}",
                additionalInfo = InfoCardData.AdditionalInfo(
                    primaryInfo = "Returns True if GNSS chip measuring multi-band tracking power",
                    secondaryInfo = additionalInfo(
                        info = "Refers to the ability of chip to simultaneously track signals from multiple frequency bands or satellite constellations."
                    ),
                    source = "https://developer.android.com/reference/android/location/GnssCapabilities"
                )
            ),
            InfoCardData(
                title = "Has Single Band Tracking Power",
                info = "${gpsManager.gnssCapabilities.value?.hasPowerSinglebandTracking()}",
                additionalInfo = InfoCardData.AdditionalInfo(
                    primaryInfo = "Returns True if GNSS chip measuring single-band tracking power",
                    secondaryInfo = additionalInfo(
                        info = "Refers to the ability of chip to track frequency from single band or satellite constellations."
                    ),
                    source = "https://developer.android.com/reference/android/location/GnssCapabilities#hasPowerMultibandTracking()"
                )
            ),
            InfoCardData(
                title = "Has Single Band Acquisition Power",
                info = "${gpsManager.gnssCapabilities.value?.hasPowerSinglebandAcquisition()}",
                additionalInfo = InfoCardData.AdditionalInfo(
                    primaryInfo = "Returns True if GNSS chip measuring single-band acquisition power",
                    secondaryInfo = additionalInfo(
                        info = "Refers to the ability of chip acquire information from single satellite constellations."
                    ),
                    source = "https://developer.android.com/reference/android/location/GnssCapabilities#hasPowerSinglebandAcquisition()"
                )
            ),
            InfoCardData(
                title = "Has Power Other Modes",
                info = "${gpsManager.gnssCapabilities.value?.hasPowerOtherModes()}",
                additionalInfo = InfoCardData.AdditionalInfo(
                    primaryInfo = "Returns True if GNSS chip measuring measuring OEM defined mode power",
                    secondaryInfo = additionalInfo(
                        info = "Tells weather does chip supports OEM defined different power modes."
                    ),
                    source = "https://developer.android.com/reference/android/location/GnssCapabilities"
                )
            ),
            InfoCardData(
                title = "Has Power Totals",
                info = "${gpsManager.gnssCapabilities.value?.hasPowerTotal()}",
                additionalInfo = InfoCardData.AdditionalInfo(
                    primaryInfo = "Returns True if GNSS Chip can output its power totals",
                    source = "https://developer.android.com/reference/android/location/GnssCapabilities#hasPowerTotal()"
                )
            ),
            InfoCardData(
                title = "Has Satellite Blocklist",
                info = "${gpsManager.gnssCapabilities.value?.hasSatelliteBlocklist()}",
                additionalInfo = InfoCardData.AdditionalInfo(
                    primaryInfo = "Returns True if GNSS chip supports satellite blocklisting",
                    secondaryInfo = additionalInfo("Refers to the ability of chip to block certain satellites for better accuracy"),
                    source = "https://developer.android.com/reference/android/location/GnssCapabilities"
                )
            ),
            InfoCardData(
                title = "Has Satellite PVT",
                info = "${gpsManager.gnssCapabilities.value?.hasSatellitePvt()}",
                additionalInfo = InfoCardData.AdditionalInfo(
                    primaryInfo = "Returns True if GNSS chip supports supports satellite PVT (Position, Velocity & Time)",
                    source = "https://developer.android.com/reference/android/location/GnssCapabilities"
                )
            ),
            InfoCardData(
                title = "Has Single Shot Fix",
                info = "${gpsManager.gnssCapabilities.value?.hasSingleShotFix()}",
                additionalInfo = InfoCardData.AdditionalInfo(
                    primaryInfo = "Returns True if GNSS chip supports single shot locating",
                    secondaryInfo = additionalInfo("Refers to the ability of chip to obtain a user's position with a single request or measurement with relatively accurate position fix in a short amount of time"),
                    source = "https://developer.android.com/reference/android/location/GnssCapabilities"
                )
            )
        )
    }
}