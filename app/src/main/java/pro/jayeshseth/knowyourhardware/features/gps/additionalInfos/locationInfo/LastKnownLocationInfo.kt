package pro.jayeshseth.knowyourhardware.features.gps.additionalInfos.locationInfo

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
fun LastKnownLocationInfo(modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(top = 12.dp)) {
        Text(
            text = "Returns the last saved location by the provider",
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        )
        Text(text = buildAnnotatedString {
            append("Once a location service is created it caches the location. Calling it doesn't activate sensor to retrieve location thus doesn't affect battery but it also be null in certain situations")
            append("\n- device location turned off since it clears the cache")
            append("\n- if the device never recorded it location or the device got factory reset")
            append("\n- (if fetched using fused location client) if the google play services had restarted")
        })
        SourceText(url = "https://developer.android.com/develop/sensors-and-location/location/retrieve-current#last-known")
    }
}