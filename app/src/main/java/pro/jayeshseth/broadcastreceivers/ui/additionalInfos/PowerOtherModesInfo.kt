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
fun PowerOtherModesInfo(modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(top = 12.dp)) {
        Text(
            text = "Returns True if GNSS chip measuring measuring OEM defined mode power",
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        )
        Text(text = buildAnnotatedString {
            append("Tells weather does chip supports OEM defined different power modes.")
        })
        SourceText(url = "https://developer.android.com/reference/android/location/GnssCapabilities#hasPowerOtherModes()")
    }
}