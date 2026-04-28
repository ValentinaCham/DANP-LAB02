package com.example.danp_lab02.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun HabitTrackerApp(viewModel: HabitViewModel = viewModel()) {
    var input by remember { mutableStateOf("") }
    val habits by viewModel.habits.collectAsState()

    val completedCount = habits.count { it.isCompletedToday }
    val progress = if (habits.isNotEmpty()) {
        completedCount.toFloat() / habits.size
    } else 0f

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Habit Tracker",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(16.dp))

        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text("Progreso: ${(progress * 100).toInt()}%")
        Spacer(modifier = Modifier.height(16.dp))

        Row {
            TextField(
                value = input,
                onValueChange = { input = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Nuevo hábito") }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                if (input.isNotBlank()) {
                    viewModel.addHabit(input)
                    input = ""
                }
            }) {
                Text("Agregar")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn {
            items(habits, key = { it.id }) { habit ->
                HabitItem(
                    habit = habit,
                    onToggle = { checked ->
                        viewModel.toggleHabit(habit, checked)
                    },
                    onDelete = {
                        viewModel.deleteHabit(habit)
                    }
                )
            }
        }
    }
}
