package com.example.danp_lab02.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.danp_lab02.model.Habit

@Composable
fun HabitItem(
    habit: Habit,
    onToggle: (Boolean) -> Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(modifier = Modifier.weight(1f)) {
            Checkbox(
                checked = habit.isCompletedToday,
                onCheckedChange = onToggle
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = habit.title,
                style = if (habit.isCompletedToday)
                    MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.primary
                    )
                else
                    MaterialTheme.typography.bodyLarge
            )
        }
        Button(onClick = onDelete) {
            Text("Eliminar")
        }
    }
}
