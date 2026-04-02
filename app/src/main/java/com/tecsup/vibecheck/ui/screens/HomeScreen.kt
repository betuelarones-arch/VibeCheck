package com.tecsup.vibecheck.ui.screens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.tecsup.vibecheck.data.local.ReminderEntity
import com.tecsup.vibecheck.ui.viewmodel.ReminderViewModel
import java.util.*
import java.text.SimpleDateFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: ReminderViewModel) {
    val reminders by viewModel.reminders.collectAsState()
    val context = LocalContext.current

    var showDialog by remember { mutableStateOf(false) }
    var title by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }

    // Usamos "hh:mm a" para que TÚ veas si es AM o PM en el botón
    var selectedTimestamp by remember { mutableStateOf(System.currentTimeMillis()) }
    val sdf = SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault())

    Scaffold(
        topBar = { TopAppBar(title = { Text("VibeCheck ✅") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Agregar")
            }
        }
    ) { padding ->
        if (reminders.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = androidx.compose.ui.Alignment.Center) {
                Text("No hay tareas. ¡Añade una! 🚀")
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
                items(reminders) { reminder ->
                    ReminderItem(
                        reminder = reminder,
                        onDelete = { viewModel.deleteReminder(reminder) },
                        onCheckedChange = { viewModel.toggleCompletion(reminder) }
                    )
                }
            }
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Nueva Tarea") },
                text = {
                    Column {
                        TextField(value = title, onValueChange = { title = it }, label = { Text("Título") })
                        Spacer(modifier = Modifier.height(8.dp))
                        TextField(value = desc, onValueChange = { desc = it }, label = { Text("Descripción") })
                        Spacer(modifier = Modifier.height(16.dp))

                        Button(onClick = {
                            val calendar = Calendar.getInstance()
                            // Selector de Fecha
                            DatePickerDialog(context, { _, year, month, day ->
                                // Selector de Hora (dentro de la fecha)
                                TimePickerDialog(context, { _, hour, minute ->
                                    val pickedDateTime = Calendar.getInstance()
                                    pickedDateTime.set(year, month, day, hour, minute)
                                    pickedDateTime.set(Calendar.SECOND, 0)
                                    pickedDateTime.set(Calendar.MILLISECOND, 0)
                                    selectedTimestamp = pickedDateTime.timeInMillis
                                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show()
                                // 'false' arriba muestra AM/PM en el reloj si prefieres,
                                // pero guarda los datos igual de bien.
                            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
                        }) {
                            Text("Vence: ${sdf.format(Date(selectedTimestamp))}")
                        }
                    }
                },
                confirmButton = {
                    Button(onClick = {
                        if (title.isNotBlank()) {
                            viewModel.addReminder(title, desc, selectedTimestamp)

                            val tempReminder = ReminderEntity(
                                title = title,
                                description = desc,
                                createdAt = System.currentTimeMillis(),
                                dueDate = selectedTimestamp
                            )
                            viewModel.scheduleAlarm(context, tempReminder)

                            title = ""
                            desc = ""
                            showDialog = false
                        }
                    }) { Text("Guardar") }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) { Text("Cancelar") }
                }
            )
        }
    }
}