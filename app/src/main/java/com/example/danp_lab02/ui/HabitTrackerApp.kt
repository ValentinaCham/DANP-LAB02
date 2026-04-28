package com.example.danp_lab02.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun HabitTrackerApp(viewModel: HabitViewModel = viewModel()) {
    var showHistory by remember { mutableStateOf(false) }

    if (showHistory) {
        HistoryScreen(viewModel = viewModel, onBack = { showHistory = false })
    } else {
        MainScreen(viewModel, onOpenHistory = { showHistory = true })
    }
}

@Composable
fun MainScreen(viewModel: HabitViewModel, onOpenHistory: () -> Unit) {
    var input by remember { mutableStateOf("") }
    val selectedDate by viewModel.selectedDate.collectAsState()
    val habitsWithStatus by viewModel.habitsWithStatus.collectAsState()
    val selectedDays = remember { mutableStateListOf(1, 2, 3, 4, 5, 6, 7) }

    val completedCount = habitsWithStatus.count { it.isCompleted }
    val progress = if (habitsWithStatus.isNotEmpty()) {
        completedCount.toFloat() / habitsWithStatus.size
    } else 0f

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Habit Tracker",
                style = MaterialTheme.typography.headlineMedium
            )
            IconButton(onClick = onOpenHistory) {
                Text("📊", style = MaterialTheme.typography.headlineSmall)
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))

        // Selector de Fecha
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { viewModel.setSelectedDate(selectedDate.minusDays(1)) }) {
                Text("<")
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = selectedDate.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault()),
                    style = MaterialTheme.typography.labelLarge
                )
                Text(
                    text = selectedDate.toString(),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            IconButton(onClick = { viewModel.setSelectedDate(selectedDate.plusDays(1)) }) {
                Text(">")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text("Progreso del día: ${(progress * 100).toInt()}%")
        
        Spacer(modifier = Modifier.height(24.dp))

        // Sección para añadir nuevo hábito
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                TextField(
                    value = input,
                    onValueChange = { input = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("¿Qué hábito quieres cultivar?") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("Repetir los días:", style = MaterialTheme.typography.labelMedium)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    val daysNames = listOf("L", "M", "X", "J", "V", "S", "D")
                    daysNames.forEachIndexed { index, name ->
                        val dayNum = index + 1
                        val isSelected = selectedDays.contains(dayNum)
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .background(
                                    if (isSelected) MaterialTheme.colorScheme.primary else Color.LightGray,
                                    CircleShape
                                )
                                .clickable {
                                    if (isSelected) selectedDays.remove(dayNum)
                                    else selectedDays.add(dayNum)
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                name,
                                color = if (isSelected) Color.White else Color.Black,
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        if (input.isNotBlank() && selectedDays.isNotEmpty()) {
                            viewModel.addHabit(input, selectedDays.toList())
                            input = ""
                        }
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Crear Hábito")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Lista de Hábitos del día
        if (habitsWithStatus.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No hay hábitos programados para hoy.", style = MaterialTheme.typography.bodyMedium)
            }
        } else {
            LazyColumn {
                items(habitsWithStatus, key = { it.habit.id }) { item ->
                    HabitItem(
                        habitWithStatus = item,
                        onToggle = { checked ->
                            viewModel.toggleHabit(item.habit.id, checked)
                        },
                        onDelete = {
                            viewModel.deleteHabit(item.habit.id)
                        }
                    )
                }
            }
        }
    }
}
