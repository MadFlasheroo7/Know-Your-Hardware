package pro.jayeshseth.knowyourhardware.features.gps.additionalInfos.locationInfo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pro.jayeshseth.knowyourhardware.ui.composables.SourceText

@Composable
fun CurrentLocationProviderInfo(modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(top = 12.dp)) {
        Text(
            text = "Returns the provider being used to fetch the live location",
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        )
        Text(text = buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    fontWeight = FontWeight.ExtraBold,
                    fontStyle = FontStyle.Italic
                )
            ) {
                append("\nFused: ")
            }
            append("Fused location provider combines inputs from all the providers to provide an optimal accuracy and battery consumption")
            withStyle(
                style = SpanStyle(
                    fontWeight = FontWeight.ExtraBold,
                    fontStyle = FontStyle.Italic
                )
            ) {
                append("\nGPS: ")
            }
            append("GPS (Global Positioning System) uses the dedicated hardware in the device to communicate with the satellite to provide the location. Works well in open sky and battery consumption is high.")
            withStyle(
                style = SpanStyle(
                    fontWeight = FontWeight.ExtraBold,
                    fontStyle = FontStyle.Italic
                )
            ) {
                append("\nNetwork: ")
            }
            append("Uses WIFI or Cell towers to find out estimated location battery consumption is pretty low but at the cost of accuracy.")
            withStyle(
                style = SpanStyle(
                    fontWeight = FontWeight.ExtraBold,
                    fontStyle = FontStyle.Italic
                )
            ) {
                append("\nPassive: ")
            }
            append("Passive provider doesn't actively request location updates but rather relies on other providers to give the location updates. Consumes little to no battery since it doesn't actively request location updates")
        })
        SourceText(url = "https://developer.android.com/reference/android/location/LocationManager")
    }
}