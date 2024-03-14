package pro.jayeshseth.broadcastreceivers.ui.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

const val DURATION = 780

@Composable
fun InfoCard(
    title: String,
    info: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = false,
    additionalInfoContent: @Composable (ColumnScope.() -> Unit) = {}
) {
    val isVisible = remember {
        mutableStateOf(false)
    }
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
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = "$title :", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
            Text(text = info, textAlign = TextAlign.End, modifier = Modifier.weight(1f))
        }
        AnimatedVisibility(
            visible = isVisible.value,
            enter = expandVertically(tween(DURATION, easing = FastOutSlowInEasing)),
            exit = shrinkVertically(animationSpec = tween(DURATION, easing = FastOutSlowInEasing)) + fadeOut()
        ) {
            additionalInfoContent()
        }
    }
}