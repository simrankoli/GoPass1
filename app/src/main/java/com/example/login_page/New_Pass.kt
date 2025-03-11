package com.example.login_page

import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.compose.runtime.Composable
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


@Composable
fun BusPassForm(navController: NavController) {
    var name by remember { mutableStateOf("") }
    var studentId by remember { mutableStateOf("") }
    var contact by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var pickupLocation by remember { mutableStateOf("") }
    var dropLocation by remember { mutableStateOf("") }
    var paymentMode by remember { mutableStateOf("Cash") }
    val paymentOptions = listOf("Cash", "UPI", "Credit Card", "Debit Card")

    // Validation error states
    var nameError by remember { mutableStateOf<String?>(null) }
    var studentIdError by remember { mutableStateOf<String?>(null) }
    var contactError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(36.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Apply for New Bus Pass", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(24.dp))

        CustomTextField(value = name, label = "Full Name", error = nameError) { name = it }
        CustomTextField(value = studentId, label = "Student/Employee ID", error = studentIdError) { studentId = it }
        CustomTextField(value = contact, label = "Contact Number", keyboardType = KeyboardType.Phone, error = contactError) { contact = it }
        CustomTextField(value = email, label = "Email Address", keyboardType = KeyboardType.Email, error = emailError) { email = it }
        CustomTextField(value = pickupLocation, label = "Pickup Location") { pickupLocation = it }
        CustomTextField(value = dropLocation, label = "Drop-off Location") { dropLocation = it }

        Spacer(modifier = Modifier.height(8.dp))

        Text("Payment Mode", style = MaterialTheme.typography.bodyLarge)
        DropdownMenuBox(selectedOption = paymentMode, options = paymentOptions) { paymentMode = it }


        Spacer(modifier = Modifier.height(16.dp))



        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = {
                    val isValid = validateInput(
                        name, studentId, contact, email,
                        onNameError = { nameError = it },
                        onStudentIdError = { studentIdError = it },
                        onContactError = { contactError = it },
                        onEmailError = { emailError = it }
                    )

                    if (isValid) {
                        navController.navigate(
                            "busPassDetails/$name/$studentId/$contact/$email/$pickupLocation/$dropLocation/$paymentMode"
                        )
                    }
                },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Submit")
            }
        }

    }
}

@Composable
fun CustomTextField(
    value: String,
    label: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    error: String? = null,
    onValueChange: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            isError = error != null, // Show error state
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 4.dp)
        )
        if (error != null) {
            Text(error, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenuBox(selectedOption: String, options: List<String>, onOptionSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            readOnly = true,
            label = { Text("Select Payment Mode") },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
        )

        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

// Validation Function
fun validateInput(
    name: String,
    studentId: String,
    contact: String,
    email: String,
    onNameError: (String?) -> Unit,
    onStudentIdError: (String?) -> Unit,
    onContactError: (String?) -> Unit,
    onEmailError: (String?) -> Unit
): Boolean {
    var isValid = true

    if (name.isBlank()) {
        onNameError("Full Name is required")
        isValid = false
    } else {
        onNameError(null)
    }

    if (studentId.isBlank()) {
        onStudentIdError("Student/Employee ID is required")
        isValid = false
    } else {
        onStudentIdError(null)
    }

    if (contact.isBlank() || !contact.all { it.isDigit() } || contact.length < 10) {
        onContactError("Enter a valid 10-digit contact number")
        isValid = false
    } else {
        onContactError(null)
    }

    if (email.isBlank() || !email.contains("@") || !email.contains(".")) {
        onEmailError("Enter a valid email address")
        isValid = false
    } else {
        onEmailError(null)
    }

    return isValid
}

@Composable
fun BusPassDetails(
    name: String,
    studentId: String,
    contact: String,
    email: String,
    pickupLocation: String,
    dropLocation: String,
    paymentMode: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Your Bus Pass",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .border(2.dp, MaterialTheme.colorScheme.primary, MaterialTheme.shapes.medium), // Adding a border
            elevation = CardDefaults.cardElevation(6.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                DetailRow(label = "Full Name", value = name)
                DetailRow(label = "Student/Employee ID", value = studentId)
                DetailRow(label = "Contact Number", value = contact)
                DetailRow(label = "Email Address", value = email)
                DetailRow(label = "Pickup Location", value = pickupLocation)
                DetailRow(label = "Drop-off Location", value = dropLocation)
                DetailRow(label = "Payment Mode", value = paymentMode)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = { /* Add PDF generation or Print feature */ }) {
            Text("Download Pass")
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "$label:",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
        Text(text = value, style = MaterialTheme.typography.bodyLarge)
    }
}
