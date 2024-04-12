package pro.jayeshseth.knowyourhardware

import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import pro.jayeshseth.knowyourhardware.features.gps.broadcastReceiver.GpsManager
import pro.jayeshseth.knowyourhardware.features.deviceInfo.screens.DeviceInfoScreen
import pro.jayeshseth.knowyourhardware.features.gps.screens.GPSScreen
import pro.jayeshseth.knowyourhardware.features.gps.screens.GpsInfoScreen
import pro.jayeshseth.knowyourhardware.ui.screens.HomeScreen
import pro.jayeshseth.knowyourhardware.features.gps.screens.LocationScreen

const val HOME_ROUTE = "home"
const val GPS_SCREEN_ROUTE = "gps_route"
const val GPS_INFO_SCREEN_ROUTE = "gps_info_route"
const val LOCATION_SCREEN_ROUTE = "location_route"
const val DEVICE_INFO_SCREEN_ROUTE = "device_info_route"


@Composable
fun BRNavHost(
    gpsManager: GpsManager,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = HOME_ROUTE
    ) {
        composable(
            route = HOME_ROUTE,
        ) {
            HomeScreen(
                navToGpsScreen = { navController.navigate(GPS_SCREEN_ROUTE) },
                navToDeviceInfoScreen = { navController.navigate(DEVICE_INFO_SCREEN_ROUTE) }
            )
        }
        composable(
            route = GPS_SCREEN_ROUTE,
        ) {
            GPSScreen(
                navToLocationScreen = { navController.navigate(LOCATION_SCREEN_ROUTE) },
                navToGpsInfoScreen = { navController.navigate(GPS_INFO_SCREEN_ROUTE) }
            )
        }
        composable(
            route = GPS_INFO_SCREEN_ROUTE
        ) {
            GpsInfoScreen(gpsManager = gpsManager)
        }
        composable(
            route = LOCATION_SCREEN_ROUTE
        ) {
            LocationScreen(gpsManager)
        }
        composable(
            route = DEVICE_INFO_SCREEN_ROUTE
        ) {
            DeviceInfoScreen()
        }
    }
}