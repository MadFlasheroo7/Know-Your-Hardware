package pro.jayeshseth.knowyourhardware.ui.composables

import android.os.Build
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.theapache64.rebugger.Rebugger

const val DURATION = 780

@Composable
fun InfoCard(
    title: String,
    info: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = false,
    minSdk: Int? = null,
    additionalInfoContent: @Composable (ColumnScope.() -> Unit) = {}
) {
    val isVisible = remember { mutableStateOf(false) }
    Rebugger(
        composableName = "InfoCard",
        trackMap = mapOf(
            "title" to title,
            "isVisible" to isVisible,
            "info" to info,
            "modifier" to modifier,
            "enabled" to enabled,
            "additionalInfoContent" to additionalInfoContent,
        ),
    )
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .fillMaxWidth()
            .clickable(
                enabled = enabled,
                onClick = { isVisible.value = !isVisible.value }
            )
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        if (minSdk != null && Build.VERSION.SDK_INT < minSdk) {
            Text(
                text = "\"$title\" Unavailable for API below $minSdk",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "$title :", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                Text(text = info, textAlign = TextAlign.End, modifier = Modifier.weight(1f))
            }
        }
        AnimatedVisibility(
            visible = isVisible.value,
            enter = expandVertically(tween(DURATION, easing = FastOutSlowInEasing)),
            exit = shrinkVertically(
                animationSpec = tween(
                    DURATION,
                    easing = FastOutSlowInEasing
                )
            ) + fadeOut()
        ) {
            additionalInfoContent()
        }
    }
}