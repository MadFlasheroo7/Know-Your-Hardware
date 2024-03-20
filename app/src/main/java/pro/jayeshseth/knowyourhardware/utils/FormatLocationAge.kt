package pro.jayeshseth.knowyourhardware.utils

fun formatLocationAge(ageInMilliseconds: Long): String {
    val seconds = ageInMilliseconds / 1000
    val minutes = seconds / 60

    return if (minutes < 1) {
        "$ageInMilliseconds ms (very recent)"
    } else if (minutes < 60) {
        "$minutes minute(s) ago"
    } else {
        val hours = minutes / 60
        "$hours hour(s) ago"
    }
}