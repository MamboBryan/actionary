package dev.mambo.lib.ui.presentation.screens.task.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.mambo.lib.ui.presentation.screens.task.TaskScreenState

enum class TaskAction {
    CREATE, UPDATE, DELETE
}

enum class TaskValue {
    TITLE, DESCRIPTION
}

@Composable
fun TaskEditSection(
    state: TaskScreenState,
    modifier: Modifier = Modifier,
    onClickTaskAction: (TaskAction) -> Unit,
    onValueChangeTask: (TaskValue, String) -> Unit,
) {
    Column(modifier = modifier) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(text = "Date : ")
            Spacer(modifier = Modifier.weight(1f))
            Text(text = "Priority : ")
        }
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.title,
            onValueChange = { onValueChangeTask(TaskValue.TITLE, it) },
            label = { Text(text = "Title") }
        )
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            value = state.description,
            onValueChange = { onValueChangeTask(TaskValue.DESCRIPTION, it) },
            label = { Text(text = "Description") }
        )
        HorizontalDivider()
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                modifier = Modifier.weight(1f),
                onClick = {
                    when (state.task) {
                        null -> onClickTaskAction(TaskAction.CREATE)
                        else -> onClickTaskAction(TaskAction.UPDATE)
                    }
                }
            ) {
                when (state.task) {
                    null -> Text(text = "create")
                    else -> Text(text = "update")
                }
            }

            AnimatedVisibility(visible = state.task != null) {
                IconButton(
                    onClick = { onClickTaskAction(TaskAction.DELETE) },
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError
                    )
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Delete,
                        contentDescription = "delete"
                    )
                }
            }

        }
    }
}