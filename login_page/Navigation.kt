package kotlinprogram.examplelearning.login_page

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

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
        composable("busPassDetails/{name}/{studentId}/{contact}/{email}/{course}/{pickupLocation}/{dropLocation}/{paymentMode}/{academicYear}/{passDuration}/{imageUriString}") { backStackEntry ->

      //  composable("busPassDetails/{name}/{studentId}/{contact}/{email}/{course}/{pickupLocation}/{dropLocation}/{paymentMode}/{academicYear}/{passDuration}/{imageUriString}") { backStackEntry ->
            BusPassDetails(
                name = backStackEntry.arguments?.getString("name") ?: "",
                studentId = backStackEntry.arguments?.getString("studentId") ?: "",
                contact = backStackEntry.arguments?.getString("contact") ?: "",
                email = backStackEntry.arguments?.getString("email") ?: "",
                course = backStackEntry.arguments?.getString("course") ?: "",
                pickupLocation = backStackEntry.arguments?.getString("pickupLocation") ?: "",
                dropLocation = backStackEntry.arguments?.getString("dropLocation") ?: "",
                paymentMode = backStackEntry.arguments?.getString("paymentMode") ?: "",
                academicYear = backStackEntry.arguments?.getString("academicYear") ?: "",
                passDuration = backStackEntry.arguments?.getString("passDuration") ?: "",
                imageUriString = backStackEntry.arguments?.getString("imageUriString")?.takeIf { it != "null" }
            )
        }
    }
}
