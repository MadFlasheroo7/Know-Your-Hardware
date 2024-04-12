package pro.jayeshseth.knowyourhardware.ui.composables

import androidx.annotation.DrawableRes
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
    @DrawableRes icon: Int,
    title: String,
    checked: Boolean,
    onCheckedChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Rebugger(
        composableName = "Painter Toggler",
        trackMap = mapOf(
            "icon" to icon,
            "title" to title,
            "checked" to checked,
            "onCheckedChanged" to onCheckedChanged,
            "modifier" to modifier,
            "enabled" to enabled,
        ),
    )
    val painter = painterResource(id = icon)

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
                    painter = painter,
                    contentDescription = "",
                    modifier = Modifier.size(SwitchDefaults.IconSize)
                )
            },
            enabled = enabled
        )
    }
}