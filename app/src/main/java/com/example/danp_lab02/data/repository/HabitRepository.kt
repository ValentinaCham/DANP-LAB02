package com.example.danp_lab02.data.repository

import com.example.danp_lab02.data.HabitDao
import com.example.danp_lab02.model.Habit
import com.example.danp_lab02.model.HabitCompletion
import kotlinx.coroutines.flow.Flow

interface HabitRepository {
    fun getAllHabits(): Flow<List<Habit>>
    fun getCompletionsByDate(date: String): Flow<List<HabitCompletion>>
    fun getAllCompletions(): Flow<List<HabitCompletion>>
    suspend fun insertHabit(habit: Habit)
    suspend fun deleteHabit(habit: Habit)
    suspend fun insertCompletion(completion: HabitCompletion)
    suspend fun deleteCompletion(completion: HabitCompletion)
    suspend fun deleteCompletionsByHabitId(habitId: Int)
}

class HabitRepositoryImpl(private val habitDao: HabitDao) : HabitRepository {
    override fun getAllHabits() = habitDao.getAllHabits()
    override fun getCompletionsByDate(date: String) = habitDao.getCompletionsByDate(date)
    override fun getAllCompletions() = habitDao.getAllCompletions()
    
    override suspend fun insertHabit(habit: Habit) {
        habitDao.insertHabit(habit)
    }
    
    override suspend fun deleteHabit(habit: Habit) {
        habitDao.deleteHabit(habit)
    }
    
    override suspend fun insertCompletion(completion: HabitCompletion) {
        habitDao.insertCompletion(completion)
    }
    
    override suspend fun deleteCompletion(completion: HabitCompletion) {
        habitDao.deleteCompletion(completion)
    }
    
    override suspend fun deleteCompletionsByHabitId(habitId: Int) {
        habitDao.deleteCompletionsByHabitId(habitId)
    }
}
