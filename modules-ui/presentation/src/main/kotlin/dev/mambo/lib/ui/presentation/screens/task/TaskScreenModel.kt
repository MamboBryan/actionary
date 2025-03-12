package dev.mambo.lib.ui.presentation.screens.task

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import dev.mambo.lib.data.domain.helpers.DataResult
import dev.mambo.lib.data.domain.helpers.LocalDateTime
import dev.mambo.lib.data.domain.models.CategoryDomain
import dev.mambo.lib.data.domain.models.PriorityDomain
import dev.mambo.lib.data.domain.models.TaskDomain
import dev.mambo.lib.data.domain.repositories.CategoryRepository
import dev.mambo.lib.data.domain.repositories.TaskRepository
import dev.mambo.lib.ui.presentation.helpers.ItemUiState
import dev.mambo.lib.ui.presentation.screens.task.components.TaskAction
import dev.mambo.lib.ui.presentation.screens.task.components.TaskValue
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime

data class TaskScreenState(
    val id: Int? = null,
    val updated: Boolean = false,
    val editing: Boolean = false,
    val actionState: ItemUiState<Boolean>? = null,
    val navigateBack: Boolean = false,
    val task: TaskDomain? = null,
    val title: String = "",
    val description: String = "",
    val dueDate: LocalDateTime? = null,
    val priority: PriorityDomain? = null,
    val categoryName: String = "",
    val category: CategoryDomain? = null,
    val result: ItemUiState<TaskDomain> = ItemUiState.Loading,
    val categories: List<CategoryDomain> = emptyList(),
    val categoryResult: ItemUiState<CategoryDomain>? = null,
) {
    val isActionEnabled
        get() =
            when {
                id == null -> title.isNotBlank() && description.isNotBlank()
                else -> true
            }
}

class TaskScreenModel(
    private val taskRepository: TaskRepository,
    private val categoryRepository: CategoryRepository,
) : StateScreenModel<TaskScreenState>(TaskScreenState()) {
    init {
        observeCategories()
    }

    fun observeCategories() {
        screenModelScope.launch {
            categoryRepository.getAllFlow().collectLatest {
                mutableState.update { state -> state.copy(categories = it) }
            }
        }
    }

    fun onValueChangeId(id: Int?) {
        if (state.value.updated.not()) {
            mutableState.update { it.copy(id = id, updated = true, editing = false) }
            getTask()
        }
    }

    fun onClickTaskAction(action: TaskAction) {
        screenModelScope.launch {
            when (action) {
                TaskAction.CREATE -> createTask()
                TaskAction.UPDATE -> updateTask()
                TaskAction.DELETE -> deleteTask()
            }
        }
    }

    fun onValueChangeTask(
        value: TaskValue,
        update: String,
    ) {
        when (value) {
            TaskValue.TITLE -> mutableState.update { it.copy(title = update) }
            TaskValue.DESCRIPTION -> mutableState.update { it.copy(description = update) }
            TaskValue.CATEGORY -> mutableState.update { it.copy(categoryName = update) }
        }
    }

    fun onValueChangeDate(date: LocalDateTime) {
        mutableState.update { it.copy(dueDate = date) }
    }

    fun onValueChangePriority(priority: PriorityDomain?) {
        mutableState.update { it.copy(priority = priority) }
    }

    fun onValueChangeCategory(category: CategoryDomain?) {
        mutableState.update { it.copy(category = if (it.category == category) null else category) }
    }

    fun onClickEditTask() {
        val task = mutableState.value.task ?: return
        mutableState.update {
            it.copy(
                editing = true,
                title = task.title,
                description = task.description,
                dueDate = task.dueAt,
                priority = task.priority,
                category = task.category,
            )
        }
    }

    fun onClickCompleteTask() {
        updateTask(date = Clock.System.now().LocalDateTime)
    }

    fun onClickCreateCategory() {
        val name = state.value.categoryName
        if (name.isBlank()) return
        screenModelScope.launch {
            mutableState.update { it.copy(categoryResult = ItemUiState.Loading) }
            val result = categoryRepository.create(name = name)
            when (result) {
                is DataResult.Error -> {
                    mutableState.update {
                        it.copy(
                            categoryResult = ItemUiState.Error(result.message),
                        )
                    }
                }

                is DataResult.Success ->
                    mutableState.update {
                        it.copy(categoryResult = ItemUiState.Success(result.data))
                    }
            }
            delay(500)
            mutableState.update { it.copy(categoryResult = null, categoryName = "") }
        }
    }

    fun onDismissDialog() {
        mutableState.update { it.copy(actionState = null) }
    }

    private fun getTask() {
        screenModelScope.launch {
            val id = state.value.id ?: return@launch
            taskRepository.getTask(id = id).collectLatest {
                mutableState.update { state ->
                    state.copy(
                        task = it,
                        result =
                            if (it == null) {
                                ItemUiState.Error("Task not found")
                            } else {
                                ItemUiState.Success(
                                    it,
                                )
                            },
                    )
                }
            }
        }
    }

    private fun createTask() {
        val state = state.value
        screenModelScope.launch {
            mutableState.update { it.copy(actionState = ItemUiState.Loading) }
            val result =
                taskRepository.createTask(
                    title = state.title,
                    description = state.description,
                    dueAt = state.dueDate,
                    priority = state.priority,
                    category = state.category,
                )
            when (result) {
                is DataResult.Error -> {
                    mutableState.update { it.copy(actionState = ItemUiState.Error(result.message)) }
                }

                is DataResult.Success -> {
                    val task = result.data
                    mutableState.update {
                        it.copy(
                            id = task.id,
                            actionState = null,
                            task = task,
                            result = ItemUiState.Success(task),
                        )
                    }
                }
            }
        }
    }

    private fun updateTask(date: LocalDateTime? = null) {
        val state = state.value
        val task = state.task ?: return
        val update =
            task.copy(
                title = state.title,
                description = state.description,
                dueAt = state.dueDate,
                priority = state.priority,
                completedAt = date,
                category = state.category,
            )
        screenModelScope.launch {
            mutableState.update { it.copy(actionState = ItemUiState.Loading) }
            val result = taskRepository.updateTask(task = update)
            when (result) {
                is DataResult.Error -> {
                    mutableState.update { it.copy(actionState = ItemUiState.Error(result.message)) }
                }

                is DataResult.Success -> {
                    val task = result.data
                    mutableState.update {
                        it.copy(
                            actionState = null,
                            task = task,
                            editing = false,
                            result = ItemUiState.Success(task),
                        )
                    }
                }
            }
        }
    }

    private fun deleteTask() {
        val task = state.value.task ?: return
        screenModelScope.launch {
            mutableState.update {
                it.copy(actionState = ItemUiState.Loading)
            }
            val result = taskRepository.deleteTask(task = task)
            when (result) {
                is DataResult.Error -> {
                    mutableState.update { it.copy(actionState = ItemUiState.Error(result.message)) }
                }

                is DataResult.Success -> {
                    mutableState.update { it.copy(actionState = null, navigateBack = true) }
                }
            }
        }
    }
}
