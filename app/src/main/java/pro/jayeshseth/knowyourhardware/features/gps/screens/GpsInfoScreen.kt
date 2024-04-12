package pro.jayeshseth.knowyourhardware.features.gps.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import pro.jayeshseth.knowyourhardware.features.gps.additionalInfos.GpsDataInfo
import pro.jayeshseth.knowyourhardware.features.gps.broadcastReceiver.GpsManager
import pro.jayeshseth.knowyourhardware.ui.composables.InfoCardMapper
import pro.jayeshseth.knowyourhardware.ui.composables.TitleText
import pro.jayeshseth.knowyourhardware.utils.isAbove_UPSIDE_DOWN_CAKE

@Composable
fun GpsInfoScreen(gpsManager: GpsManager, modifier: Modifier = Modifier) {
    val gpsDataInfo = GpsDataInfo(gpsManager)

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
            .padding(horizontal = 16.dp)
    ) {
        item { Spacer(modifier = Modifier.statusBarsPadding()) }
        items(gpsDataInfo.gpsDataInfoList) { info ->
            InfoCardMapper(info)
        }
        item {
            TitleText(text = "Gnss")
        }
        items(gpsDataInfo.gnssDataInfoList) { info ->
            InfoCardMapper(info)
        }
        item {
            Text(
                text = "GNSS Capabilities",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
        if (isAbove_UPSIDE_DOWN_CAKE) {
            items(gpsDataInfo.gnssCapabilitiesList) { info ->
                InfoCardMapper(info)
            }
        } else {
            item {
                Text(
                    text = "Gnss Capabilities Unavailable for API below 34",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        item { Spacer(modifier = Modifier.navigationBarsPadding()) }
    }
}