package kotlinprogram.examplelearning.login_page

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


@Composable
fun BusPassForm(navController: NavController) {
    var name by remember { mutableStateOf("") }
    var studentId by remember { mutableStateOf("") }
    var contact by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var course by remember { mutableStateOf("Engineering") } // New course state
    var city by remember { mutableStateOf("") }
    var pickupLocation by remember { mutableStateOf("") }
    var dropLocation by remember { mutableStateOf("") }
    var paymentMode by remember { mutableStateOf("Cash") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var academicYear by remember { mutableStateOf("2024-25") }
    var passDuration by remember { mutableStateOf("6 months") }
    var feeMessage by remember { mutableStateOf("") }

    val courseOptions = listOf("Engineering", "Diploma", "MBA", "MTech") // New dropdown options
    val cityOptions = listOf("Jalgaon", "Bhusawal")
    val academicYearOptions = (2024..2035).map { "$it-${(it + 1) % 100}" }
    val passDurationOptions = listOf("6 months", "1 year")
    val pickupDropOptions = when (city) {
        "Jalgaon" -> listOf("Khote Nagar", "Mahabal", "DSP Chowk", "Sagar Park", "Akashwani Chowk","Icchadevi Chowk","Ajanta Chowk "," kalinka Mata Mandir ","Khedi")
        "Bhusawal" -> listOf("Nahata Chowk", "Khadka Chowk", "Fekari Fata", "Deepnagar", "Anand Nagar","Ashtabhuja Mandir","Pandurang Talkies", "Gandhi Putala", "Satara Chowk", "Lonari Samaj Hall ", "Sakegaon"," Patil Nursery", " Godavari Medical College ","Jalgaon Kh.", "Nashirabad")
        else -> emptyList()
    }
    val paymentOptions = listOf("Cash", "UPI", "Credit Card", "Debit Card")
    val scrollState = rememberScrollState()
    val imagePickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        imageUri = uri
    }

    LaunchedEffect(city, passDuration) {
        feeMessage = when (city) {
            "Jalgaon" -> if (passDuration == "6 months") "You need to pay 4000 for 6 month pass" else "You need to pay 8000 for 1 year pass"
            "Bhusawal" -> if (passDuration == "6 months") "You need to pay 6000 for 6 month pass" else "You need to pay 12000 for 1 year pass"
            else -> ""
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(36.dp).verticalScroll(scrollState),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Apply for New Bus Pass", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))

        CustomTextField(name, "Full Name") { name = it }
        CustomTextField(studentId, "Student/Employee ID") { studentId = it }
        CustomTextField(contact, "Contact Number", KeyboardType.Phone) { contact = it }
        CustomTextField(email, "Email Address", KeyboardType.Email) { email = it }
        CustomDropdown("Select Course", course, courseOptions) { course = it }
        CustomDropdown("Select City", city, cityOptions) { city = it }

        if (city.isNotEmpty()) {
            CustomDropdown("Pickup Location", pickupLocation, pickupDropOptions) { pickupLocation = it }
            CustomDropdown("Drop-off Location", dropLocation, pickupDropOptions) { dropLocation = it }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { imagePickerLauncher.launch("image/*") }) { Text("Upload Image") }

        imageUri?.let {
            Image(
                painter = rememberAsyncImagePainter(it),
                contentDescription = "Uploaded Image",
                modifier = Modifier.size(120.dp).border(1.dp, MaterialTheme.colorScheme.primary)
            )
        }

        CustomDropdown("Select Academic Year", academicYear, academicYearOptions) { academicYear = it }
        CustomDropdown("Select Pass Duration", passDuration, passDurationOptions) { passDuration = it }

        if (feeMessage.isNotEmpty()) {
            Text(feeMessage, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(top = 8.dp))
        }

        CustomDropdown("Select Payment Mode", paymentMode, paymentOptions) { paymentMode = it }
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            if (city.isEmpty()) return@Button
            val encodedImageUri = Uri.encode(imageUri?.toString() ?: "null")
            navController.navigate("busPassDetails/$name/$studentId/$contact/$email/$course/$pickupLocation/$dropLocation/$paymentMode/$academicYear/$passDuration/$encodedImageUri")
        }) {
            Text("Submit")
        }
    }
}


