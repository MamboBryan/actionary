package dev.mambo.lib.ui.presentation.screens.tasks

import dev.mambo.lib.data.domain.helpers.LocalDateTime
import dev.mambo.lib.data.domain.models.PriorityDomain
import dev.mambo.lib.data.domain.models.TaskDomain
import dev.mambo.lib.data.domain.repositories.CategoryRepository
import dev.mambo.lib.data.domain.repositories.TaskRepository
import dev.mambo.lib.ui.presentation.helpers.ListUiState
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.datetime.Clock
import org.junit.After
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class TasksScreenModelTest {
    val list =
        (1..10).map {
            TaskDomain(
                id = it,
                title = "Task $it",
                description = "Description $it",
                createdAt = Clock.System.now().LocalDateTime,
                dueAt = null,
                priority = PriorityDomain.entries.random(),
                completedAt = null,
            )
        }
    val mediums = list.filter { it.priority == PriorityDomain.MEDIUM }
    private val taskRepository =
        mockk<TaskRepository> {
            every { getTasks() } returns flowOf(list)
            every { getTasksByPriority(priority = PriorityDomain.MEDIUM) } returns flowOf(mediums)
        }
    private val categoryRepository =
        mockk<CategoryRepository> {
            every { getAllFlow() } returns flowOf(emptyList())
        }
    lateinit var model: TasksScreenModel

    val dispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        model =
            TasksScreenModel(
                taskRepository = taskRepository,
                categoryRepository = categoryRepository,
            )
    }

    @After
    fun tearDown() {
        model.onDispose()
        Dispatchers.resetMain()
    }

    @Test
    fun `tasks returns a list of entries`() =
        runTest(dispatcher) {
            val result = model.tasks.first()
            assertTrue { result is ListUiState.NotEmpty }
            val actual = (result as ListUiState.NotEmpty).data
            assertContentEquals(list, actual)
        }

    @Test
    fun `onClickTask saves task and clears it after 500 millis`() =
        runTest {
            val task = list.random()
            model.onTaskClicked(task = task)
            val actual = model.state.value.task
            assertEquals(null, actual)
        }

    @Test
    fun `onValueChangePriority saves selected priority`() =
        runTest {
            model.onValueChangePriority(priority = PriorityDomain.MEDIUM)
            model.tasks.first()
            val actual = model.state.value.priority
            assertEquals(PriorityDomain.MEDIUM, actual)
        }

    @Test
    fun `when priority changes list should contain valid priorities`() =
        runTest {
            model.onValueChangePriority(priority = PriorityDomain.MEDIUM)
            val result = model.tasks.first()
            assertTrue { result is ListUiState.NotEmpty }
            val list = (result as ListUiState.NotEmpty).data
            assertContentEquals(list.filter { it.priority == PriorityDomain.MEDIUM }, list)
        }

    @Test
    fun `task state should be null on init`() =
        runTest {
            val actual = model.state.value.task
            assertEquals(null, actual)
        }
}
