package com.tecsup.vibecheck.domain

import com.tecsup.vibecheck.data.local.ReminderEntity
import kotlinx.coroutines.flow.Flow

interface ReminderRepository {

    fun getReminders(): Flow<List<ReminderEntity>>


    suspend fun insertReminder(reminder: ReminderEntity)

    suspend fun deleteReminder(reminder: ReminderEntity)

    suspend fun updateReminder(reminder: ReminderEntity)

}