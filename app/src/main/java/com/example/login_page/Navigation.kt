package com.example.login_page

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.login_page.BusPassDetails
import com.example.login_page.BusPassForm
import com.example.login_page.HomePage
import com.example.login_page.LoginScreen



@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") { LoginScreen(navController) }
        composable("home") { HomePage(navController) }
        composable("main") { MainScreen(navController) }
        composable("new_pass") { BusPassForm(navController) }


        composable("busPassForm") {
            BusPassForm(navController)
        }
        composable("busPassDetails/{name}/{studentId}/{contact}/{email}/{pickup}/{drop}/{payment}") { backStackEntry ->
            BusPassDetails(
                name = backStackEntry.arguments?.getString("name") ?: "",
                studentId = backStackEntry.arguments?.getString("studentId") ?: "",
                contact = backStackEntry.arguments?.getString("contact") ?: "",
                email = backStackEntry.arguments?.getString("email") ?: "",
                pickupLocation = backStackEntry.arguments?.getString("pickup") ?: "",
                dropLocation = backStackEntry.arguments?.getString("drop") ?: "",
                paymentMode = backStackEntry.arguments?.getString("payment") ?: ""
            )
        }
    }
}