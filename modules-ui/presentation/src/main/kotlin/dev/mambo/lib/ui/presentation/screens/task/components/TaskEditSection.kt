package dev.mambo.lib.ui.presentation.screens.task.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.mambo.lib.data.domain.models.PriorityDomain
import dev.mambo.lib.ui.presentation.screens.task.TaskScreenState
import kotlinx.datetime.LocalDateTime

enum class TaskAction {
    CREATE,
    UPDATE,
    DELETE,
}

enum class TaskValue {
    TITLE,
    DESCRIPTION,
    CATEGORY,
}

@Composable
fun TaskEditSection(
    state: TaskScreenState,
    modifier: Modifier = Modifier,
    onClickTaskAction: (TaskAction) -> Unit,
    onValueChangeTask: (TaskValue, String) -> Unit,
    onValueChangeDate: (LocalDateTime) -> Unit,
    onValueChangePriority: (PriorityDomain) -> Unit,
) {
    Column(modifier = modifier) {
        Row(modifier = Modifier.fillMaxWidth()) {
            TaskDateComponent(state = state, onValueChangeDate = onValueChangeDate)
            Spacer(modifier = Modifier.weight(1f))
            TaskPriorityComponent(state = state, onValueChangePriority = onValueChangePriority)
        }
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.title,
            onValueChange = { onValueChangeTask(TaskValue.TITLE, it) },
            label = { Text(text = "Title") },
            colors =
                OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.background,
                    unfocusedContainerColor = MaterialTheme.colorScheme.background,
                    focusedBorderColor = MaterialTheme.colorScheme.background,
                    unfocusedBorderColor = MaterialTheme.colorScheme.background,
                    focusedTextColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                    cursorColor = MaterialTheme.colorScheme.onBackground,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.primary,
                ),
        )
        TextField(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .weight(1f),
            value = state.description,
            onValueChange = { onValueChangeTask(TaskValue.DESCRIPTION, it) },
            label = { Text(text = "Description") },
            colors =
                OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.background,
                    unfocusedContainerColor = MaterialTheme.colorScheme.background,
                    focusedBorderColor = MaterialTheme.colorScheme.background,
                    unfocusedBorderColor = MaterialTheme.colorScheme.background,
                    focusedTextColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                    cursorColor = MaterialTheme.colorScheme.onBackground,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.primary,
                ),
        )
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Button(
                enabled = state.isActionEnabled,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(25),
                onClick = {
                    when (state.task) {
                        null -> onClickTaskAction(TaskAction.CREATE)
                        else -> onClickTaskAction(TaskAction.UPDATE)
                    }
                },
            ) {
                when (state.task) {
                    null -> Text(text = "create")
                    else -> Text(text = "update")
                }
            }

            AnimatedVisibility(visible = state.task != null) {
                SmallFloatingActionButton(
                    onClick = { onClickTaskAction(TaskAction.DELETE) },
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError,
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Delete,
                        contentDescription = "delete",
                    )
                }
            }
        }
    }
}
