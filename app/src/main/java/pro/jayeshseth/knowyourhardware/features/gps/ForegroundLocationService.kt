package pro.jayeshseth.knowyourhardware.features.gps

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.location.Location
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ForegroundLocationService : Service() {

    companion object {
        val liveLocation = MutableStateFlow(Location(""))
        val isServiceRunning = MutableStateFlow(false)

        const val START_TRACKING = "START_TRACKING"
        const val STOP_TRACKING = "STOP_TRACKING"
        private const val NOTIFICATION_CHANNEL = "location_tracking_channel"
        private const val SERVICE_ID = 1
    }
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private lateinit var locationRepository: LocationRepository

    override fun onCreate() {
        super.onCreate()
        val fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(applicationContext)
        locationRepository = LocationRepositoryImpl(
            fusedLocationClient = fusedLocationClient,
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            START_TRACKING -> startForegroundTracking()
            STOP_TRACKING -> stopForegroundTracking()
        }

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        stopSelf()
        scope.cancel()
        Log.d("service", "service destroyed")
    }

    @SuppressLint("MissingPermission")
    private fun startForegroundTracking() {
        createNotificationChannel()
        Log.d("service", "service started")
        val notification = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL)
            .setContentTitle("Live Location")
            .setContentText("Tracking Location in Foreground")
            .setSmallIcon(applicationInfo.icon)
            .setOngoing(true)
        val dateTimeFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        locationRepository.getForegroundLocation()
            .onEach {
                val time =
                    dateTimeFormat.format(Date(it.time))
                liveLocation.value = it
                val updateNotification = notification.setContentText(
                    "Latitude: ${it.latitude}\n Longitude: ${it.longitude}\n Time: $time"
                ).setStyle(NotificationCompat.BigTextStyle())
                NotificationManagerCompat.from(this).notify(SERVICE_ID, updateNotification.build())
            }
            .launchIn(scope)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(
                SERVICE_ID,
                notification.build(),
                ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION
            )
        } else {
            startForeground(SERVICE_ID, notification.build())
        }
    }

    private fun stopForegroundTracking() {
        isServiceRunning.value = false
        scope.cancel()
        stopSelf()
        Log.d("service", "service stopped")
    }

    private fun createNotificationChannel() {
        val notificationChannel = NotificationChannelCompat.Builder(
            NOTIFICATION_CHANNEL,
            NotificationManagerCompat.IMPORTANCE_HIGH
        )
            .setName("Foreground Location Tracking")
            .setDescription("Channel for foreground location tracking")
            .build()
        NotificationManagerCompat.from(this).createNotificationChannel(notificationChannel)
    }
}