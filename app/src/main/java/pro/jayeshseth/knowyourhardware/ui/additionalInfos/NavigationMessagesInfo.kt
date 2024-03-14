package pro.jayeshseth.knowyourhardware.ui.additionalInfos

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
fun NavigationMessageInfo(modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(top = 12.dp)) {
        Text(
            text = "Returns True if GNSS Chip supports navigation messages",
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        )
        Text(text = buildAnnotatedString {
            append("Tells weather the device has the capability to send and receive information from satellite.")
            append("This gives chip the ability to clock correction, compute satellite clock, time correction and more.")
            append("you can register to its callback using")
            withStyle(
                style = SpanStyle(
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic
                )
            ) {
                append("\nLocationManager.registerGnssNavigationMessageCallback()")
            }
        })
        SourceText(url = "https://gssc.esa.int/navipedia/index.php/GPS_Navigation_Message")
    }
}