package pro.jayeshseth.knowyourhardware.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import pro.jayeshseth.knowyourhardware.ui.composables.InteractiveButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navToGpsScreen: () -> Unit,
    navToDeviceInfoScreen: () -> Unit,
    navToCameraScreen: () -> Unit,
    navToAboutScreen: () -> Unit,

) {
    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Know Your Hardware",
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent
                ),
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .verticalScroll(rememberScrollState())
                .navigationBarsPadding()
        ) {
            InteractiveButton(text = "GPS", onClick = navToGpsScreen)
//            InteractiveButton(text = "Camera", onClick = navToGpsScreen)
            InteractiveButton(text = "Device Info", onClick = navToDeviceInfoScreen)
            InteractiveButton(text = "About", onClick = navToDeviceInfoScreen)
        }
    }
}