package pro.jayeshseth.knowyourhardware.ui.additionalInfos.locationInfo

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
fun ElapsedRealtimeAgeMillisInfo(modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(top = 12.dp)) {
        Text(
            text = "Returns the age of this location in milli seconds with respect to time elapsed.",
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        )
        SourceText(url = "https://developer.android.com/reference/android/location/Location.html#getElapsedRealtimeAgeMillis()")
    }
}