package dev.mambo.lib.ui.presentation.screens.tasks

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import dev.mambo.lib.data.domain.helpers.LocalDateTime
import dev.mambo.lib.data.domain.models.TaskDomain
import dev.mambo.lib.ui.presentation.helpers.ListUiState
import kotlinx.datetime.Clock
import org.junit.Rule
import org.junit.Test

class TasksScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()
    val list = (1..10).map {
        TaskDomain(
            id = it,
            title = "Task $it",
            description = "Description $it",
            createdAt = Clock.System.now().LocalDateTime,
            dueAt = null,
            priority = null,
            completedAt = null
        )
    }

    @Test
    fun shouldShowLoadingIndicator_WhenOnInitCalled(){
        composeTestRule.setContent {
            TasksScreenContent(
                tasks = ListUiState.Loading,
                state = TasksScreenState(),
                onClickTask = {},
                onClickCreateTask = {}
            )
        }
        composeTestRule.onNodeWithTag(TasksScreen.TestTags.Loading).assertIsDisplayed()
    }

    @Test
    fun shouldShowError_WhenGettingTasksFails(){
        composeTestRule.setContent {
            TasksScreenContent(
                tasks = ListUiState.Error(message = "failed getting tasks"),
                state = TasksScreenState(),
                onClickTask = {},
                onClickCreateTask = {}
            )
        }
        composeTestRule.onNodeWithTag(TasksScreen.TestTags.Error).assertIsDisplayed()
    }

    @Test
    fun shouldShowValidErrorMessage_WhenGettingTasksFails(){
        val message = "failed getting tasks"
        composeTestRule.setContent {
            TasksScreenContent(
                tasks = ListUiState.Error(message = message),
                state = TasksScreenState(),
                onClickTask = {},
                onClickCreateTask = {}
            )
        }
        composeTestRule.onNodeWithTag(TasksScreen.TestTags.Error).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TasksScreen.TestTags.ErrorMessage)
            .assertTextEquals(message)
    }

}