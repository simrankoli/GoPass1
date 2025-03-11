
package com.example.login_page

import androidx.compose.runtime.Composable
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.login_page.R


@Composable
fun HomePage(navController: NavController) {

    Column(
        modifier = Modifier
            .fillMaxSize().padding(top = 50.dp),

        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Text(text = "GoPass", fontSize = 28.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(16.dp))
        Image(
            painter = painterResource(id = R.drawable.img),
            contentDescription = "Rounded Image",
            modifier = Modifier.fillMaxWidth() // Makes image take full width
                .aspectRatio(16f / 9f)
                .clip(RoundedCornerShape(16.dp)) // Apply rounded corners
        )

        Spacer(modifier = Modifier.height(64.dp))
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                CustomButton(text = "New Pass"){ navController.navigate("New_Pass") }
                CustomButton(text = "Renew Pass"){ navController.navigate("renew_pass") }
            }

            Spacer(modifier = Modifier.height(16.dp)) // Space between rows

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                CustomButton(text = "Payment"){ navController.navigate("payment") }
                CustomButton(text = "Location"){ navController.navigate("location") }
            }
        }
    }
}


@Composable
fun CustomButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick, // Call onClick here
        modifier = Modifier
            .size(width = 150.dp, height = 80.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)

    ) {
        Text(text = text, fontSize = 16.sp)
    }
}
@Composable
fun PlaceholderScreen(title: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = title, fontSize = 24.sp, fontWeight = FontWeight.Bold)
    }
}
@Composable
fun MainScreen(navController: NavHostController) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    val tabRoutes = listOf("home", "new_pass", "renew_pass", "payment", "profile")
    val selectedTab = tabRoutes.indexOf(currentRoute).takeIf { it >= 0 } ?: 0

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomNavigationBar(selectedTab) { index ->
                val destination = tabRoutes[index]
                navController.navigate(destination) {
                    popUpTo("home") { inclusive = false }
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("home") { HomePage(navController) }
            composable("new_pass") { PlaceholderScreen("Apply for New Pass") }
            composable("renew_pass") { PlaceholderScreen("Renew Pass") }
            composable("payment") { PlaceholderScreen("Payment") }
            composable("profile") { PlaceholderScreen("Profile") }
        }
    }
}


@Composable
fun BottomNavigationBar(selectedTab: Int, onTabSelected: (Int) -> Unit) {
    NavigationBar {
        val items = listOf(
            "Home" to Icons.Filled.Home,
            "New Pass" to Icons.Filled.AddCircle,
            "Renew Pass" to Icons.Filled.Refresh,
            "Payment" to Icons.Filled.CreditCard,
            "Profile" to Icons.Filled.AccountCircle
        )

        items.forEachIndexed { index, (title, icon) ->
            NavigationBarItem(
                selected = selectedTab == index,
                onClick = { onTabSelected(index) }, // ✅ Updates `selectedTab`
                label = { Text(title) },
                icon = { Icon(imageVector = icon, contentDescription = title) }
            )
        }
    }
}









