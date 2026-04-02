package com.tecsup.vibecheck.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.tecsup.vibecheck.data.local.ReminderEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ReminderItem(
    reminder: ReminderEntity,
    onDelete: () -> Unit,
    onCheckedChange: (Boolean) -> Unit
) {
    val isExpired = reminder.dueDate < System.currentTimeMillis() && !reminder.isCompleted
    val sdf = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault())

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (reminder.isCompleted)
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Botón de Checkbox Pro
            FilledIconToggleButton(
                checked = reminder.isCompleted,
                onCheckedChange = onCheckedChange,
                modifier = Modifier.size(48.dp),
                colors = IconButtonDefaults.filledIconToggleButtonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    checkedContainerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(
                    imageVector = if (reminder.isCompleted) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                    contentDescription = null
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = reminder.title,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    textDecoration = if (reminder.isCompleted) TextDecoration.LineThrough else null,
                    color = if (isExpired) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = if (isExpired) "⏰ Vencido: ${sdf.format(Date(reminder.dueDate))}"
                    else "📅 Vence: ${sdf.format(Date(reminder.dueDate))}",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isExpired) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.secondary
                )
            }

            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.DeleteOutline,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}