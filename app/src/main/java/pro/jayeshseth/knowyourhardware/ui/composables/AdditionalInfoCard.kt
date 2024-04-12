package pro.jayeshseth.knowyourhardware.ui.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AdditionalInfoCard(
    primaryInfo: String,
    source: String,
    modifier: Modifier = Modifier,
    secondaryInfo: AnnotatedString? = null
) {
    Column(modifier = modifier.padding(top = 12.dp)) {
        Text(
            text = primaryInfo,
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        )
        if (secondaryInfo != null) {
            Text(secondaryInfo)
        }
        SourceText(source)
    }
}