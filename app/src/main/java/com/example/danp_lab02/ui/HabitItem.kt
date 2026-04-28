package com.example.danp_lab02.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.danp_lab02.model.HabitWithStatus
import com.example.danp_lab02.ui.theme.GlassWhite
import com.example.danp_lab02.ui.theme.SuccessMint

@Composable
fun HabitItem(
    habitWithStatus: HabitWithStatus,
    onToggle: (Boolean) -> Unit,
    onDelete: () -> Unit
) {
    val habit = habitWithStatus.habit
    val isCompleted = habitWithStatus.isCompleted

    // Usamos Box en lugar de Surface para evitar la elevación tonal automática de Material 3
    // que causa ese "resaltado blanco" indeseado sobre fondos con gradiente.
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(GlassWhite)
            .clickable { onToggle(!isCompleted) }
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                // Contenedor del Icono (usando Box para transparencia pura)
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (isCompleted) SuccessMint.copy(alpha = 0.1f) 
                            else MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = getIconForHabit(habit.title),
                        fontSize = 24.sp
                    )
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                Column {
                    Text(
                        text = habit.title,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = if (isCompleted) Color.Gray else MaterialTheme.colorScheme.onSurface
                    )
                    val daysCount = habit.repeatDays.split(",").filter { it.isNotBlank() }.size
                    Text(
                        text = "DAILY • $daysCount DAYS",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Gray
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onDelete) {
                    Icon(
                        Icons.Default.Delete, 
                        contentDescription = null, 
                        tint = Color.LightGray.copy(alpha = 0.6f), 
                        modifier = Modifier.size(20.dp)
                    )
                }
                
                // Checkbox personalizado (evitando Surface para mantener el efecto Glass)
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(if (isCompleted) SuccessMint else Color.Transparent)
                        .then(
                            if (!isCompleted) Modifier.border(2.dp, Color.LightGray.copy(alpha = 0.5f), CircleShape)
                            else Modifier
                        )
                        .clickable { onToggle(!isCompleted) },
                    contentAlignment = Alignment.Center
                ) {
                    if (isCompleted) {
                        Icon(
                            Icons.Default.Check,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

fun getIconForHabit(title: String): String {
    return when {
        title.contains("meditar", ignoreCase = true) -> "🧘"
        title.contains("agua", ignoreCase = true) -> "💧"
        title.contains("leer", ignoreCase = true) -> "📚"
        title.contains("ejercicio", ignoreCase = true) -> "🏋️"
        title.contains("cama", ignoreCase = true) -> "🛏️"
        else -> "✨"
    }
}
