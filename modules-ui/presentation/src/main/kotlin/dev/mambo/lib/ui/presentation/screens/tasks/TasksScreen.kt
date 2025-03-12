package dev.mambo.lib.ui.presentation.screens.tasks

import android.R.attr.onClick
import android.R.attr.text
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.outlined.OutlinedFlag
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ClearAll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import dev.mambo.lib.data.domain.helpers.LocalDateTime
import dev.mambo.lib.data.domain.models.CategoryDomain
import dev.mambo.lib.data.domain.models.PriorityDomain
import dev.mambo.lib.data.domain.models.TaskDomain
import dev.mambo.lib.ui.design.theme.ActionaryTheme
import dev.mambo.lib.ui.presentation.components.CenteredColumn
import dev.mambo.lib.ui.presentation.helpers.ListUiState
import dev.mambo.lib.ui.presentation.screens.task.TaskScreen
import dev.mambo.lib.ui.presentation.screens.task.components.FilterCategory
import dev.mambo.lib.ui.presentation.screens.task.components.label
import dev.mambo.lib.ui.presentation.screens.tasks.components.FilterPriority
import kotlinx.datetime.Clock

object TasksScreen : Screen {
    data object TestTags {
        const val LOADING = "Tasks.Loading"
        const val ERROR = "Tasks.Error"
        const val EMPTY = "Tasks.Empty"
        const val NOT_EMPTY = "Tasks.NotEmpty"
        const val ERROR_MESSAGE = "Tasks.Error.Message"
    }

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        val screenModel: TasksScreenModel = getScreenModel()
        val state by screenModel.state.collectAsStateWithLifecycle()
        val tasks by screenModel.tasks.collectAsStateWithLifecycle(ListUiState.Loading)
        TasksScreenContent(
            state = state,
            tasks = tasks,
            onClickTask = screenModel::onTaskClicked,
            onCheckTask = screenModel::onCheckTask,
            onClickCreateTask = { navigator?.push(TaskScreen()) },
            onValueChangeCategory = screenModel::onValueChangeCategory,
            onValueChangePriority = screenModel::onValueChangePriority,
            onClickClearFilters = screenModel::onClickClearFilters,
            navigate = { screen -> navigator?.push(screen) },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreenContent(
    tasks: ListUiState<TaskDomain>,
    state: TasksScreenState,
    onClickTask: (TaskDomain) -> Unit,
    onCheckTask: (TaskDomain) -> Unit,
    onClickCreateTask: () -> Unit,
    onValueChangeCategory: (CategoryDomain?) -> Unit,
    onValueChangePriority: (PriorityDomain?) -> Unit,
    onClickClearFilters: () -> Unit = {},
    navigate: (Screen) -> Unit,
) {
    if (state.task != null) navigate(TaskScreen(id = state.task.id))

    Scaffold(
        topBar = {
            TasksTopAppBar(
                state = state,
                onValueChangeCategory = onValueChangeCategory,
                onValueChangePriority = onValueChangePriority,
                onClickClearFilters = onClickClearFilters,
            )
        },
        floatingActionButton = {
            AnimatedVisibility(visible = tasks is ListUiState.NotEmpty) {
                FloatingActionButton(
                    onClick = onClickCreateTask,
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Add,
                        contentDescription = "create task",
                    )
                }
            }
        },
    ) { innerPadding ->
        AnimatedContent(
            modifier =
            Modifier
                .fillMaxSize()
                .padding(innerPadding),
            targetState = tasks,
        ) { result ->
            when (result) {
                ListUiState.Empty -> {
                    CenteredColumn(
                        modifier =
                        Modifier
                            .fillMaxSize()
                            .testTag(TasksScreen.TestTags.EMPTY),
                    ) {
                        Text(text = "No tasks found")
                        Button(
                            modifier = Modifier.padding(vertical = 16.dp),
                            onClick = onClickCreateTask,
                        ) {
                            Text(text = "create")
                        }
                    }
                }

                ListUiState.Loading -> {
                    CenteredColumn(
                        modifier =
                        Modifier
                            .fillMaxSize()
                            .testTag(TasksScreen.TestTags.LOADING),
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is ListUiState.Error -> {
                    CenteredColumn(
                        modifier =
                        Modifier
                            .fillMaxSize()
                            .testTag(TasksScreen.TestTags.ERROR),
                    ) {
                        Text(
                            modifier = Modifier.padding(vertical = 16.dp),
                            text = "Error",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                        )
                        Text(
                            modifier = Modifier.testTag(TasksScreen.TestTags.ERROR_MESSAGE),
                            text = result.message,
                        )
                    }
                }

                is ListUiState.NotEmpty -> {
                    LazyColumn(
                        modifier =
                        Modifier
                            .fillMaxSize()
                            .testTag(TasksScreen.TestTags.NOT_EMPTY),
                    ) {
                        items(result.data.size) { index ->
                            val task = result.data[index]
                            TaskItem(
                                modifier = Modifier.fillMaxWidth(),
                                task = task,
                                onClick = { onClickTask(task) },
                                onCheck = { onCheckTask(task) },
                            )
                        }
                        item {
                            Spacer(modifier = Modifier.padding(vertical = 64.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun TasksTopAppBar(
    state: TasksScreenState,
    onValueChangeCategory: (CategoryDomain?) -> Unit,
    onValueChangePriority: (PriorityDomain?) -> Unit,
    onClickClearFilters: () -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = 4.dp,
    ) {
        val padding = TopAppBarDefaults.windowInsets.asPaddingValues()
        Column(
            modifier =
            Modifier
                .fillMaxWidth()
                .padding(
                    top = padding.calculateTopPadding(),
                    bottom = 16.dp,
                ),
        ) {
            Text(
                modifier = Modifier.padding(top = 8.dp, start = 16.dp, bottom = 8.dp),
                text = "Actionary",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                LazyRow(modifier = Modifier.weight(1f)) {
                    item {
                        FilterCategory(
                            modifier = Modifier.padding(start = 16.dp),
                            category = state.category,
                            categories = state.categories,
                            onItemSelected = onValueChangeCategory,
                        )
                    }
                    item {
                        FilterPriority(
                            modifier = Modifier.padding(start = 16.dp),
                            priority = state.priority,
                            priorities = state.priorities,
                            onItemSelected = onValueChangePriority,
                        )
                    }
                }
                AnimatedVisibility(visible = state.priority != null || state.category != null) {
                    SmallFloatingActionButton(
                        modifier = Modifier.padding(horizontal = 8.dp),
                        onClick = onClickClearFilters,
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError,
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.ClearAll,
                                contentDescription = "clear filter",
                            )
                            Text(modifier = Modifier.padding(start = 8.dp), text = "Clear")
                        }

                    }
                }
            }
        }
    }
}

@Composable
fun TaskItem(
    task: TaskDomain,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    onCheck: () -> Unit,
) {
    val background =
        if (task.completedAt != null) MaterialTheme.colorScheme.primary.copy(0.1f) else MaterialTheme.colorScheme.background

    Row(
        modifier =
        Modifier
            .fillMaxWidth()
            .background(background),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(
            modifier = Modifier,
            checked = task.completedAt != null,
            onCheckedChange = { onCheck() },
        )
        Card(
            modifier = modifier,
            onClick = onClick,
            colors =
            CardDefaults.cardColors(
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.onBackground,
            ),
            shape = RoundedCornerShape(0),
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.padding(bottom = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Row(
                            modifier = Modifier,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            val category = task.category
                            if (category != null) {
                                Text(
                                    style = MaterialTheme.typography.labelMedium,
                                    modifier =
                                    Modifier
                                        .padding(end = 8.dp)
                                        .clip(RoundedCornerShape(25))
                                        .background(MaterialTheme.colorScheme.primary)
                                        .padding(horizontal = 8.dp, vertical = 4.dp),
                                    text =
                                    category.name.lowercase()
                                        .replaceFirstChar { it.uppercase() },
                                    color = MaterialTheme.colorScheme.onPrimary,
                                )
                            }

                            if (task.dueAt != null) {
                                Row(
                                    modifier = Modifier,
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Icon(
                                        modifier = Modifier.size(16.dp),
                                        imageVector = Icons.Filled.CalendarMonth,
                                        contentDescription = "date",
                                    )
                                    Text(
                                        style = MaterialTheme.typography.labelMedium,
                                        modifier = Modifier.padding(start = 8.dp, top = 2.dp),
                                        text =
                                        task.dueAt?.let {
                                            buildString {
                                                append(
                                                    it.dayOfWeek.name.lowercase().take(3)
                                                        .replaceFirstChar { it.uppercase() },
                                                )
                                                append(", ")
                                                append(it.dayOfMonth)
                                                append(" ")
                                                append(
                                                    it.month.name.lowercase().take(3)
                                                        .replaceFirstChar { it.uppercase() },
                                                )
                                                append(" ")
                                                append(it.year)
                                            }
                                        } ?: "Date",
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        if (task.priority != null) {
                            Row(
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    style = MaterialTheme.typography.labelMedium,
                                    modifier = Modifier,
                                    text = task.priority?.label ?: "Priority",
                                )
                                Icon(
                                    modifier =
                                    Modifier
                                        .padding(start = 8.dp)
                                        .size(16.dp),
                                    imageVector = Icons.Outlined.OutlinedFlag,
                                    contentDescription = "priority",
                                )
                            }
                        }
                    }
                    Text(
                        text = task.title,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium,
                        textDecoration = if (task.completedAt != null) TextDecoration.LineThrough else null,
                    )
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = task.description,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textDecoration = if (task.completedAt != null) TextDecoration.LineThrough else null,
                    )
                    if (task.updatedAt != task.createdAt) {
                        Text(
                            style = MaterialTheme.typography.labelMedium,
                            modifier = Modifier.padding(top = 8.dp),
                            text =
                            task.updatedAt.let {
                                buildString {
                                    append("Edited : ")
                                    append(it.dayOfMonth)
                                    append("/")
                                    append(it.monthNumber)
                                    append("/")
                                    append(it.year)
                                }
                            },
                        )
                    }
                }

                HorizontalDivider(thickness = 0.5.dp)
            }
        }
    }
}

@Preview
@Composable
private fun TasksScreenLoadingPreview() {
    ActionaryTheme {
        TasksScreenContent(
            tasks = ListUiState.Loading,
            state = TasksScreenState(),
            onClickTask = {},
            onClickCreateTask = {},
            navigate = {},
            onValueChangeCategory = {},
            onValueChangePriority = {},
            onCheckTask = {},
        )
    }
}

@Preview
@Composable
private fun TasksScreenEmptyPreview() {
    ActionaryTheme {
        TasksScreenContent(
            tasks = ListUiState.Empty,
            state = TasksScreenState(),
            onClickTask = {},
            onClickCreateTask = {},
            navigate = {},
            onValueChangeCategory = {},
            onValueChangePriority = {},
            onCheckTask = {},
        )
    }
}

@Preview
@Composable
private fun TasksScreenErrorPreview() {
    ActionaryTheme {
        TasksScreenContent(
            tasks = ListUiState.Error(message = "unable to fetch tasks"),
            state = TasksScreenState(),
            onClickTask = {},
            onClickCreateTask = {},
            navigate = {},
            onValueChangeCategory = {},
            onValueChangePriority = {},
            onCheckTask = {},
        )
    }
}

@Preview
@Composable
private fun TasksScreenNotEmptyPreview() {
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
    ActionaryTheme {
        TasksScreenContent(
            tasks = ListUiState.NotEmpty(list),
            state = TasksScreenState(),
            onClickTask = {},
            onClickCreateTask = {},
            navigate = {},
            onValueChangeCategory = {},
            onValueChangePriority = {},
            onCheckTask = {},
        )
    }
}
