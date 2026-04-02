package com.tecsup.vibecheck.data.repository

import com.tecsup.vibecheck.data.local.ReminderDao
import com.tecsup.vibecheck.data.local.ReminderEntity
import com.tecsup.vibecheck.domain.ReminderRepository
import kotlinx.coroutines.flow.Flow

class ReminderRepositoryImpl(
    private val dao: ReminderDao
) : ReminderRepository {

    override fun getReminders(): Flow<List<ReminderEntity>> {
        return dao.getAllReminders()
    }

    override suspend fun insertReminder(reminder: ReminderEntity) {
        dao.insertReminder(reminder)
    }

    override suspend fun deleteReminder(reminder: ReminderEntity) {
        dao.deleteReminder(reminder)
    }

    override suspend fun updateReminder(reminder: ReminderEntity) {
        dao.updateReminder(reminder)
    }
}