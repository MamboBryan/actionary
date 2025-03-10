package dev.mambo.lib.ui.presentation.screens.tasks

import cafe.adriel.voyager.core.model.StateScreenModel
import dev.mambo.lib.data.domain.helpers.LocalDateTime
import dev.mambo.lib.data.domain.models.TaskDomain
import dev.mambo.lib.data.domain.repositories.TaskRepository
import kotlinx.coroutines.flow.update
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime

data class TasksScreenState(
    val date: LocalDateTime = Clock.System.now().LocalDateTime,
    val task: TaskDomain? = null,
)

class TasksScreenModel(
    private val repository: TaskRepository,
) : StateScreenModel<TasksScreenState>(TasksScreenState()) {
    private val _tasks = repository.getTasks()
    val tasks = _tasks

    fun onTaskClicked(task: TaskDomain) {
        mutableState.update { it.copy(task = task) }
    }
}
