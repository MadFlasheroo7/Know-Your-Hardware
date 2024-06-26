package pro.jayeshseth.knowyourhardware.ui.screens

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import pro.jayeshseth.commoncomponents.HomeScaffold
import pro.jayeshseth.commoncomponents.InteractiveButton
import pro.jayeshseth.commoncomponents.StatusBarAwareThemedColumn

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navToGpsScreen: () -> Unit,
    navToDeviceInfoScreen: () -> Unit,
    navToCameraScreen: () -> Unit,
    navToAboutScreen: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val scrollState = rememberScrollState()
    HomeScaffold(
        topAppBarScrollBehavior = scrollBehavior,
        innerScrollState = scrollState,
        title = {
            Text(
                text = "Know Your Hardware",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
        }
    ) {
        StatusBarAwareThemedColumn(
            statusBarColor = Color.Transparent,
            modifier = Modifier
                .padding(it)
                .navigationBarsPadding()
        ) {
            InteractiveButton(text = "GPS", onClick = navToGpsScreen)
//            InteractiveButton(text = "Camera", onClick = navToGpsScreen)
            InteractiveButton(text = "Device Info", onClick = navToDeviceInfoScreen)
            repeat(100) {
                InteractiveButton(text = "About", onClick = navToDeviceInfoScreen)
            }
        }
    }
}