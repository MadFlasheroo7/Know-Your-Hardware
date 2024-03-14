package pro.jayeshseth.broadcastreceivers

import android.content.res.Resources
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import pro.jayeshseth.broadcastreceivers.broadcastReceivers.GpsManager
import pro.jayeshseth.broadcastreceivers.ui.theme.BroadcastReceiversTheme

class MainActivity : ComponentActivity() {
    private val gpsManager = GpsManager()

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gpsManager.register(this)
        val isDark = Resources.getSystem().configuration.isNightModeActive
        Log.d("test", "$isDark")
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(
                lightScrim = Color.RED,
                darkScrim = Color.RED,
                detectDarkMode = { isDark }
            ),
            navigationBarStyle = SystemBarStyle.auto(
                lightScrim = Color.RED,
                darkScrim = Color.RED,
                detectDarkMode = { isDark }
            ),
        )
        setContent {
            BroadcastReceiversTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BRNavHost(gpsManager = gpsManager)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        gpsManager.unregister(this)
    }
}

/**
 * The default light scrim, as defined by androidx and the platform:
 * https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:activity/activity/src/main/java/androidx/activity/EdgeToEdge.kt;l=35-38;drc=27e7d52e8604a080133e8b842db10c89b4482598
 */
private val lightScrim = android.graphics.Color.argb(0xe6, 0xFF, 0xFF, 0xFF)

/**
 * The default dark scrim, as defined by androidx and the platform:
 * https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:activity/activity/src/main/java/androidx/activity/EdgeToEdge.kt;l=40-44;drc=27e7d52e8604a080133e8b842db10c89b4482598
 */
private val darkScrim = android.graphics.Color.argb(0x80, 0x1b, 0x1b, 0x1b)
