package pro.jayeshseth.knowyourhardware.features.gps.screens

import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import pro.jayeshseth.knowyourhardware.features.gps.additionalInfos.gpsInfo.AntennaInfo
import pro.jayeshseth.knowyourhardware.features.gps.additionalInfos.gpsInfo.GeofencingInfo
import pro.jayeshseth.knowyourhardware.features.gps.additionalInfos.gpsInfo.LowPowerModeInfo
import pro.jayeshseth.knowyourhardware.features.gps.additionalInfos.gpsInfo.MeasurementCorrectionExcessPathLengthInfo
import pro.jayeshseth.knowyourhardware.features.gps.additionalInfos.gpsInfo.MeasurementCorrectionInfo
import pro.jayeshseth.knowyourhardware.features.gps.additionalInfos.gpsInfo.MeasurementCorrectionsForDrivingInfo
import pro.jayeshseth.knowyourhardware.features.gps.additionalInfos.gpsInfo.MeasurementCorrectionsLosSatsInfo
import pro.jayeshseth.knowyourhardware.features.gps.additionalInfos.gpsInfo.MeasurementCorrectionsReflectingPlaneInfo
import pro.jayeshseth.knowyourhardware.features.gps.additionalInfos.gpsInfo.MeasurementCorrelationVectorsInfo
import pro.jayeshseth.knowyourhardware.features.gps.additionalInfos.gpsInfo.MeasurementsInfo
import pro.jayeshseth.knowyourhardware.features.gps.additionalInfos.gpsInfo.MsaInfo
import pro.jayeshseth.knowyourhardware.features.gps.additionalInfos.gpsInfo.MsbInfo
import pro.jayeshseth.knowyourhardware.features.gps.additionalInfos.gpsInfo.NavigationMessageInfo
import pro.jayeshseth.knowyourhardware.features.gps.additionalInfos.gpsInfo.OnDemandTimeInfo
import pro.jayeshseth.knowyourhardware.features.gps.additionalInfos.gpsInfo.PowerMultibandAcquisitionInfo
import pro.jayeshseth.knowyourhardware.features.gps.additionalInfos.gpsInfo.PowerMultibandTrackingInfo
import pro.jayeshseth.knowyourhardware.features.gps.additionalInfos.gpsInfo.PowerOtherModesInfo
import pro.jayeshseth.knowyourhardware.features.gps.additionalInfos.gpsInfo.PowerSinglebandAcquisitionInfo
import pro.jayeshseth.knowyourhardware.features.gps.additionalInfos.gpsInfo.PowerSinglebandTrackingInfo
import pro.jayeshseth.knowyourhardware.features.gps.additionalInfos.gpsInfo.PowerTotalsInfo
import pro.jayeshseth.knowyourhardware.features.gps.additionalInfos.gpsInfo.SatelliteBlocklistInfo
import pro.jayeshseth.knowyourhardware.features.gps.additionalInfos.gpsInfo.SatellitePvtInfo
import pro.jayeshseth.knowyourhardware.features.gps.additionalInfos.gpsInfo.SchedulingInfo
import pro.jayeshseth.knowyourhardware.features.gps.additionalInfos.gpsInfo.SingleShotFixInfo
import pro.jayeshseth.knowyourhardware.features.gps.broadcastReceiver.GpsManager
import pro.jayeshseth.knowyourhardware.ui.composables.InfoCard
import pro.jayeshseth.knowyourhardware.ui.composables.MinSdkText
import pro.jayeshseth.knowyourhardware.ui.composables.TitleText

