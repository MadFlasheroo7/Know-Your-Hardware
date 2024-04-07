package pro.jayeshseth.knowyourhardware.ui.screens

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import pro.jayeshseth.knowyourhardware.ui.composables.InteractiveButton

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionScreen(
    context: Context,
    permission: String,
    title: String,
    rationale: String,
    permanentlyDeclinedRationale: String,
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit,
    onGranted: @Composable () -> Unit
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

    if (permissionState.status.isGranted) {
        onGranted()
    } else {
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
    icon: @Composable () -> Unit,
    onGranted: @Composable () -> Unit
) {
    val showDialog = remember { mutableStateOf(false) }
    val isPermanentlyDeclined = remember { mutableStateOf(false) }
    val permissionState = rememberMultiplePermissionsState(
        permissions = permissions,
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

    if (permissionState.allPermissionsGranted) {
        onGranted()
    } else {
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
}

@Composable
fun RationaleDialog(
    context: Context,
    title: String,
    rationale: String,
    isPermanentlyDeclined: Boolean,
    onConfirmClick: () -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        title = { Text(title) },
        text = {
            Text(rationale)
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (isPermanentlyDeclined) {
                        val intent = Intent(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.fromParts("package", context.packageName, null)
                        )
                        context.startActivity(intent)
                    } else {
                        onConfirmClick()
                    }
                }
            ) {
                Text(if (isPermanentlyDeclined) "Go To Settings".lowercase() else "I Understand")
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismissRequest() }) {
                Text("Dismiss")
            }
        }
    )

}

fun String.trimAndroidPrefix(): String {
    return this.removePrefix("android.permission.")
}

fun isAtleastDeclinedOnce(context: Context, permission: String): Boolean {
    return ActivityCompat.shouldShowRequestPermissionRationale(context as Activity, permission)
}