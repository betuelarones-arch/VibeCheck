package com.tecsup.vibecheck.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.vibecheck.data.local.ReminderEntity
import com.tecsup.vibecheck.domain.ReminderRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.tecsup.vibecheck.data.local.AlarmReceiver

class ReminderViewModel(
    private val repository: ReminderRepository
) : ViewModel() {
    val reminders: StateFlow<List<ReminderEntity>> = repository.getReminders()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addReminder(title: String, description: String, dueDate: Long) {
        viewModelScope.launch {
            val newReminder = ReminderEntity(
                title = title,
                description = description,
                createdAt = System.currentTimeMillis(),
                dueDate = dueDate
            )
            repository.insertReminder(newReminder)
        }
    }

    fun deleteReminder(reminder: ReminderEntity) {
        viewModelScope.launch {
            repository.deleteReminder(reminder)
        }
    }

    fun toggleCompletion(reminder: ReminderEntity) {
        viewModelScope.launch {
            repository.updateReminder(reminder.copy(isCompleted = !reminder.isCompleted))
        }
    }

    fun scheduleAlarm(context: Context, reminder: ReminderEntity) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            if (!alarmManager.canScheduleExactAlarms()) {
                // Si no tiene permiso, abrimos la configuración para que el usuario lo active
                val intent = Intent(android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                context.startActivity(intent)
                return
            }
        }

        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("title", reminder.title)
            putExtra("desc", reminder.description)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            reminder.hashCode(), // Usamos hashCode para asegurar un ID único si el ID de la DB es 0 aún
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        try {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                reminder.dueDate,
                pendingIntent
            )
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }
}