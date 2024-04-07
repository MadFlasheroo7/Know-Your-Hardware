package pro.jayeshseth.knowyourhardware.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import com.theapache64.rebugger.Rebugger
import androidx.compose.ui.unit.dp

@Composable
fun Toggler(
    imageVector: ImageVector,
    title: String,
    checked: Boolean,
    onCheckedChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Rebugger(
        composableName = "imageVector Toggler",
        trackMap = mapOf(
            "imageVector" to imageVector,
            "title" to title,
            "checked" to checked,
            "onCheckedChanged" to onCheckedChanged,
            "modifier" to modifier,
            "enabled" to enabled,
        ),
    )
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title)
        Spacer(modifier = Modifier.padding(8.dp))
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChanged,
            thumbContent = {
                Icon(
                    imageVector = imageVector,
                    contentDescription = "",
                    modifier = Modifier.size(SwitchDefaults.IconSize)
                )
            },
            enabled = enabled
        )
    }
}

@Composable
fun Toggler(
    drawResIcon: Int,
    title: String,
    checked: Boolean,
    onCheckedChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Rebugger(
        composableName = "Painter Toggler",
        trackMap = mapOf(
            "icon" to drawResIcon,
            "title" to title,
            "checked" to checked,
            "onCheckedChanged" to onCheckedChanged,
            "modifier" to modifier,
            "enabled" to enabled,
        ),
    )
    val icon = painterResource(id = drawResIcon)

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title)
        Spacer(modifier = Modifier.padding(8.dp))
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChanged,
            thumbContent = {
                Icon(
                    painter = icon,
                    contentDescription = "",
                    modifier = Modifier.size(SwitchDefaults.IconSize)
                )
            },
            enabled = enabled
        )
    }
}