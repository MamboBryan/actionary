package dev.mambo.lib.data.domain.repositories

import dev.mambo.lib.data.domain.helpers.DataResult
import dev.mambo.lib.data.domain.models.CategoryDomain
import dev.mambo.lib.data.domain.models.PriorityDomain
import dev.mambo.lib.data.domain.models.TaskDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime

interface TaskRepository {
    suspend fun getTask(id: Int): Flow<TaskDomain?>

    fun getTasks(): Flow<List<TaskDomain>>

    fun getTasksByCategory(category: CategoryDomain): Flow<List<TaskDomain>>

    fun getTasksByPriority(priority: PriorityDomain): Flow<List<TaskDomain>>

    fun getTasksByPriorityAndCategory(
        priority: PriorityDomain,
        category: CategoryDomain,
    ): Flow<List<TaskDomain>>

    suspend fun createTask(
        title: String,
        description: String,
        dueAt: LocalDateTime? = null,
        priority: PriorityDomain? = null,
        category: CategoryDomain? = null,
    ): DataResult<TaskDomain>

    suspend fun updateTask(task: TaskDomain): DataResult<TaskDomain>

    suspend fun deleteTask(task: TaskDomain): DataResult<Boolean>
}
