package dev.mambo.lib.ui.presentation.screens.task.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import dev.mambo.lib.data.domain.helpers.LocalDateTime
import dev.mambo.lib.data.domain.helpers.asEpochMilliseconds
import dev.mambo.lib.ui.presentation.screens.task.TaskScreenState
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDateComponent(
    state: TaskScreenState,
    modifier: Modifier = Modifier,
    onValueChangeDate: (LocalDateTime) -> Unit,
) {

    var isOpen by remember { mutableStateOf(false) }
    val pickerState = rememberDatePickerState(
        initialSelectedDateMillis = state.dueDate?.asEpochMilliseconds(),
    )

    fun toggleDatePicker() {
        isOpen = isOpen.not()
    }

    if (isOpen)
        Dialog(
            properties = DialogProperties(),
            onDismissRequest = ::toggleDatePicker
        ) {
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(5))
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(5))
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.25f))
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    DatePicker(
                        state = pickerState,
                        title = {
                            Text(
                                text = "Set Date",
                                style = MaterialTheme.typography.titleLarge
                            )
                        },
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                    ) {
                        OutlinedButton(
                            modifier = Modifier.weight(1f),
                            onClick = ::toggleDatePicker
                        ) {
                            Text(
                                text = "Cancel",
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                        Spacer(modifier = Modifier.padding(16.dp))
                        Button(
                            modifier = Modifier.weight(1f),
                            enabled = pickerState.selectedDateMillis != null,
                            onClick = {
                                val time = pickerState.selectedDateMillis
                                if (time != null) {
                                    val instant = Instant.fromEpochMilliseconds(time)
                                    onValueChangeDate(instant.LocalDateTime)
                                }
                                toggleDatePicker()
                            }) {
                            Text(text = "Confirm")
                        }
                    }
                }
            }
        }

    Card(
        modifier = modifier,
        onClick = ::toggleDatePicker,
        shape = RoundedCornerShape(0),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground
        )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.CalendarMonth,
                contentDescription = "date"
            )
            Text(
                modifier = Modifier.padding(start = 8.dp, top = 2.dp),
                fontWeight = FontWeight.Bold,
                text = state.dueDate?.let {
                    buildString {
                        append(
                            it.dayOfWeek.name.lowercase().take(3)
                                .replaceFirstChar { it.uppercase() })
                        append(", ")
                        append(it.dayOfMonth)
                        append(" ")
                        append(
                            it.month.name.lowercase().take(3).replaceFirstChar { it.uppercase() })
                        append(" ")
                        append(it.year)
                    }
                } ?: "Date")
        }
    }
}