@Composable
fun BusPassDetails(
    name: String,
    studentId: String,
    contact: String,
    email: String,
    course: String,
    pickupLocation: String,
    dropLocation: String,
    paymentMode: String,
    academicYear: String,
    passDuration: String,
    imageUriString: String?
) {
    val imageUri = imageUriString?.takeIf { it != "null" }?.let { Uri.parse(it) }
    val scrollState = rememberScrollState()
    val context = LocalContext.current // Get the current context

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp).verticalScroll(scrollState),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Your Bus Pass", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(bottom = 16.dp))

        Card(
            modifier = Modifier.fillMaxWidth().padding(8.dp).border(2.dp, MaterialTheme.colorScheme.primary),
            elevation = CardDefaults.cardElevation(6.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                imageUri?.let {
                    Image(
                        alignment = Alignment.Center,
                        painter = rememberAsyncImagePainter(it),
                        contentDescription = "Uploaded Image",
                        modifier = Modifier.size(150.dp).border(2.dp, MaterialTheme.colorScheme.primary)
                    )
                }
                DetailRow("Full Name", name)
                DetailRow("Student/Employee ID", studentId)
                DetailRow("Contact Number", contact)
                DetailRow("Email Address", email)
                DetailRow("Course", course)
                DetailRow("Pickup Location", pickupLocation)
                DetailRow("Drop-off Location", dropLocation)
                DetailRow("Payment Mode", paymentMode)
                DetailRow("Academic Year", academicYear)
                DetailRow("Pass Duration", passDuration)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = {
            generateBusPassPDF(
                context, name, studentId, contact, email, course, pickupLocation, dropLocation, paymentMode, academicYear, passDuration, imageUri
            )

        }) {
            Text("Download Pass")
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text("$label:", fontWeight = FontWeight.Bold)
        Text(value)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDropdown(label: String, selectedOption: String, options: List<String>, onOptionSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Column(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
        Text(label)
        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
            OutlinedTextField(
                value = selectedOption,
                onValueChange = {},
                readOnly = true,
                label = { Text(label) },
                modifier = Modifier.fillMaxWidth().menuAnchor() // ERROR
            )

            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                options.forEach { option ->
                    DropdownMenuItem(text = { Text(option) }, onClick = { onOptionSelected(option); expanded = false })
                }
            }
        }
    }
}

@Composable
fun CustomTextField(value: String, label: String, keyboardType: KeyboardType = KeyboardType.Text, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp)
    )
}



fun generateBusPassPDF(
    context: Context,
    name: String,
    studentId: String,
    contact: String,
    email: String,
    course: String,
    pickupLocation: String,
    dropLocation: String,
    paymentMode: String,
    academicYear: String,
    passDuration: String,
    imageUri: Uri?
) {
    val pdfDocument = PdfDocument()
    val pageInfo = PdfDocument.PageInfo.Builder(300, 500, 1).create()
    val page = pdfDocument.startPage(pageInfo)
    val canvas = page.canvas
    val paint = Paint()
    paint.textSize = 14f

    // Card Border
    val borderPaint = Paint()
    borderPaint.style = Paint.Style.STROKE
    borderPaint.strokeWidth = 2f
    canvas.drawRect(20f, 20f, 280f, 480f, borderPaint)

    // Draw Image
    imageUri?.let {
        val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, it)
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, false)
        canvas.drawBitmap(scaledBitmap, 100f, 30f, null)
    }

    // Text formatting
    val textX = 30f
    var yPosition = 150f

    val details = listOf(
        "Full Name:" to name,
        "Student ID:" to studentId,
        "Contact:" to contact,
        "Email:" to email,
        "Course:" to course,
        "Pickup Location:" to pickupLocation,
        "Drop-off Location:" to dropLocation,
        "Payment Mode:" to paymentMode,
        "Academic Year:" to academicYear,
        "Pass Duration:" to passDuration
    )

    for ((label, value) in details) {
        paint.isFakeBoldText = true
        canvas.drawText(label, textX, yPosition, paint)
        paint.isFakeBoldText = false
        canvas.drawText(value, textX + 120f, yPosition, paint)
        yPosition += 30f
    }

    pdfDocument.finishPage(page)

    val directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
    val file = File(directory, "BusPass_$studentId.pdf")

    try {
        val fos = FileOutputStream(file)
        pdfDocument.writeTo(fos)
        pdfDocument.close()
        fos.close()
        Toast.makeText(context, "PDF Saved to Downloads", Toast.LENGTH_LONG).show()

        // Open PDF in viewer
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/pdf")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        context.startActivity(Intent.createChooser(intent, "Open PDF"))

    } catch (e: IOException) {
        e.printStackTrace()
        Toast.makeText(context, "Error saving PDF", Toast.LENGTH_LONG).show()
    }
}
