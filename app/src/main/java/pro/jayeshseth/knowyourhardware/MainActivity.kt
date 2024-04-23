package pro.jayeshseth.knowyourhardware

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.google.android.gms.location.LocationServices
import pro.jayeshseth.knowyourhardware.features.gps.ForegroundLocationService
import pro.jayeshseth.knowyourhardware.features.gps.LocationRepository
import pro.jayeshseth.knowyourhardware.features.gps.LocationRepositoryImpl
import pro.jayeshseth.knowyourhardware.features.gps.LocationScreenViewModel
import pro.jayeshseth.knowyourhardware.features.gps.LocationScreenViewModelFactory
import pro.jayeshseth.knowyourhardware.features.gps.broadcastReceiver.GpsManager
import pro.jayeshseth.knowyourhardware.ui.theme.BroadcastReceiversTheme

class MainActivity : ComponentActivity() {
    private val gpsManager = GpsManager()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gpsManager.register(this)
        enableEdgeToEdge()
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        val locationRepository: LocationRepository = LocationRepositoryImpl(
            fusedLocationClient
        )

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