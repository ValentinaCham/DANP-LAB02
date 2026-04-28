package com.example.danp_lab02.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.danp_lab02.data.HabitDatabase
import com.example.danp_lab02.model.DayProgress
import com.example.danp_lab02.model.Habit
import com.example.danp_lab02.model.HabitCompletion
import com.example.danp_lab02.model.HabitWithStatus
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate

class HabitViewModel(application: Application) : AndroidViewModel(application) {
    private val habitDao = HabitDatabase.getDatabase(application).habitDao()

    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val habitsWithStatus: StateFlow<List<HabitWithStatus>> = _selectedDate
        .flatMapLatest { date ->
            getHabitsWithStatusForDate(date)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val last7DaysProgress: StateFlow<List<DayProgress>> = combine(
        habitDao.getAllHabits(),
        habitDao.getAllCompletions()
    ) { allHabits, allCompletions ->
        (0..6).map { i ->
            val date = LocalDate.now().minusDays(i.toLong())
            val progress = calculateProgressForDate(date, allHabits, allCompletions)
            DayProgress(date.toString(), progress)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val currentStreak: StateFlow<Int> = combine(
        habitDao.getAllHabits(),
        habitDao.getAllCompletions()
    ) { allHabits, allCompletions ->
        calculateStreak(allHabits, allCompletions)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    private fun getHabitsWithStatusForDate(date: LocalDate): Flow<List<HabitWithStatus>> {
        val dateStr = date.toString()
        val dayOfWeek = date.dayOfWeek.value
        return combine(
            habitDao.getAllHabits(),
            habitDao.getCompletionsByDate(dateStr)
        ) { allHabits, completions ->
            val completedIds = completions.map { it.habitId }.toSet()
            allHabits
                .filter { it.repeatDays.contains(dayOfWeek.toString()) }
                .map { habit ->
                    HabitWithStatus(habit, completedIds.contains(habit.id))
                }
        }
    }

    private fun calculateProgressForDate(
        date: LocalDate, 
        allHabits: List<Habit>, 
        allCompletions: List<HabitCompletion>
    ): Float {
        val dateStr = date.toString()
        val dayOfWeek = date.dayOfWeek.value
        
        val habitsForDay = allHabits.filter { it.repeatDays.contains(dayOfWeek.toString()) }
        if (habitsForDay.isEmpty()) return 0f
        
        val completionsForDay = allCompletions.filter { it.date == dateStr }
        val completedCount = completionsForDay.count { comp -> habitsForDay.any { it.id == comp.habitId } }
        
        return completedCount.toFloat() / habitsForDay.size
    }

    private fun calculateStreak(allHabits: List<Habit>, allCompletions: List<HabitCompletion>): Int {
        var streak = 0
        var checkDate = LocalDate.now()
        
        // El usuario quiere que una racha cuente si tiene al menos un 75%
        while (true) {
            val progress = calculateProgressForDate(checkDate, allHabits, allCompletions)
            if (progress >= 0.75f) {
                streak++
                checkDate = checkDate.minusDays(1)
            } else {
                // Si llegamos a hoy y aún no tiene el 75%, miramos ayer para ver si la racha sigue viva
                // (permitiendo al usuario completar hoy para mantener la racha)
                if (checkDate == LocalDate.now()) {
                    checkDate = checkDate.minusDays(1)
                    continue 
                }
                break
            }
        }
        return streak
    }

    fun setSelectedDate(date: LocalDate) {
        _selectedDate.value = date
    }

    fun addHabit(title: String, days: List<Int>) {
        viewModelScope.launch {
            habitDao.insertHabit(Habit(title = title, repeatDays = days.joinToString(",")))
        }
    }

    fun toggleHabit(habitId: Int, isCompleted: Boolean) {
        viewModelScope.launch {
            val dateStr = _selectedDate.value.toString()
            if (isCompleted) {
                habitDao.insertCompletion(HabitCompletion(habitId, dateStr))
            } else {
                habitDao.deleteCompletion(HabitCompletion(habitId, dateStr))
            }
        }
    }

    fun deleteHabit(habitId: Int) {
        viewModelScope.launch {
            habitDao.deleteHabit(Habit(id = habitId, title = "", repeatDays = ""))
            habitDao.deleteCompletionsByHabitId(habitId)
        }
    }
}
