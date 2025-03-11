package dev.mambo.lib.ui.presentation.screens.tasks

import dev.mambo.lib.data.domain.helpers.LocalDateTime
import dev.mambo.lib.data.domain.models.TaskDomain
import dev.mambo.lib.data.domain.repositories.TaskRepository
import dev.mambo.lib.ui.presentation.helpers.ListUiState
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TasksScreenModelTest {
    val list =
        (1..10).map {
            TaskDomain(
                id = it,
                title = "Task $it",
                description = "Description $it",
                createdAt = Clock.System.now().LocalDateTime,
                dueAt = null,
                priority = null,
                completedAt = null,
            )
        }
    private val repository =
        mockk<TaskRepository> {
            every { getTasks() } returns flowOf(list)
        }
    private val model = TasksScreenModel(repository = repository)

    @Test
    fun `tasks returns a list of entries`() =
        runTest {
            val result = model.tasks.first()
            assertTrue { result is ListUiState.NotEmpty }
            val actual = (result as ListUiState.NotEmpty).data
            assertContentEquals(list, actual)
        }

    @Test
    fun `onClickTask saves task in the screen's state`() =
        runTest {
            val task = list.random()
            model.onTaskClicked(task = task)
            val actual = model.state.value.task
            assertEquals(task, actual)
        }

    @Test
    fun `task state should be null on init`() =
        runTest {
            val actual = model.state.value.task
            assertEquals(null, actual)
        }
}
