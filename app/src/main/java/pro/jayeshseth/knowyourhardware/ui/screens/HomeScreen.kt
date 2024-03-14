package pro.jayeshseth.knowyourhardware.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
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
) {
    Column {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = "Broadcast Receiver in Compose",
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = Color.Transparent
            ),
        )
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .navigationBarsPadding()
        ) {
            InteractiveButton(text = "Location", onClick = navToGpsScreen)
        }
    }
}