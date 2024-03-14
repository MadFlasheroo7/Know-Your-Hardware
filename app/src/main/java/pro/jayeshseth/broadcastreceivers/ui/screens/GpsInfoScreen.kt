package pro.jayeshseth.broadcastreceivers.ui.screens

import android.content.Context
import android.location.LocationManager
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import pro.jayeshseth.broadcastreceivers.broadcastReceivers.GpsManager
import pro.jayeshseth.broadcastreceivers.ui.additionalInfos.AntennaInfo
import pro.jayeshseth.broadcastreceivers.ui.additionalInfos.GeofencingInfo
import pro.jayeshseth.broadcastreceivers.ui.additionalInfos.LowPowerModeInfo
import pro.jayeshseth.broadcastreceivers.ui.additionalInfos.MeasurementCorrectionExcessPathLengthInfo
import pro.jayeshseth.broadcastreceivers.ui.additionalInfos.MeasurementCorrectionInfo
import pro.jayeshseth.broadcastreceivers.ui.additionalInfos.MeasurementCorrectionsForDrivingInfo
import pro.jayeshseth.broadcastreceivers.ui.additionalInfos.MeasurementCorrectionsLosSatsInfo
import pro.jayeshseth.broadcastreceivers.ui.additionalInfos.MeasurementCorrectionsReflectingPlaneInfo
import pro.jayeshseth.broadcastreceivers.ui.additionalInfos.MeasurementCorrelationVectorsInfo
import pro.jayeshseth.broadcastreceivers.ui.additionalInfos.MeasurementsInfo
import pro.jayeshseth.broadcastreceivers.ui.additionalInfos.MsaInfo
import pro.jayeshseth.broadcastreceivers.ui.additionalInfos.MsbInfo
import pro.jayeshseth.broadcastreceivers.ui.additionalInfos.NavigationMessageInfo
import pro.jayeshseth.broadcastreceivers.ui.additionalInfos.OnDemandTimeInfo
import pro.jayeshseth.broadcastreceivers.ui.additionalInfos.PowerMultibandAcquisitionInfo
import pro.jayeshseth.broadcastreceivers.ui.additionalInfos.PowerMultibandTrackingInfo
import pro.jayeshseth.broadcastreceivers.ui.additionalInfos.PowerOtherModesInfo
import pro.jayeshseth.broadcastreceivers.ui.additionalInfos.PowerSinglebandAcquisitionInfo
import pro.jayeshseth.broadcastreceivers.ui.additionalInfos.PowerSinglebandTrackingInfo
import pro.jayeshseth.broadcastreceivers.ui.additionalInfos.PowerTotalsInfo
import pro.jayeshseth.broadcastreceivers.ui.additionalInfos.SatelliteBlocklistInfo
import pro.jayeshseth.broadcastreceivers.ui.additionalInfos.SatellitePvtInfo
import pro.jayeshseth.broadcastreceivers.ui.additionalInfos.SchedulingInfo
import pro.jayeshseth.broadcastreceivers.ui.additionalInfos.SingleShotFixInfo
import pro.jayeshseth.broadcastreceivers.ui.composables.InfoCard
import pro.jayeshseth.broadcastreceivers.ui.composables.TitleText

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
        InfoCard(
            title = "Location",
            info = if (gpsManager.isLocationEnabled.value) "Enabled" else "Disabled"
        )
        InfoCard(
            title = "All Providers",
            info = "${gpsManager.allProviders.value}"
        )
        InfoCard(
            title = "Enabled Provider",
            info = "${gpsManager.enabledProviders.value}"
        )
        TitleText(text = "GNSS", onInfoClick = {})
        InfoCard(
            title = "Gnss Hardware",
            info = "${gpsManager.gnssHardwareModelName.value}"
        )
        InfoCard(
            title = "Gnss Antenna",
            info = "${gpsManager.gnssAntennaInfo.value}"
        )
        InfoCard(
            title = "Gnss Signal Types",
            info = "${gpsManager.gnssSignalType.value}"
        )
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
                text = "Information Unavailable for API below 34",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}