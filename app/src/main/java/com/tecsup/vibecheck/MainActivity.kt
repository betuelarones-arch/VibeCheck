package com.tecsup.vibecheck

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.tecsup.vibecheck.data.local.AppDatabase
import com.tecsup.vibecheck.data.repository.ReminderRepositoryImpl
import com.tecsup.vibecheck.ui.screens.HomeScreen
import com.tecsup.vibecheck.ui.theme.VibeCheckTheme
import com.tecsup.vibecheck.ui.viewmodel.ReminderViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = AppDatabase.getDatabase(applicationContext)
        val repository = ReminderRepositoryImpl(db.reminderDao())
        val viewModel = ReminderViewModel(repository)

        setContent {
            VibeCheckTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HomeScreen(viewModel)
                }
            }
        }
    }
}