package pro.jayeshseth.knowyourhardware.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun ProviderButton(
    text: String,
    onClick: () -> Unit,
    isSelected: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .background(
                if (isSelected) MaterialTheme.colorScheme.surfaceVariant else Color.Transparent
            )
            .border(
                2.dp,
                MaterialTheme.colorScheme.surfaceVariant,
                RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 8.dp, vertical = 12.dp)
    ) {
        Text(
            text = text,
//            modifier = modifier
//                .clip(RoundedCornerShape(12.dp))
//                .fillMaxWidth()
//                .clickable(onClick = onClick)
//                .background(
//                    if (isSelected) MaterialTheme.colorScheme.surfaceVariant else Color.Transparent
//                )
//                .border(
//                    2.dp,
//                    MaterialTheme.colorScheme.surfaceVariant,
//                    RoundedCornerShape(12.dp)
//                )
//                .padding(horizontal = 8.dp, vertical = 12.dp),
//            textAlign = TextAlign.Center
        )
    }
}