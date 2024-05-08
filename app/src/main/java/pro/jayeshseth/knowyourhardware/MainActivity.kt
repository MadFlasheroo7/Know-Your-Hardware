package pro.jayeshseth.knowyourhardware

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import pro.jayeshseth.knowyourhardware.features.gps.ForegroundLocationService
import pro.jayeshseth.knowyourhardware.features.gps.LocationScreenViewModel
import pro.jayeshseth.knowyourhardware.features.gps.LocationScreenViewModelFactory
import pro.jayeshseth.knowyourhardware.features.gps.broadcastReceiver.GpsManager
import pro.jayeshseth.knowyourhardware.ui.theme.BroadcastReceiversTheme

class MainActivity : ComponentActivity() {
    private val gpsManager = GpsManager()
    override fun attachBaseContext(newBase: Context?) {
        val configuration = Configuration(newBase?.resources?.configuration)
        configuration.fontScale = 1.0f
        applyOverrideConfiguration(configuration)
        super.attachBaseContext(newBase)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gpsManager.register(this)
        enableEdgeToEdge()

        val viewModel by viewModels<LocationScreenViewModel>(factoryProducer = {
            LocationScreenViewModelFactory(
                gpsManager = gpsManager
            )
        })
        setContent {
            BroadcastReceiversTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.surface
                ) {
                    BRNavHost(viewModel = viewModel, gpsManager = gpsManager)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val intent = Intent(this, ForegroundLocationService::class.java)
        gpsManager.unregister(this)
        stopService(intent)
    }
}