@Composable
fun GpsInfoScreen(gpsManager: GpsManager, modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
            .padding(horizontal = 16.dp)
            .verticalScroll(
                rememberScrollState()
            )
            .systemBarsPadding()
    ) {
        InfoCard(
            title = "Gps State",
            info = if (gpsManager.isGpsEnabled.value) "Enabled" else "Disabled"
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            InfoCard(
                title = "Location",
                info = if (gpsManager.isLocationEnabled.value) "Enabled" else "Disabled"
            )
        } else {
            MinSdkText(title = "Is Location Enabled", minSdk = Build.VERSION_CODES.P)
        }
        InfoCard(
            title = "All Providers",
            info = "${gpsManager.allProviders.value}"
        )
        InfoCard(
            title = "Enabled Provider",
            info = "${gpsManager.enabledProviders.value}"
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            InfoCard(
                title = "Provider Properties",
                info = "${gpsManager.gpsProviderProperties.value}"
            )
        } else {
            MinSdkText(title = "Provider Properties", minSdk = Build.VERSION_CODES.S)
        }
        TitleText(text = "GNSS")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            InfoCard(
                title = "Gnss Hardware",
                info = "${gpsManager.gnssHardwareModelName.value}"
            )
        } else {
            MinSdkText(title = "Gnss Hardware", minSdk = Build.VERSION_CODES.P)
        }
        InfoCard(
            title = "Gnss Hardware Year",
            info = "${gpsManager.gnssHardwareYear.value}"
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            InfoCard(
                title = "Gnss Antenna",
                info = "${gpsManager.gnssAntennaInfo}"
            )
        } else {
            MinSdkText(title = "Gnss Antenna", minSdk = Build.VERSION_CODES.S)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            InfoCard(
                title = "Gnss Signal Types",
                info = "${gpsManager.gnssCapabilities.value?.gnssSignalTypes}"
            )
        } else {
            MinSdkText(title = "Gnss Signal Types", minSdk = Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
        }
        Text(
            text = "GNSS Capabilities",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            InfoCard(
                title = "Has Low Power Mode",
                info = "${gpsManager.gnssCapabilities.value?.hasLowPowerMode()}",
                enabled = true,
                additionalInfoContent = { LowPowerModeInfo() }
            )
            InfoCard(
                title = "Has Antenna Info",
                info = "${gpsManager.gnssCapabilities.value?.hasAntennaInfo()}",
                enabled = true,
                additionalInfoContent = { AntennaInfo() }
            )
            InfoCard(
                title = "Has Geofencing",
                info = "${gpsManager.gnssCapabilities.value?.hasGeofencing()}",
                enabled = true,
                additionalInfoContent = { GeofencingInfo() }
            )
            InfoCard(
                title = "Has Msa (Mobile Station Assisted Assistance)",
                info = "${gpsManager.gnssCapabilities.value?.hasMsa()}",
                enabled = true,
                additionalInfoContent = { MsaInfo() }
            )
            InfoCard(
                title = "Has Msb (Mobile Station Based Assistance)",
                info = "${gpsManager.gnssCapabilities.value?.hasMsb()}",
                enabled = true,
                additionalInfoContent = { MsbInfo() }
            )
            InfoCard(
                title = "Has Measurement",
                info = "${gpsManager.gnssCapabilities.value?.hasMeasurements()}",
                enabled = true,
                additionalInfoContent = { MeasurementsInfo() }
            )
            InfoCard(
                title = "Has Measurement Corrections",
                info = "${gpsManager.gnssCapabilities.value?.hasMeasurementCorrections()}",
                enabled = true,
                additionalInfoContent = { MeasurementCorrectionInfo() }
            )
            InfoCard(
                title = "Has Measurement Corrections Excess Path Length",
                info = "${gpsManager.gnssCapabilities.value?.hasMeasurementCorrectionsExcessPathLength()}",
                enabled = true,
                additionalInfoContent = { MeasurementCorrectionExcessPathLengthInfo() }
            )
            InfoCard(
                title = "Has Measurement Corrections For Driving",
                info = "${gpsManager.gnssCapabilities.value?.hasMeasurementCorrectionsForDriving()}",
                enabled = true,
                additionalInfoContent = { MeasurementCorrectionsForDrivingInfo() }
            )
            InfoCard(
                title = "Has Measurement Corrections LOS Satellite",
                info = "${gpsManager.gnssCapabilities.value?.hasMeasurementCorrectionsLosSats()}",
                enabled = true,
                additionalInfoContent = { MeasurementCorrectionsLosSatsInfo() }
            )
            InfoCard(
                title = "Has Measurement Corrections Reflecting Plane",
                info = "${gpsManager.gnssCapabilities.value?.hasMeasurementCorrectionsReflectingPlane()}",
                enabled = true,
                additionalInfoContent = { MeasurementCorrectionsReflectingPlaneInfo() }
            )
            InfoCard(
                title = "Has Measurement Corrections Vectors",
                info = "${gpsManager.gnssCapabilities.value?.hasMeasurementCorrelationVectors()}",
                enabled = true,
                additionalInfoContent = { MeasurementCorrelationVectorsInfo() }
            )
            InfoCard(
                title = "Has Navigation Messages",
                info = "${gpsManager.gnssCapabilities.value?.hasNavigationMessages()}",
                enabled = true,
                additionalInfoContent = { NavigationMessageInfo() }
            )
            InfoCard(
                title = "Has On Demand Time",
                info = "${gpsManager.gnssCapabilities.value?.hasOnDemandTime()}",
                enabled = true,
                additionalInfoContent = { OnDemandTimeInfo() }
            )
            InfoCard(
                title = "Has Multi band Acquisition Power",
                info = "${gpsManager.gnssCapabilities.value?.hasPowerMultibandTracking()}",
                enabled = true,
                additionalInfoContent = { PowerMultibandAcquisitionInfo() }
            )
            InfoCard(
                title = "Has Multi band Tracking Power",
                info = "${gpsManager.gnssCapabilities.value?.hasPowerMultibandTracking()}",
                enabled = true,
                additionalInfoContent = { PowerMultibandTrackingInfo() }
            )
            InfoCard(
                title = "Has Single Band Acquisition Power",
                info = "${gpsManager.gnssCapabilities.value?.hasPowerSinglebandTracking()}",
                enabled = true,
                additionalInfoContent = { PowerSinglebandAcquisitionInfo() }
            )
            InfoCard(
                title = "Has Single Band Tracking Power",
                info = "${gpsManager.gnssCapabilities.value?.hasPowerSinglebandTracking()}",
                enabled = true,
                additionalInfoContent = { PowerSinglebandTrackingInfo() }
            )
            InfoCard(
                title = "Has Power Other Modes",
                info = "${gpsManager.gnssCapabilities.value?.hasPowerOtherModes()}",
                enabled = true,
                additionalInfoContent = { PowerOtherModesInfo() }
            )
            InfoCard(
                title = "Has Power Totals",
                info = "${gpsManager.gnssCapabilities.value?.hasPowerTotal()}",
                enabled = true,
                additionalInfoContent = { PowerTotalsInfo() }
            )
            InfoCard(
                title = "Has Satellite Blocklist",
                info = "${gpsManager.gnssCapabilities.value?.hasSatelliteBlocklist()}",
                enabled = true,
                additionalInfoContent = { SatelliteBlocklistInfo() }
            )
            InfoCard(
                title = "Has Satellite PVT",
                info = "${gpsManager.gnssCapabilities.value?.hasSatellitePvt()}",
                enabled = true,
                additionalInfoContent = { SatellitePvtInfo() }
            )
            InfoCard(
                title = "Has Scheduling",
                info = "${gpsManager.gnssCapabilities.value?.hasScheduling()}",
                enabled = true,
                additionalInfoContent = { SchedulingInfo() }
            )
            InfoCard(
                title = "Has Single Shot Fix",
                info = "${gpsManager.gnssCapabilities.value?.hasSingleShotFix()}",
                enabled = true,
                additionalInfoContent = { SingleShotFixInfo() }
            )
        } else {
            Text(
                text = "Gnss Capabilities Unavailable for API below 34",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}