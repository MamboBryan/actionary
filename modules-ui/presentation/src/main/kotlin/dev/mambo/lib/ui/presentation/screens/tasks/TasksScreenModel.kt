package dev.mambo.lib.ui.presentation.screens.tasks

import android.R.attr.category
import android.R.attr.priority
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import dev.mambo.lib.data.domain.helpers.LocalDateTime
import dev.mambo.lib.data.domain.models.CategoryDomain
import dev.mambo.lib.data.domain.models.PriorityDomain
import dev.mambo.lib.data.domain.models.TaskDomain
import dev.mambo.lib.data.domain.repositories.CategoryRepository
import dev.mambo.lib.data.domain.repositories.TaskRepository
import dev.mambo.lib.ui.presentation.helpers.ListUiState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime

data class TasksScreenState(
    val date: LocalDateTime = Clock.System.now().LocalDateTime,
    val task: TaskDomain? = null,
    val category: CategoryDomain? = null,
    val categories: List<CategoryDomain> = emptyList(),
    val priority: PriorityDomain? = null,
    val priorities: List<PriorityDomain> = PriorityDomain.entries.toList(),
)

@OptIn(ExperimentalCoroutinesApi::class)
class TasksScreenModel(
    private val taskRepository: TaskRepository,
    private val categoryRepository: CategoryRepository,
) : StateScreenModel<TasksScreenState>(TasksScreenState()) {
    private val categoryState = MutableStateFlow<CategoryDomain?>(null)
    private val priorityState = MutableStateFlow<PriorityDomain?>(null)

    private val _tasks =
        combine(priorityState, categoryState) { priority, category ->
            mutableState.update { it.copy(priority = priority, category = category) }
            when {
                priority != null && category != null ->
                    taskRepository.getTasksByPriorityAndCategory(
                        priority = priority,
                        category = category,
                    )

                priority != null -> taskRepository.getTasksByPriority(priority = priority)
                category != null -> taskRepository.getTasksByCategory(category = category)
                else -> taskRepository.getTasks()
            }
        }.flatMapLatest { it }
    val tasks get() =
        _tasks.mapLatest { tasks ->
            when {
                tasks.isEmpty() -> ListUiState.Empty
                else -> ListUiState.NotEmpty(tasks)
            }
        }

    init {
        observeCategories()
    }

    private fun observeCategories() {
        screenModelScope.launch {
            categoryRepository.getAllFlow().collect { categories ->
                mutableState.update { it.copy(categories = categories) }
            }
        }
    }

    fun onTaskClicked(task: TaskDomain) {
        screenModelScope.launch {
            mutableState.update { it.copy(task = task) }
            delay(500)
            mutableState.update { it.copy(task = null) }
        }
    }

    fun onValueChangeCategory(category: CategoryDomain?) {
        categoryState.update { if (it == category) null else category }
    }

    fun onValueChangePriority(priority: PriorityDomain?) {
        priorityState.update { if (it == priority) null else priority }
    }
}
