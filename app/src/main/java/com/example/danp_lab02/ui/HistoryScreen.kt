package com.example.danp_lab02.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.danp_lab02.ui.theme.GlassWhite
import com.example.danp_lab02.ui.theme.SuccessMint
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun HistoryScreen(viewModel: HabitViewModel, onBack: () -> Unit) {
    val last7Days by viewModel.last7DaysProgress.collectAsState()
    val streak by viewModel.currentStreak.collectAsState()

    val streakMessage = when {
        streak >= 7 -> "You're on fire! Keep the momentum going to reach your next milestone."
        streak >= 3 -> "Great job! You're building a solid habit. Don't let the flame go out!"
        streak > 0 -> "You've started! Consistency is key. Keep going!"
        else -> "Start your journey today! Complete all your habits to start a streak."
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        contentPadding = PaddingValues(vertical = 24.dp)
    ) {
        // Streak Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent)
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(MaterialTheme.colorScheme.primary, Color(0xFF8E24AA))
                            )
                        )
                        .padding(32.dp)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(if (streak >= 7) "🔥" else "✨", fontSize = 48.sp)
                        Text(
                            "$streak Day Streak!",
                            style = MaterialTheme.typography.headlineLarge,
                            color = Color.White
                        )
                        Text(
                            streakMessage,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.8f),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Surface(
                            shape = CircleShape,
                            color = Color.White.copy(alpha = 0.2f)
                        ) {
                            Text(
                                "Level ${streak / 7 + 1} Habit Master",
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                                color = Color.White,
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
        }

        // Last 7 Days Row (Semaphore)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Last 7 Days", style = MaterialTheme.typography.headlineSmall)
                Text("Details", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.labelMedium)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Iteramos los datos y calculamos la etiqueta del día dinámicamente
                last7Days.reversed().forEach { day ->
                    val date = LocalDate.parse(day.date)
                    val dayLabel = date.dayOfWeek.getDisplayName(TextStyle.NARROW, Locale.getDefault())
                    
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = dayLabel, 
                            style = MaterialTheme.typography.labelSmall, 
                            color = if (date == LocalDate.now()) MaterialTheme.colorScheme.primary else Color.Gray,
                            fontWeight = if (date == LocalDate.now()) FontWeight.Bold else FontWeight.Normal
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        val color = when {
                            day.progress >= 0.75f -> SuccessMint // Verde si cumple el requisito de racha
                            day.progress >= 0.50f -> Color(0xFFFFC107) // Amarillo
                            day.progress > 0.0f -> Color(0xFFF44336) // Rojo
                            else -> Color.LightGray.copy(alpha = 0.3f) // Vacío
                        }
                        Surface(
                            modifier = Modifier.size(36.dp),
                            shape = CircleShape,
                            color = color
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                if (day.progress >= 0.75f) {
                                    Icon(Icons.Default.Check, null, tint = Color.White, modifier = Modifier.size(16.dp))
                                } else if (day.progress > 0.0f) {
                                    Text("!", color = Color.White, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
        }

        // Performance Card
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                color = GlassWhite
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Performance", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            Text("Weekly habit completion", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                        }
                        Surface(shape = CircleShape, color = SuccessMint.copy(alpha = 0.1f)) {
                            val avgProgress = if (last7Days.isNotEmpty()) last7Days.map { it.progress }.average() else 0.0
                            Text("${(avgProgress * 100).toInt()}%", modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp), color = SuccessMint, style = MaterialTheme.typography.labelSmall)
                        }
                    }
                    Spacer(modifier = Modifier.height(64.dp)) // Placeholder for Chart
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        listOf("W1", "W2", "W3", "W4").forEach {
                            Text(it, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        // Stats Row
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                val accuracy = if (last7Days.isNotEmpty()) (last7Days.map { it.progress }.average() * 100).toInt() else 0
                StatCard(modifier = Modifier.weight(1f), icon = "📈", value = "$accuracy%", label = "Accuracy")
                StatCard(modifier = Modifier.weight(1f), icon = "🏆", value = "${streak / 7}", label = "Badges")
            }
            Spacer(modifier = Modifier.height(32.dp))
        }

        // Recent History
        item {
            Text("Recent History", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(16.dp))
        }
        
        item {
            Text("Pro tip: Maintain at least 75% daily progress to keep your streak alive!", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        }
    }
}

@Composable
fun StatCard(modifier: Modifier, icon: String, value: String, label: String) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        color = GlassWhite
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(icon, fontSize = 24.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(value, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text(label, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        }
    }
}
