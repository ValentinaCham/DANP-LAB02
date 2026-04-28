package com.example.danp_lab02.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun HistoryScreen(viewModel: HabitViewModel, onBack: () -> Unit) {
    val last7Days by viewModel.last7DaysProgress.collectAsState()

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
            Text(text = "Historial y Progreso", style = MaterialTheme.typography.headlineMedium)
            IconButton(onClick = onBack) {
                Text("❌")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(text = "Últimos 7 días", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(last7Days) { day ->
                val color = when {
                    day.progress >= 0.75f -> Color(0xFF4CAF50) // Verde
                    day.progress >= 0.50f -> Color(0xFFFFEB3B) // Amarillo
                    else -> Color(0xFFF44336) // Rojo
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.2f))
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = day.date, fontWeight = FontWeight.Bold)
                            Text(
                                text = "Progreso: ${(day.progress * 100).toInt()}%",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(color, RoundedCornerShape(20.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            // Círculo de color semáforo
                        }
                    }
                }
            }
        }
    }
}
