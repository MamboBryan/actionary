package dev.mambo.lib.data.core

import dev.mambo.lib.data.domain.helpers.DataResult
import dev.mambo.lib.data.domain.helpers.LocalDateTime
import dev.mambo.lib.data.domain.models.TaskDomain
import dev.mambo.lib.data.domain.repositories.TaskRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TaskRepositoryTest {

    private val repository = mockk<TaskRepository>()

    @Test
    fun `createTask should add a new entry`() = runTest {
        val list = mutableListOf(
            TaskDomain(
                id = 1,
                title = "title",
                description = "description",
                createdAt = Clock.System.now().LocalDateTime,
                updatedAt = Clock.System.now().LocalDateTime,
                completedAt = null,
                dueAt = null,
                priority = null
            )
        )
        coEvery {
            repository.createTask(
                title = "title",
                description = "description",
                dueAt = null,
                priority = null
            )
        } returns DataResult.Success(list.last())
        coEvery { repository.getTasks() } returns flowOf(list)

        val result = repository.createTask(
            title = "title",
            description = "description",
            dueAt = null,
            priority = null
        )
        assertTrue { result is DataResult.Success }
        val task = (result as DataResult.Success).data
        assertEquals(list.last(), task)
        val getResult = repository.getTasks().first()
        assertEquals(list, getResult)
    }

    @Test
    fun `updateTask should update an existing entry`() = runTest {
        val list = mutableListOf(
            TaskDomain(
                id = 1,
                title = "title",
                description = "description",
                createdAt = Clock.System.now().LocalDateTime,
                updatedAt = Clock.System.now().LocalDateTime,
                completedAt = null,
                dueAt = null,
                priority = null
            )
        )
        val update = list.first().copy(title = "new title")
        list.removeLast()
        list.add(update)
        coEvery { repository.updateTask(task = update) } returns DataResult.Success(update)
        coEvery { repository.getTasks() } returns flowOf(list)
        val result = repository.updateTask(task = update)
        assertTrue { result is DataResult.Success }
        val task = (result as DataResult.Success).data
        assertEquals(update, task)
    }

    @Test
    fun `deleteTask should remove an existing entry`() = runTest {
        val list = mutableListOf(
            TaskDomain(
                id = 1,
                title = "title",
                description = "description",
                createdAt = Clock.System.now().LocalDateTime,
                updatedAt = Clock.System.now().LocalDateTime,
                completedAt = null,
                dueAt = null,
                priority = null
            )
        )
        val task = list.first()
        coEvery { repository.deleteTask(task = task) } returns DataResult.Success(true)
        list.removeLast()
        coEvery { repository.getTasks() } returns flowOf(list)
        val result = repository.deleteTask(task = task)
        assertTrue { result is DataResult.Success }
        val getResult = repository.getTasks().first()
        assertContentEquals(emptyList(), getResult)
    }

    @Test
    fun `getTasks should return a list of entries`() = runTest {
        val list = (1..5).map {
            TaskDomain(
                id = it,
                title = "title $it",
                description = "description $it",
                createdAt = Clock.System.now().LocalDateTime,
                updatedAt = Clock.System.now().LocalDateTime,
                completedAt = null,
                dueAt = null,
                priority = null
            )
        }
        coEvery { repository.getTasks() } returns flowOf(list)
        val result = repository.getTasks().first()
        assertContentEquals(list, result)
    }

    @Test
    fun `getTask should return a single entry`() = runTest {
        val list = (1..5).map {
            TaskDomain(
                id = it,
                title = "title $it",
                description = "description $it",
                createdAt = Clock.System.now().LocalDateTime,
                updatedAt = Clock.System.now().LocalDateTime,
                completedAt = null,
                dueAt = null,
                priority = null
            )
        }
        val item = list.random()
        coEvery { repository.getTask(id = item.id) } returns flowOf(list.first { it.id == item.id })
        val result = repository.getTask(id = item.id).first()
        assertEquals(item, result)
    }

}
