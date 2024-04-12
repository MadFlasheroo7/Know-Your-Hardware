package pro.jayeshseth.knowyourhardware.model

import androidx.compose.ui.text.AnnotatedString

data class LocationInfoCardData(
    val title: String,
    val latitude: String,
    val longitude: String,
    val time: String? = null,
    val additionalInfo: AdditionalInfo? = null,
    val enabled: Boolean = additionalInfo != null
) {
    data class AdditionalInfo(
        val primaryInfo: String,
        val secondaryInfo: AnnotatedString? = null,
        val source: String
    )
}