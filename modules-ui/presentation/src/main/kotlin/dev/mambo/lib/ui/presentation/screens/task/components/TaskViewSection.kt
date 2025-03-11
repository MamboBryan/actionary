package dev.mambo.lib.ui.presentation.screens.task.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.OutlinedFlag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.mambo.lib.data.domain.models.TaskDomain
import dev.mambo.lib.ui.presentation.components.CenteredColumn
import dev.mambo.lib.ui.presentation.components.LoadingComponent
import dev.mambo.lib.ui.presentation.helpers.ItemUiState

@Composable
fun TaskViewSection(
    state: ItemUiState<TaskDomain>,
    modifier: Modifier = Modifier,
    onClickCompleteTask: () -> Unit,
) {
    AnimatedContent(
        modifier = modifier,
        targetState = state
    ) { result ->
        when (result) {
            ItemUiState.Loading -> {
                LoadingComponent(modifier = Modifier.fillMaxSize())
            }

            is ItemUiState.Error -> {
                CenteredColumn(modifier = Modifier.fillMaxSize()) {
                    Text(text = "Error")
                    Text(text = result.message)
                }
            }

            is ItemUiState.Success -> {
                val task = result.data
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(bottom = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (task.dueAt != null) {
                            Row(
                                modifier = Modifier,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.CalendarMonth,
                                    contentDescription = "date"
                                )
                                Text(
                                    modifier = Modifier.padding(start = 8.dp, top = 2.dp),
                                    fontWeight = FontWeight.Bold,
                                    text = task.dueAt?.let {
                                        buildString {
                                            append(
                                                it.dayOfWeek.name.lowercase().take(3)
                                                    .replaceFirstChar { it.uppercase() })
                                            append(", ")
                                            append(it.dayOfMonth)
                                            append(" ")
                                            append(
                                                it.month.name.lowercase().take(3)
                                                    .replaceFirstChar { it.uppercase() })
                                            append(" ")
                                            append(it.year)
                                        }
                                    } ?: "Date")
                            }
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        if (task.priority != null) {
                            Row(
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    modifier = Modifier,
                                    fontWeight = FontWeight.Bold,
                                    text = task.priority?.label ?: "Priority"
                                )
                                Icon(
                                    modifier = Modifier.padding(start = 8.dp),
                                    imageVector = Icons.Outlined.OutlinedFlag,
                                    contentDescription = "priority"
                                )
                            }
                        }
                    }
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = result.data.title,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        modifier = Modifier.padding(top = 16.dp),
                        text = "Description",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.labelLarge
                    )
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        text = result.data.description
                    )
                    if (task.updatedAt != task.createdAt) {
                        Row(
                            modifier = Modifier.padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                modifier = Modifier.size(16.dp),
                                imageVector = Icons.Filled.Edit,
                                contentDescription = "date"
                            )
                            Text(
                                style = MaterialTheme.typography.labelMedium,
                                modifier = Modifier.padding(start = 8.dp, top = 2.dp),
                                text = task.updatedAt.let {
                                    buildString {
                                        append("Edited : ")
                                        append(it.dayOfMonth)
                                        append("/")
                                        append(it.monthNumber)
                                        append("/")
                                        append(it.year)
                                    }
                                }
                            )
                        }
                    }
                    Button(
                        enabled = task.completedAt == null,
                        modifier = Modifier.fillMaxWidth(),
                        onClick = onClickCompleteTask,
                        shape = RoundedCornerShape(25),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF62C370),
                            contentColor = Color(0xFF000000)
                        )
                    ) {
                        val completedAt = task.completedAt
                        Text(text = if (completedAt == null) "Complete" else completedAt.let {
                            buildString {
                                append("Completed on ")
                                append(
                                    it.dayOfWeek.name.lowercase().take(3)
                                        .replaceFirstChar { it.uppercase() })
                                append(" ")
                                append(it.dayOfMonth)
                                append(" ")
                                append(
                                    it.month.name.lowercase().take(3)
                                        .replaceFirstChar { it.uppercase() })
                                append(" ")
                                append(it.year)
                            }
                        })
                    }

                }
            }
        }
    }
}