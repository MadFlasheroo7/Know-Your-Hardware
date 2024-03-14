package pro.jayeshseth.knowyourhardware.ui.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TitleText(
    text: String,
    modifier: Modifier = Modifier
) {
    val isVisible = remember { mutableStateOf(false) }
    Column {
        CenterAlignedTopAppBar(
            windowInsets = WindowInsets(0,0,0,0),
            modifier = modifier,
            title = {
                Text(
                    text = text,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                )
            },
            actions = {
                IconButton(onClick = { isVisible.value = !isVisible.value}) {
                    Icon(imageVector = Icons.Outlined.Info, contentDescription = "info button")
                }
            }
        )
        AnimatedVisibility(
            visible = isVisible.value,
            enter = expandVertically(tween(DURATION, easing = FastOutSlowInEasing)),
            exit = shrinkVertically(animationSpec = tween(DURATION, easing = FastOutSlowInEasing)) + fadeOut()
        ) {
            Column(modifier = modifier.padding(top = 12.dp)) {
                Text(
                    text = "GNSS stands for Global Navigation Satellite System",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(text = buildAnnotatedString {
                    append("GNSS helps us receive signals from satellites like GPS (US), GLONASS (Russia), Galileo (Europe), BeiDou (China) and more, ")
                    append("this helps us determine accurate device location.")
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Bold,
                            fontStyle = FontStyle.Italic
                        )
                    ) {
                        append("\nNote: Support for Raw GNSS is Mandatory that run android 10 (api 29) or higher.")
                    }
                })
                SourceText(url = "https://developer.android.com/develop/sensors-and-location/sensors/gnss#supported-devices")
            }
        }
    }
}