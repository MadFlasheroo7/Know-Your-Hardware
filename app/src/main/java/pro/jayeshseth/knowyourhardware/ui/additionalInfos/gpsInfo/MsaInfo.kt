package pro.jayeshseth.knowyourhardware.ui.additionalInfos.gpsInfo

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
fun MsaInfo(modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(top = 12.dp)) {
        Text(
            text = "Returns True if GNSS Chip support for MSA (Mobile Station Assisted Assistance)",
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        )
        Text(text = buildAnnotatedString {
            append("When available MSA helps with faster positioning and improved accuracy with reduced power consumption.")
        })
        SourceText(url = "https://en.wikipedia.org/wiki/Assisted_GNSS")
    }
}