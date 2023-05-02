package com.adamve.hwkeyinfo

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.adamve.hwkeyinfo.ui.security_key.SecurityKeyEditDestination
import com.adamve.hwkeyinfo.ui.security_key.SecurityKeyEditScreen
import com.adamve.hwkeyinfo.ui.security_key.SecurityKeyListDestination
import com.adamve.hwkeyinfo.ui.security_key.SecurityKeyListScreen
import com.adamve.hwkeyinfo.ui.service.ServiceEditDestination
import com.adamve.hwkeyinfo.ui.service.ServiceEditScreen
import com.adamve.hwkeyinfo.ui.service.ServiceListDestination
import com.adamve.hwkeyinfo.ui.service.ServiceListScreen

@Composable
fun HwKeyInfoApp() {
    val navController = rememberNavController()

    HwKeyInfoNavHost(
        navController = navController,
    )
}

@Composable
fun HwKeyInfoNavHost(
    navController: NavHostController,
) {
    NavHost(navController = navController, startDestination = SecurityKeyListDestination.route) {
        composable(route = SecurityKeyListDestination.route) {
            SecurityKeyListScreen(
                navigateToItemEntry = { navController.navigate(SecurityKeyEditDestination.addKeyRoute) },
                navigateToItemUpdate = { navController.navigate("${SecurityKeyEditDestination.route}/$it") },
                navigateToServiceList = { navController.navigate(ServiceListDestination.route) }
            )
        }

        composable(
            SecurityKeyEditDestination.routeWithArgs,
            arguments = listOf(navArgument(SecurityKeyEditDestination.securityKeyIdArg) {
                type = NavType.LongType
            })
        ) {
            SecurityKeyEditScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }

            )
        }

        composable(SecurityKeyEditDestination.addKeyRoute) {
            SecurityKeyEditScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }

        // Service
        composable(route = ServiceListDestination.route) {
            ServiceListScreen(
                navigateToItemEntry = { navController.navigate(ServiceEditDestination.addServiceRoute) },
                navigateToItemUpdate = { navController.navigate("${ServiceEditDestination.route}/$it") },
                navigateToSecurityKeyList = { navController.navigate(SecurityKeyListDestination.route) }
            )
        }

        composable(ServiceEditDestination.addServiceRoute) {
            ServiceEditScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() })
        }

        composable(
            ServiceEditDestination.routeWithArgs,
            arguments = listOf(navArgument(ServiceEditDestination.serviceIdArg) {
                type = NavType.LongType
            })
        ) {
            ServiceEditScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() })
        }
    }

}