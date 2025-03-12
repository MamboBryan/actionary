package dev.mambo.lib.local.sources

import dev.mambo.lib.local.caches.TaskCache
import dev.mambo.lib.local.caches.toTaskEntity
import dev.mambo.lib.local.dao.TaskDAO
import dev.mambo.lib.local.entities.toTaskCache
import dev.mambo.lib.local.helpers.LocalResult
import dev.mambo.lib.local.helpers.safeTransaction
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest

interface TaskLocalSource {
    suspend fun insert(task: TaskCache): LocalResult<TaskCache>

    suspend fun update(task: TaskCache): LocalResult<TaskCache>

    suspend fun delete(task: TaskCache): LocalResult<Boolean>

    suspend fun get(id: Int): LocalResult<TaskCache>

    suspend fun getFlow(id: Int): Flow<TaskCache?>

    fun getAllAsFlow(): Flow<List<TaskCache>>

    fun getAllByPriority(priority: String): Flow<List<TaskCache>>

    fun getAllByCategory(categoryId: Int): Flow<List<TaskCache>>

    fun getAllByPriorityAndCategory(
        priority: String,
        categoryId: Int,
    ): Flow<List<TaskCache>>
}

@OptIn(ExperimentalCoroutinesApi::class)
internal class TaskLocalSourceImpl(
    private val dispatcher: CoroutineDispatcher,
    private val dao: TaskDAO,
) : TaskLocalSource {
    override suspend fun insert(task: TaskCache): LocalResult<TaskCache> =
        safeTransaction(dispatcher) {
            val id = dao.insert(task.toTaskEntity()).toInt()
            dao.fetchById(id = id)?.toTaskCache() ?: throw Exception("Task not found")
        }

    override suspend fun update(task: TaskCache): LocalResult<TaskCache> =
        safeTransaction(dispatcher) {
            dao.update(task.toTaskEntity())
            dao.fetchById(id = task.id)?.toTaskCache() ?: throw Exception("Task not found")
        }

    override suspend fun delete(task: TaskCache): LocalResult<Boolean> =
        safeTransaction(dispatcher) {
            dao.delete(task.toTaskEntity())
            true
        }

    override suspend fun get(id: Int): LocalResult<TaskCache> =
        safeTransaction(dispatcher) {
            dao.fetchById(id = id)?.toTaskCache() ?: throw Exception("Task not found")
        }

    override suspend fun getFlow(id: Int): Flow<TaskCache?> = dao.fetchByIdFlow(id = id).mapLatest { it?.toTaskCache() }

    override fun getAllAsFlow(): Flow<List<TaskCache>> = dao.fetchAllWithCategory().mapLatest { list -> list.map { it.toTaskCache() } }

    override fun getAllByPriority(priority: String) =
        dao.fetchAllWithCategoryByPriority(priority = priority)
            .mapLatest { list -> list.map { it.toTaskCache() } }

    override fun getAllByCategory(categoryId: Int): Flow<List<TaskCache>> =
        dao.fetchAllWithCategoryByCategory(categoryId = categoryId)
            .mapLatest { list -> list.map { it.toTaskCache() } }

    override fun getAllByPriorityAndCategory(
        priority: String,
        categoryId: Int,
    ): Flow<List<TaskCache>> =
        dao.fetchAllWithCategoryByPriorityAndCategory(priority = priority, categoryId = categoryId)
            .mapLatest { list -> list.map { it.toTaskCache() } }
}
