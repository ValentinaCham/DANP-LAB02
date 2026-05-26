package com.example.danp_lab02.ui

import com.example.danp_lab02.model.DayProgress
import com.example.danp_lab02.model.HabitWithStatus
import java.time.LocalDate

data class HabitUiState(
    val habits: List<HabitWithStatus> = emptyList(),
    val selectedDate: LocalDate = LocalDate.now(),
    val weeklyProgress: List<DayProgress> = emptyList(),
    val currentStreak: Int = 0,
    val isLoading: Boolean = false
)
