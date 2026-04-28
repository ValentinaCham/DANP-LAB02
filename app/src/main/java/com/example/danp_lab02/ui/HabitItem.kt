package com.example.danp_lab02.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.danp_lab02.model.HabitWithStatus

@Composable
fun HabitItem(
    habitWithStatus: HabitWithStatus,
    onToggle: (Boolean) -> Unit,
    onDelete: () -> Unit
) {
    val habit = habitWithStatus.habit
    val isCompleted = habitWithStatus.isCompleted

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(modifier = Modifier.weight(1f)) {
            Checkbox(
                checked = isCompleted,
                onCheckedChange = onToggle
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = habit.title,
                style = if (isCompleted)
                    MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.primary
                    )
                else
                    MaterialTheme.typography.bodyLarge
            )
        }
        IconButton(onClick = onDelete) {
            Text("🗑️")
        }
    }
}
