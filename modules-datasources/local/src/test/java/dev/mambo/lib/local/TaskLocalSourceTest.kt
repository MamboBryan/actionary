package dev.mambo.lib.local

import dev.mambo.lib.local.caches.TaskCache
import dev.mambo.lib.local.helpers.LocalResult
import dev.mambo.lib.local.sources.TaskLocalSource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class TaskLocalSourceTest {
    val tasks =
        (1..10).map {
            TaskCache(
                id = it,
                title = "title $it",
                description = "description $it",
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis(),
                completedAt = null,
                dueAt = null,
            )
        }

    val source = mockk<TaskLocalSource>()

    @Test
    fun `insert should add a task to the database`() =
        runTest {
            coEvery { source.insert(tasks.first()) }.answers { LocalResult.Success(tasks.first()) }
            val result = source.insert(tasks.first())
            assert(result is LocalResult.Success)
            val value = (result as LocalResult.Success).data
            assertEquals(tasks.first(), value)
        }

    @Test
    fun `update should change a task in the database`() =
        runTest {
            val update =
                tasks.random().copy(
                    title = "updated",
                    description = "updates description",
                    updatedAt = System.currentTimeMillis(),
                )
            coEvery { source.update(task = update) }.answers { LocalResult.Success(update) }
            val result = source.update(task = update)
            assert(result is LocalResult.Success)
            val value = (result as LocalResult.Success).data
            assertEquals(update, value)
        }

    @Test
    fun `delete should remove a task from the database`() =
        runTest {
            val update = tasks.toMutableList()
            update.removeAt(4)
            coEvery { source.delete(task = tasks[4]) }.answers { LocalResult.Success(true) }
            coEvery { source.getAllAsFlow() } returns flowOf(update)
            val result = source.delete(task = tasks[4])
            assert(result is LocalResult.Success)
            val list = source.getAllAsFlow().first()
            assertEquals(update, list)
        }

    @Test
    fun `get should return a task from the database`() =
        runTest {
            val expected = tasks[5]
            coEvery { source.get(id = tasks[5].id) }.answers { LocalResult.Success(tasks[5]) }
            val result = source.get(id = tasks[5].id)
            assert(result is LocalResult.Success)
            val actual = (result as LocalResult.Success).data
            assertEquals(expected, actual)
        }
}
