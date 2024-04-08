package pro.jayeshseth.knowyourhardware.features.gps.additionalInfos.gpsInfo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pro.jayeshseth.knowyourhardware.ui.composables.SourceText

@Composable
fun PowerMultibandAcquisitionInfo(modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(top = 12.dp)) {
        Text(
            text = "Returns True if GNSS chip measuring multi-band acquisition power",
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        )
        Text(text = buildAnnotatedString {
            append("Refers to the ability of chip to acquire information from multiple satellite constellations like GPS (US), GLONASS (Russia), Galileo (Europe), BeiDou (China) and more")
        })
        SourceText(url = "https://developer.android.com/reference/android/location/GnssCapabilities#hasPowerMultibandAcquisition()")
    }
}