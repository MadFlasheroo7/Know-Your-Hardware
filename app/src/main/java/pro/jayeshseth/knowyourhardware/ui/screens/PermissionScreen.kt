package pro.jayeshseth.knowyourhardware.ui.screens

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.theapache64.rebugger.Rebugger
import pro.jayeshseth.knowyourhardware.ui.composables.InteractiveButton
import pro.jayeshseth.knowyourhardware.ui.composables.RationaleDialog
import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
//import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import kotlin.math.min
import kotlinx.coroutines.delay


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionScreen(
    context: Context,
    permission: String,
    title: String,
    rationale: String,
    permanentlyDeclinedRationale: String,
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit
) {
    val showDialog = remember { mutableStateOf(false) }
    val isPermanentlyDeclined = remember { mutableStateOf(false) }
    val trimmedPermission by remember {
        mutableStateOf(permission.trimAndroidPrefix())
    }
    val permissionState = rememberPermissionState(
        permission = permission,
        onPermissionResult = {
            if (it) {
                Toast.makeText(context, "$trimmedPermission Permission granted", Toast.LENGTH_SHORT)
                    .show()
            } else if (isAtleastDeclinedOnce(context, permission)) {
                Toast.makeText(context, "$trimmedPermission Permission Denied", Toast.LENGTH_SHORT)
                    .show()
            } else {
                isPermanentlyDeclined.value = true
            }
        }
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxSize()
    ) {
        icon()
        Text(
            text = "Required Permission",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = " - $trimmedPermission",
            style = MaterialTheme.typography.bodyMedium,
            fontStyle = FontStyle.Italic,
            modifier = Modifier.padding(16.dp)
        )

        if (isPermanentlyDeclined.value) {
            Text(
                text = "Permission Permanently Denied",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        InteractiveButton(
            text = "Grant Permission",
            height = 80.dp,
            modifier = Modifier.padding(horizontal = 24.dp),
            onClick = {
                if (permissionState.status.shouldShowRationale || isPermanentlyDeclined.value) {
                    showDialog.value = !showDialog.value
                } else {
                    permissionState.launchPermissionRequest()
                }
            })

        if (showDialog.value) {
            RationaleDialog(
                context = context,
                title = title,
                rationale = if (permissionState.status.shouldShowRationale) rationale else permanentlyDeclinedRationale,
                isPermanentlyDeclined = isPermanentlyDeclined.value,
                onConfirmClick = { permissionState.launchPermissionRequest() },
                onDismissRequest = { showDialog.value = !showDialog.value })
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionScreen(
    context: Context,
    permissions: List<String>,
    title: String,
    rationale: String,
    permanentlyDeclinedRationale: String,
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit
) {
    val showDialog = remember { mutableStateOf(false) }
    val isPermanentlyDeclined = remember { mutableStateOf(false) }
    val permissionList = remember(permissions) {
        permissions
    }
    val permissionState = rememberMultiplePermissionsState(
        permissions = permissionList,
        onPermissionsResult = { result ->
            result.keys.map {
                if (result[it] == true) {
                    Toast.makeText(
                        context,
                        "${it.trimAndroidPrefix()} Permission granted",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (isAtleastDeclinedOnce(context, it)) {
                    Toast.makeText(
                        context,
                        "${it.trimAndroidPrefix()} Permission Denied",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    isPermanentlyDeclined.value = true
                }
            }
        }
    )

    Rebugger(
        composableName = "Permission Screen - multiple",
        trackMap = mapOf(
            "show dialog" to showDialog.value,
            "isPermanentlyDeclined" to isPermanentlyDeclined.value,
            "permission state" to permissionState,
            "is all granted" to permissionState.allPermissionsGranted,
            "context" to context,
            "permissions" to permissions,
            "title" to title,
            "rationale" to rationale,
            "permanentlyDeclinedRationale" to permanentlyDeclinedRationale,
            "modifier" to modifier,
            "icon" to icon
        ),
    )
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxSize()
    ) {
        icon()
        Text(
            text = "Required Permissions",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp)
        )

        Text(
            text = permissionState.revokedPermissions.joinToString("\n") {
                " - ${it.permission.trimAndroidPrefix()}"
            },
            style = MaterialTheme.typography.bodyMedium,
            fontStyle = FontStyle.Italic,
            modifier = Modifier.padding(16.dp)
        )

        if (isPermanentlyDeclined.value) {
            Text(
                text = "Permission Permanently Denied",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        InteractiveButton(
            text = "Grant Permission",
            height = 80.dp,
            modifier = Modifier.padding(horizontal = 24.dp),
            onClick = {
                if (permissionState.shouldShowRationale || isPermanentlyDeclined.value) {
                    showDialog.value = !showDialog.value
                } else {
                    permissionState.launchMultiplePermissionRequest()
                }
            })

        if (showDialog.value) {
            RationaleDialog(
                context = context,
                title = title,
                rationale = if (permissionState.shouldShowRationale) rationale else permanentlyDeclinedRationale,
                isPermanentlyDeclined = isPermanentlyDeclined.value,
                onConfirmClick = {
                    permissionState.launchMultiplePermissionRequest()
                },
                onDismissRequest = { showDialog.value = !showDialog.value })
        }
    }
}

private fun String.trimAndroidPrefix(): String {
    return this.removePrefix("android.permission.")
}

private fun isAtleastDeclinedOnce(context: Context, permission: String): Boolean {
    return shouldShowRequestPermissionRationale(context as Activity, permission)
}