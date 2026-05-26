package com.example.danp_lab02.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.danp_lab02.data.HabitDatabase
import com.example.danp_lab02.data.repository.HabitRepository
import com.example.danp_lab02.data.repository.HabitRepositoryImpl
import com.example.danp_lab02.model.DayProgress
import com.example.danp_lab02.model.Habit
import com.example.danp_lab02.model.HabitCompletion
import com.example.danp_lab02.model.HabitWithStatus
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate

class HabitViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: HabitRepository = HabitRepositoryImpl(
        HabitDatabase.getDatabase(application).habitDao()
    )

    private val _selectedDate = MutableStateFlow(LocalDate.now())
    
    val uiState: StateFlow<HabitUiState> = combine(
        _selectedDate.flatMapLatest { date -> getHabitsWithStatusForDate(date) },
        _selectedDate,
        calculateWeeklyProgress(),
        calculateStreakFlow()
    ) { habits, date, weeklyProgress, streak ->
        HabitUiState(
            habits = habits,
            selectedDate = date,
            weeklyProgress = weeklyProgress,
            currentStreak = streak
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), HabitUiState())

    // Exponemos habitsWithStatus para compatibilidad con la UI actual o la refactorizamos luego
    val habitsWithStatus = uiState.map { it.habits }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        seedDatabase()
    }

    private fun seedDatabase() {
        viewModelScope.launch {
            val existingHabits = repository.getAllHabits().first()
            if (existingHabits.size < 10) {
                (1..80).forEach { i ->
                    repository.insertHabit(
                        Habit(
                            title = "Habit sintético #$i",
                            repeatDays = "1,2,3,4,5,6,7"
                        )
                    )
                }
            }
        }
    }

    private fun calculateWeeklyProgress(): Flow<List<DayProgress>> = combine(
        repository.getAllHabits(),
        repository.getAllCompletions()
    ) { allHabits, allCompletions ->
        (0..6).map { i ->
            val date = LocalDate.now().minusDays(i.toLong())
            val progress = calculateProgressForDate(date, allHabits, allCompletions)
            DayProgress(date.toString(), progress)
        }
    }

    private fun calculateStreakFlow(): Flow<Int> = combine(
        repository.getAllHabits(),
        repository.getAllCompletions()
    ) { allHabits, allCompletions ->
        calculateStreak(allHabits, allCompletions)
    }

    private fun getHabitsWithStatusForDate(date: LocalDate): Flow<List<HabitWithStatus>> {
        val dateStr = date.toString()
        val dayOfWeek = date.dayOfWeek.value
        return combine(
            repository.getAllHabits(),
            repository.getCompletionsByDate(dateStr)
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
            repository.insertHabit(Habit(title = title, repeatDays = days.joinToString(",")))
        }
    }

    fun toggleHabit(habitId: Int, isCompleted: Boolean) {
        viewModelScope.launch {
            val dateStr = _selectedDate.value.toString()
            if (isCompleted) {
                repository.insertCompletion(HabitCompletion(habitId, dateStr))
            } else {
                repository.deleteCompletion(HabitCompletion(habitId, dateStr))
            }
        }
    }

    fun deleteHabit(habitId: Int) {
        viewModelScope.launch {
            repository.deleteHabit(Habit(id = habitId, title = "", repeatDays = ""))
            repository.deleteCompletionsByHabitId(habitId)
        }
    }
}
