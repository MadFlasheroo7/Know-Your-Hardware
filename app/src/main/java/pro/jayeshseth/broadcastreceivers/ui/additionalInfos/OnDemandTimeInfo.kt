package pro.jayeshseth.broadcastreceivers.ui.additionalInfos

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
import pro.jayeshseth.broadcastreceivers.ui.composables.SourceText

@Composable
fun OnDemandTimeInfo(modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(top = 12.dp)) {
        Text(
            text = "Returns True if GNSS chip requests periodic time signal injection from the platform in addition to on-demand and occasional time updates",
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        )
        Text(text = buildAnnotatedString {
            append("when available GNSS provides ability to obtain accurate GNSS time information for various purposes, such as precise timing synchronization, time stamping data, or ensuring accurate timekeeping in applications")

        })
        SourceText(url = "https://developer.android.com/reference/android/location/GnssCapabilities#hasOnDemandTime()")
    }
}