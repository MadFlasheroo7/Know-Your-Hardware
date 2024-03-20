package pro.jayeshseth.knowyourhardware

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import pro.jayeshseth.knowyourhardware.broadcastReceivers.GpsManager
import pro.jayeshseth.knowyourhardware.ui.theme.BroadcastReceiversTheme

class MainActivity : ComponentActivity() {
    private val gpsManager = GpsManager()

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gpsManager.register(this)
        enableEdgeToEdge()
        setContent {
            BroadcastReceiversTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.surface
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