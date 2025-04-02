package com.example.login_page


import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch

@Composable
fun RenewPassScreen(navController: androidx.navigation.NavController, viewModel: BusPassViewModel = viewModel()) {
    val passDetails by viewModel.passDetails.collectAsState()
    val renewalOptions = listOf("1 Month", "3 Months", "6 Months")
    var selectedOption by remember { mutableStateOf(renewalOptions[0]) }
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Renew Bus Pass", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(8.dp))

        if (passDetails != null) {
            Text(text = "Current Pass Expiry: ${passDetails?.expiryDate}")
            Spacer(modifier = Modifier.height(8.dp))

            renewalOptions.forEach { option ->
                Row(modifier = Modifier.padding(vertical = 4.dp)) {
                    RadioButton(
                        selected = (selectedOption == option),
                        onClick = { selectedOption = option }
                    )
                    Text(text = option, modifier = Modifier.padding(start = 8.dp))
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                coroutineScope.launch {
                    val success = viewModel.renewPass(selectedOption)
                    if (success) {
                        Toast.makeText(
                            viewModel.context, "Pass renewed successfully!", Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Toast.makeText(
                            viewModel.context, "Renewal failed. Try again!", Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }) {
                Text("Proceed to Payment")
            }
        } else {
            Text("No active pass found.")
        }
    }
}
