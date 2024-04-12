package pro.jayeshseth.knowyourhardware.features.gps.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import pro.jayeshseth.knowyourhardware.ui.composables.InteractiveButton

@Composable
fun GPSScreen(
    navToLocationScreen: () -> Unit,
    navToGpsInfoScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .systemBarsPadding()
    ) {
        InteractiveButton(text = "GPS Info", onClick = navToGpsInfoScreen)
        InteractiveButton(text = "Location", onClick = navToLocationScreen)
    }
}