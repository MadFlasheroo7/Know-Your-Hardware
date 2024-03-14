package pro.jayeshseth.broadcastreceivers.ui.additionalInfos

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
import pro.jayeshseth.broadcastreceivers.ui.composables.SourceText

@Composable
fun AntennaInfo(modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(top = 12.dp)) {
        Text(
            text = "Returns True if GNSS chip contains information about the antenna",
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        )
        Text(text = buildAnnotatedString {
            append("when available GNSS provides information like Phase Center Offset (POC) and corrections on a spherical mapping")
            append("the information is listened to using")
            withStyle(
                style = SpanStyle(
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic
                )
            ) {
                append("\nGnssAntennaInfo.Listener")
            }
        })
        SourceText(url = "https://developer.android.com/reference/android/location/GnssAntennaInfo")
    }
}