package com.example.danp_lab02.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habits")
data class Habit(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val repeatDays: String // Guardado como "1,2,3" donde 1=Lunes, 7=Domingo
)

@Entity(
    tableName = "habit_completions",
    primaryKeys = ["habitId", "date"]
)
data class HabitCompletion(
    val habitId: Int,
    val date: String // Formato "YYYY-MM-DD"
)

data class HabitWithStatus(
    val habit: Habit,
    val isCompleted: Boolean
)

data class DayProgress(
    val date: String,
    val progress: Float
)
