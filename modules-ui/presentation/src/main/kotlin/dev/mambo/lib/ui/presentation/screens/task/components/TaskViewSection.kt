package dev.mambo.lib.ui.presentation.screens.task.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.mambo.lib.data.domain.models.TaskDomain
import dev.mambo.lib.ui.presentation.components.CenteredColumn
import dev.mambo.lib.ui.presentation.components.LoadingComponent
import dev.mambo.lib.ui.presentation.helpers.ItemUiState

@Composable
fun TaskViewSection(
    state: ItemUiState<TaskDomain>,
    modifier: Modifier = Modifier
) {
    AnimatedContent(
        modifier = modifier,
        targetState = state
    ) { result ->
        when (result) {
            ItemUiState.Loading -> {
                LoadingComponent(modifier = Modifier.fillMaxSize())
            }

            is ItemUiState.Error -> {
                CenteredColumn(modifier = Modifier.fillMaxSize()) {
                    Text(text = "Error")
                    Text(text = result.message)
                }
            }

            is ItemUiState.Success -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Text(text = "Title")
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = result.data.title
                    )
                    Text(
                        modifier = Modifier.padding(top = 16.dp),
                        text = "Description"
                    )
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        text = result.data.description
                    )
                }
            }
        }
    }
}