package pro.jayeshseth.knowyourhardware

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import pro.jayeshseth.knowyourhardware.broadcastReceivers.GpsManager
import pro.jayeshseth.knowyourhardware.ui.screens.GpsInfoScreen
import pro.jayeshseth.knowyourhardware.ui.screens.HomeScreen

const val HOME_ROUTE = "home"
const val GPS_ROUTE = "gps_route"


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
                navToGpsScreen = { navController.navigate(GPS_ROUTE) }
            )
        }
        composable(
            route = GPS_ROUTE,
        ) {
            GpsInfoScreen(gpsManager = gpsManager)
        }
    }
}