package pro.jayeshseth.knowyourhardware

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import pro.jayeshseth.knowyourhardware.broadcastReceivers.GpsManager
import pro.jayeshseth.knowyourhardware.ui.screens.GPSScreen
import pro.jayeshseth.knowyourhardware.ui.screens.GpsInfoScreen
import pro.jayeshseth.knowyourhardware.ui.screens.HomeScreen
import pro.jayeshseth.knowyourhardware.ui.screens.LocationScreen

const val HOME_ROUTE = "home"
const val GPS_SCREEN_ROUTE = "gps_route"
const val GPS_INFO_SCREEN_ROUTE = "gps_info_route"
const val LOCATION_SCREEN_ROUTE = "location_route"


@Composable
fun BRNavHost(
    gpsManager: GpsManager,
) {
    val navController = rememberNavController()
    NavHost(
        modifier = Modifier,
        navController = navController,
        startDestination = HOME_ROUTE
    ) {
        composable(
            route = HOME_ROUTE,
        ) {
            HomeScreen(
                navToGpsScreen = { navController.navigate(GPS_SCREEN_ROUTE) }
            )
        }
        composable(
            route = GPS_SCREEN_ROUTE,
        ) {
            GPSScreen(
                navToLocationScreen = { navController.navigate(LOCATION_SCREEN_ROUTE) },
                navToGpsInfoScreen = { navController.navigate(GPS_INFO_SCREEN_ROUTE) }
            )
//            GPSDataScreen()
//            GpsInfoScreen(gpsManager = gpsManager)
        }
        composable(
            route = GPS_INFO_SCREEN_ROUTE
        ) {
            GpsInfoScreen(gpsManager = gpsManager)
        }
        composable(
            route = LOCATION_SCREEN_ROUTE
        ) {
            LocationScreen()
        }
    }
}