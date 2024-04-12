package pro.jayeshseth.knowyourhardware.ui.composables

import androidx.compose.runtime.Composable
import pro.jayeshseth.knowyourhardware.model.InfoCardData

@Composable
fun InfoCardMapper(info: InfoCardData) {
    InfoCard(
        title = info.title,
        info = info.info,
        enabled = info.enabled,
        minSdk = info.minSdk,
        additionalInfoContent = {
            if (info.additionalInfo != null) {
                AdditionalInfoCard(
                    primaryInfo = info.additionalInfo.primaryInfo,
                    secondaryInfo = info.additionalInfo.secondaryInfo,
                    source = info.additionalInfo.source
                )
            }
        }
    )
}