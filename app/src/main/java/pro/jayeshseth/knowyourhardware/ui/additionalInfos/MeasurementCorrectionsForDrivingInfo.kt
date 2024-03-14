package pro.jayeshseth.knowyourhardware.ui.additionalInfos

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pro.jayeshseth.knowyourhardware.ui.composables.SourceText

@Composable
fun MeasurementCorrectionsForDrivingInfo(modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(top = 12.dp)) {
        Text(
            text = "Returns True if GNSS Chip will benefit from measurement corrections for driving use case",
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        )
        SourceText(url = "https://developer.android.com/reference/android/location/GnssCapabilities")
    }
}