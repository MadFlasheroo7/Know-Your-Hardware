package pro.jayeshseth.knowyourhardware.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle

fun additionalInfo(
    info: String,
    usage: String? = null,
) : AnnotatedString {
    return buildAnnotatedString {
        append(info)
        withStyle(
            style = SpanStyle(
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic
            )
        ) {
            append("\n$usage")
        }
    }
}