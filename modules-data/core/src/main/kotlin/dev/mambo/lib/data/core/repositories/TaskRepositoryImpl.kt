package dev.mambo.lib.data.core.repositories

import dev.mambo.lib.data.core.mappers.asDataResult
import dev.mambo.lib.data.core.mappers.toTaskCache
import dev.mambo.lib.data.core.mappers.toTaskDomain
import dev.mambo.lib.data.domain.helpers.DataResult
import dev.mambo.lib.data.domain.helpers.LocalDateTime
import dev.mambo.lib.data.domain.helpers.asEpochMilliseconds
import dev.mambo.lib.data.domain.models.PriorityDomain
import dev.mambo.lib.data.domain.models.TaskDomain
import dev.mambo.lib.data.domain.repositories.TaskRepository
import dev.mambo.lib.local.caches.TaskCache
import dev.mambo.lib.local.sources.TaskLocalSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime

@OptIn(ExperimentalCoroutinesApi::class)
class TaskRepositoryImpl(
    private val local: TaskLocalSource
) : TaskRepository {

    override suspend fun getTask(id: Int): Flow<TaskDomain?> {
        return local.getFlow(id = id).mapLatest { it?.toTaskDomain() }
    }

    override suspend fun getTasks(): Flow<List<TaskDomain>> {
        return local.getAllAsFlow().mapLatest { list -> list.map { it.toTaskDomain() } }
    }

    override suspend fun createTask(
        title: String,
        description: String,
        dueAt: LocalDateTime?,
        priority: PriorityDomain?
    ): DataResult<TaskDomain> {
        val now = Clock.System.now().LocalDateTime.asEpochMilliseconds()
        val task = TaskCache(
            id = 0,
            title = title,
            description = description,
            createdAt = now,
            updatedAt = now,
            dueAt = dueAt?.asEpochMilliseconds(),
            priority = priority?.name,
            completedAt = null
        )
        return local.insert(task = task).asDataResult { it.toTaskDomain() }
    }

    override suspend fun updateTask(task: TaskDomain): DataResult<TaskDomain> {
        val update = task.copy(updatedAt = Clock.System.now().LocalDateTime)
        return local.update(task = update.toTaskCache()).asDataResult { it.toTaskDomain() }
    }

    override suspend fun deleteTask(task: TaskDomain): DataResult<Boolean> {
        return  local.delete(task = task.toTaskCache()).asDataResult { it }
    }

}