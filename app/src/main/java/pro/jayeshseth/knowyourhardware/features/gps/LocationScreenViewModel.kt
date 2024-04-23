package pro.jayeshseth.knowyourhardware.features.gps

import android.content.Context
import android.location.Location
import android.util.Log
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import pro.jayeshseth.knowyourhardware.features.gps.broadcastReceiver.GpsManager
import pro.jayeshseth.knowyourhardware.utils.isAbove_TIRAMISU
import java.util.concurrent.TimeUnit

class LocationScreenViewModel(
    gpsManager: GpsManager
) : ViewModel() {

    val allProviders = mutableStateOf(gpsManager.allProviders.value)
    val lastKnownLocation = mutableStateOf(Location(""))
    val liveLocation = mutableStateOf(Location(""))
    val trackLiveLocation = mutableStateOf(false)
    val showInMinutes = mutableStateOf(false)
    val showInSeconds = mutableStateOf(false)
    val speedInKms = mutableStateOf(false)
    val timeInterval = mutableLongStateOf(TimeUnit.SECONDS.toMillis(1))
    val selectedProvider = mutableStateOf("FusedGMS")

    fun updateLiveLocation(location: Location) {
        liveLocation.value = location
    }
    override fun onCleared() {
        super.onCleared()
        trackLiveLocation.value = false
    }
}

@Suppress("UNCHECKED_CAST")
class LocationScreenViewModelFactory(
    private val gpsManager: GpsManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LocationScreenViewModel::class.java)) {
            return LocationScreenViewModel(
                gpsManager = gpsManager
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}