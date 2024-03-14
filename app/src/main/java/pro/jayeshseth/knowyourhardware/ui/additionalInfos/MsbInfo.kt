package pro.jayeshseth.knowyourhardware.ui.additionalInfos

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
fun MsbInfo(modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(top = 12.dp)) {
        Text(
            text = "Returns True if GNSS Chip support for MSB (Mobile Station Based Assistance)",
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        )
        Text(text = buildAnnotatedString {
            append("It is pretty similar to Msa with the key difference being MSB can be used even in remote conditions since it doesn't rely on external sources like cell towers")
        })
        SourceText(url = "https://en.wikipedia.org/wiki/Assisted_GNSS")
    }
}