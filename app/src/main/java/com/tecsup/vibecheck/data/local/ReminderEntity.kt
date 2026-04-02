package com.tecsup.vibecheck.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reminders")
data class ReminderEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    val createdAt: Long,
    val dueData: Long,
    val isCompleted: Boolean = false
)