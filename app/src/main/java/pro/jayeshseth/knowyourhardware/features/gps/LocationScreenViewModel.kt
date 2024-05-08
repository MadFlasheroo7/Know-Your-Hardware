package pro.jayeshseth.knowyourhardware.features.gps

import android.location.Location
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import pro.jayeshseth.knowyourhardware.features.gps.broadcastReceiver.GpsManager
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