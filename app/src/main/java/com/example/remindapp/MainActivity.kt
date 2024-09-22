package com.example.remindapp

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateUtils
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerFormatter
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.remindapp.ui.theme.RemindAppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.Calendar

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RemindAppTheme {
                RemindApp()
            }
        }
    }
}

@Composable
fun RemindApp() {
    //Stores the current text in the OutlinedTextField
    var text = remember { mutableStateOf("") }
    //Saves the current text in the OutlinedTextField when a reminder is created
    var textReminder = remember { mutableStateOf("") }
    //Stores the selected date for the reminder
    var selectedDate = remember { mutableStateOf("Select a date") }
    //Boolean to open and close the DatePickerDialog
    var showDatePickerDialog = remember { mutableStateOf(false) }
    //Stores the selected time for the reminder
    var selectedTime = remember { mutableStateOf("Select a time") }
    //Boolean to open and close the TimePickerDialog
    var showTimePickerDialog = remember { mutableStateOf(false) }
    //Stores the total text displayed for the reminder
    var reminderText = remember { mutableStateOf("") }
    //Establishes a host for the Snackbar
    val snackbarHostState = remember { SnackbarHostState() }
    //Establishes a coroutine scope for the Snackbar
    val coroutineScope = rememberCoroutineScope()


    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        content = { padding ->
            Column() {
                //Text field for the reminder
                OutlinedTextField(
                    value = text.value,
                    onValueChange = { text.value = it },
                    modifier = Modifier
                        .padding(24.dp).fillMaxWidth(),
                )

                //Date Picker for the reminder
                DatePicker(selectedDate, showDatePickerDialog)

                //Time Picker for the reminder
                TimePicker(selectedTime, showTimePickerDialog)

                //Internal Row to show the set and clear button on the same row
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth().padding(8.dp)
                ) {
                    //Button to set the reminder
                        Button(
                            onClick = {
                                reminderText.value =
                                    "Reminder set for " + selectedTime.value + " @ " + selectedDate.value
                                coroutineScope.launch { // Launch a coroutine to show the Snackbar
                                    snackbarHostState.showSnackbar("New reminder set successfully!")
                                }
                                textReminder.value = text.value
                                text.value = ""
                            },
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Text(text = "Set the reminder!")
                        }
                        //Button to clear the reminder
                        Button(
                            onClick = {
                                reminderText.value = ""
                                selectedDate.value = "Select a date"
                                showDatePickerDialog.value = false
                                selectedTime.value = "Select a time"
                                showTimePickerDialog.value = false
                                textReminder.value = ""
                                text.value = ""
                                coroutineScope.launch { // Launch a coroutine to show the Snackbar
                                    snackbarHostState.showSnackbar("Reminder cleared successfully!")
                                }
                            },
                            modifier = Modifier.padding(8.dp),

                            ) {
                            Text(text = "Clear the reminder!")
                        }
                    }
                //Text for the reminder
                Text(text = reminderText.value)
                Text(text = "    " + textReminder.value)
            }
        }
    )
}

//Composable for the Date Picker
@Composable
fun DatePicker(selectedDate: MutableState<String>, showDatePickerDialog: MutableState<Boolean>) {
    if (showDatePickerDialog.value) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(
            LocalContext.current,
            { _, selectedYear, selectedMonth, selectedDay ->
                selectedDate.value = "${selectedMonth + 1}/$selectedDay/$selectedYear"
                showDatePickerDialog.value = false
            }, year, month, day
        ).show()
    }

    Button(
        onClick = { showDatePickerDialog.value = true },
        modifier = Modifier.fillMaxWidth()) {
            Text(text = selectedDate.value)
        }
}

//Composable for the Time Picker
@Composable
fun TimePicker(selectedTime: MutableState<String>, showTimePickerDialog: MutableState<Boolean>) {
    // Time Picker Button
    Button(
        onClick = { showTimePickerDialog.value = true },
        modifier = Modifier.fillMaxWidth()) {
            Text(text = selectedTime.value)
        }

    // Time Picker Dialog
    if (showTimePickerDialog.value) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        TimePickerDialog(
            LocalContext.current,
            { _, selectedHour, selectedMinute ->
                selectedTime.value = String.format("%02d:%02d", selectedHour, selectedMinute)
                showTimePickerDialog.value = false
            }, hour, minute,
            true // Use 24-hour format
        ).show()
    }
}

