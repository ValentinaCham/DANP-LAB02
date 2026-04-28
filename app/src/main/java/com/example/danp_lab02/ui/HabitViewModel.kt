package com.example.danp_lab02.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.danp_lab02.data.HabitDatabase
import com.example.danp_lab02.model.Habit
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HabitViewModel(application: Application) : AndroidViewModel(application) {
    private val habitDao = HabitDatabase.getDatabase(application).habitDao()

    val habits: StateFlow<List<Habit>> = habitDao.getAllHabits()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addHabit(title: String) {
        viewModelScope.launch {
            habitDao.insertHabit(Habit(title = title))
        }
    }

    fun toggleHabit(habit: Habit, isCompleted: Boolean) {
        viewModelScope.launch {
            habitDao.updateHabit(habit.copy(isCompletedToday = isCompleted))
        }
    }

    fun deleteHabit(habit: Habit) {
        viewModelScope.launch {
            habitDao.deleteHabit(habit)
        }
    }
}
