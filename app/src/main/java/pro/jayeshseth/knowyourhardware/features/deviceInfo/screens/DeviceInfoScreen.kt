package pro.jayeshseth.knowyourhardware.features.deviceInfo.screens

import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import pro.jayeshseth.commoncomponents.StatusBarAwareThemedColumn
import pro.jayeshseth.knowyourhardware.ui.composables.InfoCard

@Composable
fun DeviceInfoScreen(
    modifier: Modifier = Modifier
) {
    StatusBarAwareThemedColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
            .padding(horizontal = 16.dp)
            .verticalScroll(
                rememberScrollState()
            )
            .systemBarsPadding()
    ) {
        InfoCard(title = "API Version", info = "${Build.VERSION.SDK_INT}")
        InfoCard(title = "Board", info = Build.BOARD)
        InfoCard(title = "Bootloader Version", info = Build.BOOTLOADER)
        InfoCard(title = "Brand", info = Build.BRAND)
        InfoCard(title = "Device", info = Build.DEVICE)
        InfoCard(title = "Build ID", info = Build.DISPLAY)
        Build.SUPPORTED_ABIS.forEach {
            InfoCard(title = "Supported ABIs", info = it)
        }
        InfoCard(title = "Fingerprint", info = Build.FINGERPRINT)
        InfoCard(title = "Hardware", info = Build.HARDWARE)
        InfoCard(title = "ID", info = Build.ID)
        InfoCard(title = "Host", info = Build.HOST)
        InfoCard(title = "Manufacturer", info = Build.MANUFACTURER)
        InfoCard(title = "Model", info = Build.MODEL)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            InfoCard(title = "SOC Model", info = Build.SOC_MODEL)
            InfoCard(title = "ODM SKU", info = Build.ODM_SKU)
        }
        InfoCard(title = "Product", info = Build.PRODUCT)
        InfoCard(title = "Radio", info = Build.getRadioVersion())
        InfoCard(title = "Tags", info = Build.TAGS)
        InfoCard(title = "Time", info = "${Build.TIME}")
        InfoCard(title = "Type", info = Build.TYPE)
        InfoCard(title = "User", info = Build.USER)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Build.getFingerprintedPartitions().map {
                InfoCard(title = "Finger Partition - name", info = "${it.name}")
                InfoCard(title = "Finger Partition - fingerprint", info = "${it.fingerprint}")
                InfoCard(
                    title = "Finger Partition - buildTimeMillis",
                    info = "${it.buildTimeMillis}"
                )
            }
        }
        InfoCard(title = "Build Partition Sys Name", info = Build.Partition.PARTITION_NAME_SYSTEM)

        Text(
            text = "Versions",
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
        )
        InfoCard(title = "API Version", info = "${Build.VERSION.SDK_INT}")
        InfoCard(title = "Base OS", info = "${Build.VERSION.BASE_OS}")
        InfoCard(title = "Codename", info = "${Build.VERSION.CODENAME}")
        InfoCard(title = "Security Patch", info = "${Build.VERSION.SECURITY_PATCH}")
        InfoCard(title = "Incremental", info = "${Build.VERSION.INCREMENTAL}")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            InfoCard(
                title = "Media Performance Class",
                info = "${Build.VERSION.MEDIA_PERFORMANCE_CLASS}"
            )
        }
        InfoCard(title = "Preview SDK Int", info = "${Build.VERSION.PREVIEW_SDK_INT}")

    }

}