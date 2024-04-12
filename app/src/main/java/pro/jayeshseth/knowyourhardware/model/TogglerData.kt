package pro.jayeshseth.knowyourhardware.model

data class TogglerData(
    val title: String,
    val checked: Boolean,
    val onCheckedChanged: (Boolean) -> Unit,
    val enabled: Boolean,
    val icon: Int
)