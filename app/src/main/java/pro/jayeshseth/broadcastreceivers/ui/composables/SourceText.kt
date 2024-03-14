package pro.jayeshseth.broadcastreceivers.ui.composables

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SourceText(url: String, modifier: Modifier = Modifier) {
    val openWebsiteLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            // Handle the result if needed
        }
    Text(
        text = "Source",
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.tertiary,
        modifier = modifier.padding(top = 12.dp).clickable(
            onClick = {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                openWebsiteLauncher.launch(intent)
            }
        )
    )
}