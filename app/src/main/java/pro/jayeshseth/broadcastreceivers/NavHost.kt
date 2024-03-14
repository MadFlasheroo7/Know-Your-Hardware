package pro.jayeshseth.broadcastreceivers

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import pro.jayeshseth.broadcastreceivers.broadcastReceivers.GpsManager
import pro.jayeshseth.broadcastreceivers.ui.screens.GpsInfoScreen
import pro.jayeshseth.broadcastreceivers.ui.screens.HomeScreen

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