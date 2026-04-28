package com.example.danp_lab02.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowRight
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
import com.example.danp_lab02.model.Habit
import com.example.danp_lab02.ui.theme.GlassWhite

@Composable
fun ManageScreen(viewModel: HabitViewModel) {
    val habits by viewModel.habitsWithStatus.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        contentPadding = PaddingValues(vertical = 24.dp)
    ) {
        item {
            Text(
                text = "Manage Habits",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Customize your routine and track what matters most.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(32.dp))
        }

        // Create Custom Habit Button
        item {
            Button(
                onClick = { /* TODO: Open Create Dialog */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues(0.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(MaterialTheme.colorScheme.primary, Color(0xFF8E24AA))
                            )
                        )
                        .padding(horizontal = 20.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            modifier = Modifier.size(48.dp),
                            shape = CircleShape,
                            color = Color.White.copy(alpha = 0.2f)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Add, null, tint = Color.White)
                            }
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Create Custom Habit", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            Text("Start a new journey today", color = Color.White.copy(alpha = 0.7f), fontSize = 14.sp)
                        }
                        Icon(Icons.Default.KeyboardArrowRight, null, tint = Color.White)
                    }
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
        }

        item {
            Text("ACTIVE ROUTINES", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
            Spacer(modifier = Modifier.height(16.dp))
        }

        items(habits) { item ->
            ActiveRoutineItem(habit = item.habit, onDelete = { viewModel.deleteHabit(item.habit.id) })
            Spacer(modifier = Modifier.height(12.dp))
        }

        // Consistency card
        item {
            Spacer(modifier = Modifier.height(24.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF2D2F7A))
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Surface(
                        shape = CircleShape,
                        color = Color.White.copy(alpha = 0.1f)
                    ) {
                        Text(
                            "Efficiency Stats",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            color = Color.White,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Consistency is Key", style = MaterialTheme.typography.headlineMedium, color = Color.White)
                    Text(
                        "You have 3 active habits. You're currently on a 12-day streak!",
                        color = Color.White.copy(alpha = 0.7f),
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Row {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("85%", style = MaterialTheme.typography.headlineSmall, color = Color.White)
                            Text("COMPLETION", style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = 0.5f))
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text("12", style = MaterialTheme.typography.headlineSmall, color = Color.White)
                            Text("DAY STREAK", style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = 0.5f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ActiveRoutineItem(habit: Habit, onDelete: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = GlassWhite
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(48.dp),
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFB3E5FC)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(getIconForHabit(habit.title), fontSize = 24.sp)
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(habit.title, fontWeight = FontWeight.Bold)
                Row {
                    Surface(shape = CircleShape, color = Color(0xFFE1F5FE)) {
                        Text("Daily", modifier = Modifier.padding(horizontal = 8.dp), fontSize = 10.sp, color = Color(0xFF039BE5))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("• 15 mins", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                }
            }
            IconButton(onClick = { /* TODO: Edit */ }) {
                Icon(Icons.Default.Edit, null, tint = Color.LightGray, modifier = Modifier.size(20.dp))
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, null, tint = Color.LightGray, modifier = Modifier.size(20.dp))
            }
        }
    }
}
