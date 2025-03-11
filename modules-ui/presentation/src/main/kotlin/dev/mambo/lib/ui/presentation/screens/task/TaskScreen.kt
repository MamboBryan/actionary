package dev.mambo.lib.ui.presentation.screens.task

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import dev.mambo.lib.ui.presentation.components.BackButtonComponent
import dev.mambo.lib.ui.presentation.components.LoadingComponent
import dev.mambo.lib.ui.presentation.helpers.ItemUiState
import dev.mambo.lib.ui.presentation.screens.task.components.TaskAction
import dev.mambo.lib.ui.presentation.screens.task.components.TaskEditSection
import dev.mambo.lib.ui.presentation.screens.task.components.TaskValue
import dev.mambo.lib.ui.presentation.screens.task.components.TaskViewSection
import kotlinx.datetime.LocalDateTime

open class TaskScreen(val id: Int? = null) : Screen {
    object TestTags {

    }

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        val screenModel: TaskScreenModel = getScreenModel()
        screenModel.onValueChangeId(id = id)
        val state by screenModel.state.collectAsStateWithLifecycle()
        TaskScreenContent(
            state = state,
            onClickNavigateBack = { navigator?.pop() },
            onClickTaskAction = screenModel::onClickTaskAction,
            onValueChangeTask = screenModel::onValueChangeTask,
            onValueChangeDate = screenModel::onValueChangeDate,
            onDismissDialog = screenModel::onDismissDialog
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreenContent(
    state: TaskScreenState,
    onClickNavigateBack: () -> Unit,
    onClickTaskAction: (TaskAction) -> Unit,
    onValueChangeTask: (TaskValue, String) -> Unit,
    onValueChangeDate: (LocalDateTime) -> Unit,
    onDismissDialog: () -> Unit,
) {

    AnimatedVisibility(visible = state.actionState != null) {
        Dialog(onDismissRequest = onDismissDialog) { }
        if (state.actionState != null)
            when (state.actionState) {
                is ItemUiState.Error -> {
                    Column(modifier = Modifier.fillMaxWidth(0.75f)) {
                        Text(
                            text = "Error",
                            modifier = Modifier.padding(bottom = 8.dp),
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(text = state.actionState.message)
                        Button(
                            modifier = Modifier.padding(vertical = 16.dp),
                            onClick = onDismissDialog
                        ) {
                            Text(text = "Dismiss")
                        }
                    }
                }

                ItemUiState.Loading -> {
                    LoadingComponent(modifier = Modifier.size(200.dp))
                }

                is ItemUiState.Success -> {
                    Column(modifier = Modifier.fillMaxWidth(0.75f)) {
                        Text(
                            text = "Success",
                            modifier = Modifier.padding(bottom = 8.dp),
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(text = if (state.id == null) "Task Created Successfully" else "Task Updates Successfully")
                        Button(
                            modifier = Modifier.padding(vertical = 16.dp),
                            onClick = onDismissDialog
                        ) {
                            Text(text = "Dismiss")
                        }
                    }
                }
            }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = { BackButtonComponent(onClick = onClickNavigateBack) },
                title = {
                    Text(text = "Task")
                }
            )
        }
    ) { innerPadding ->
        AnimatedContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            targetState = state.id
        ) { id ->
            when {
                id == null || state.editing == true -> TaskEditSection(
                    state = state,
                    onValueChangeTask = onValueChangeTask,
                    onClickTaskAction = onClickTaskAction,
                    onValueChangeDate = onValueChangeDate
                )

                else -> TaskViewSection(state = state.result)
            }
        }
    }
}
