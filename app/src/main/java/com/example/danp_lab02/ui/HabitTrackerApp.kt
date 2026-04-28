package com.example.danp_lab02.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.danp_lab02.ui.theme.GlassWhite

@Composable
fun HabitTrackerApp(viewModel: HabitViewModel = viewModel()) {
    var currentTab by remember { mutableStateOf("Today") }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 0.dp // Eliminamos elevación para un look plano y limpio
            ) {
                NavigationBarItem(
                    selected = currentTab == "Today",
                    onClick = { currentTab = "Today" },
                    icon = { Icon(Icons.Default.DateRange, contentDescription = null) },
                    label = { Text("Today") }
                )
                NavigationBarItem(
                    selected = currentTab == "Streaks",
                    onClick = { currentTab = "Streaks" },
                    icon = { Text("📈", fontSize = 20.sp) },
                    label = { Text("Streaks") }
                )
                NavigationBarItem(
                    selected = currentTab == "Manage",
                    onClick = { currentTab = "Manage" },
                    icon = { Icon(Icons.Default.Settings, contentDescription = null) },
                    label = { Text("Manage") }
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFFF7F9FB), Color(0xFFE1E0FF))
                    )
                )
        ) {
            when (currentTab) {
                "Today" -> MainScreen(viewModel)
                "Streaks" -> HistoryScreen(viewModel, onBack = { currentTab = "Today" })
                "Manage" -> ManageScreen(viewModel)
            }
        }
    }
}

@Composable
fun MainScreen(viewModel: HabitViewModel) {
    var input by remember { mutableStateOf("") }
    val habitsWithStatus by viewModel.habitsWithStatus.collectAsState()
    val selectedDays = remember { mutableStateListOf(1, 2, 3, 4, 5, 6, 7) }
    var filter by remember { mutableStateOf("All") }

    val filteredHabits = when (filter) {
        "Done" -> habitsWithStatus.filter { it.isCompleted }
        "Pendant" -> habitsWithStatus.filter { !it.isCompleted }
        else -> habitsWithStatus
    }

    val completedCount = habitsWithStatus.count { it.isCompleted }
    val totalCount = habitsWithStatus.size
    val progress = if (totalCount > 0) completedCount.toFloat() / totalCount else 0f

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        contentPadding = PaddingValues(vertical = 24.dp)
    ) {
        // Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        modifier = Modifier.size(40.dp),
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primaryContainer
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text("👤", fontSize = 20.sp)
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "ZenHabit",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Icon(Icons.Default.Settings, contentDescription = null, tint = Color.Gray)
            }
            Spacer(modifier = Modifier.height(32.dp))
        }

        // Circular Progress Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(
                            progress = { progress },
                            modifier = Modifier.size(160.dp),
                            strokeWidth = 12.dp,
                            color = MaterialTheme.colorScheme.primary,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "${(progress * 100).toInt()}%",
                                style = MaterialTheme.typography.headlineLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "COMPLETE",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.Gray
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "Today's Progress",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = if (progress >= 1f) "All habits done! 🚀" else "Almost there! ${totalCount - completedCount} habits left.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
        }

        // Add Habit Section
        item {
            Text("ADD NEW HABIT", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
            Spacer(modifier = Modifier.height(12.dp))
            
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(GlassWhite, RoundedCornerShape(24.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        TextField(
                            value = input,
                            onValueChange = { input = it },
                            placeholder = { Text("What's your next goal?") },
                            modifier = Modifier.weight(1f),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                disabledContainerColor = Color.Transparent,
                                errorContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent,
                                errorIndicatorColor = Color.Transparent
                            )
                        )
                        FloatingActionButton(
                            onClick = {
                                if (input.isNotBlank()) {
                                    viewModel.addHabit(input, selectedDays.toList())
                                    input = ""
                                }
                            },
                            modifier = Modifier.size(40.dp),
                            shape = RoundedCornerShape(12.dp),
                            containerColor = MaterialTheme.colorScheme.primary,
                            elevation = FloatingActionButtonDefaults.elevation(0.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, tint = Color.White)
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        val daysNames = listOf("L", "M", "X", "J", "V", "S", "D")
                        daysNames.forEachIndexed { index, name ->
                            val dayNum = index + 1
                            val isSelected = selectedDays.contains(dayNum)
                            Surface(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clickable {
                                        if (isSelected) selectedDays.remove(dayNum)
                                        else selectedDays.add(dayNum)
                                    },
                                shape = CircleShape,
                                border = if (!isSelected) BorderStroke(1.dp, Color.LightGray) else null,
                                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = name,
                                        color = if (isSelected) Color.White else Color.Black,
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                }
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
        }

        // Daily Rituals
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("DAILY RITUALS", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
                
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("All", "Pendant", "Done").forEach { option ->
                        val isSelected = filter == option
                        Surface(
                            modifier = Modifier.clickable { filter = option },
                            shape = RoundedCornerShape(8.dp),
                            color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent,
                            border = if (!isSelected) BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f)) else null
                        ) {
                            Text(
                                text = option,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelSmall,
                                color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else Color.Gray
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        if (filteredHabits.isEmpty()) {
            item {
                Text(
                    text = when (filter) {
                        "Done" -> "No completed habits yet."
                        "Pendant" -> "All caught up! No pending habits."
                        else -> "No habits for today."
                    },
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
        } else {
            items(filteredHabits, key = { it.habit.id }) { item ->
                HabitItem(
                    habitWithStatus = item,
                    onToggle = { checked -> viewModel.toggleHabit(item.habit.id, checked) },
                    onDelete = { viewModel.deleteHabit(item.habit.id) }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}
