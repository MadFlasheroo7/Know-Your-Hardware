package pro.jayeshseth.knowyourhardware.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pro.jayeshseth.knowyourhardware.ui.composables.InfoCard

@Composable
fun GPSInfo(
    modifier: Modifier = Modifier
) {
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
            title = "Last Known Location",
            info = ""
        )
        InfoCard(
            title = "Live Location",
            info = ""
        )
        InfoCard(
            title = "Location Accuracy",
            info = ""
        )
        InfoCard(
            title = "Altitude",
            info = ""
        )
        InfoCard(
            title = "is moving",
            info = ""
        )
        InfoCard(
            title = "Moving Location accuracy",
            info = ""
        )
        InfoCard(
            title = "facing",
            info = ""
        )
    }
}