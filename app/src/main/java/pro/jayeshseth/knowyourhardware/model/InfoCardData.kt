package pro.jayeshseth.knowyourhardware.model

import androidx.compose.ui.text.AnnotatedString

data class InfoCardData(
    val title: String,
    val info: String,
    val additionalInfo: AdditionalInfo? = null,
    val enabled: Boolean = additionalInfo != null,
    val minSdk: Int? = null
) {
    data class AdditionalInfo(
        val primaryInfo: String,
        val secondaryInfo: AnnotatedString? = null,
        val source: String
    )
}
