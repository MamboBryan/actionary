package dev.mambo.lib.ui.presentation.screens.task.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.Tag
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.mambo.lib.data.domain.models.CategoryDomain
import dev.mambo.lib.ui.design.components.Picker
import dev.mambo.lib.ui.design.components.PickerItem
import dev.mambo.lib.ui.presentation.components.CenteredColumn
import dev.mambo.lib.ui.presentation.helpers.ItemUiState
import kotlinx.coroutines.launch

@Composable
fun FilterCategory(
    category: CategoryDomain?,
    categories: List<CategoryDomain>,
    onItemSelected: (CategoryDomain) -> Unit,
    modifier: Modifier = Modifier,
) {
    var isOpen by remember { mutableStateOf(false) }

    fun toggle() {
        isOpen = isOpen.not()
    }

    if (isOpen) {
        TaskCategoryComponent(
            category = category,
            categories = categories,
            onItemSelected = {
                onItemSelected(it)
                toggle()
            },
            onDismiss = ::toggle,
        )
    }

    FilterChip(
        modifier = modifier,
        border = BorderStroke(0.dp, Color.Transparent),
        colors =
            FilterChipDefaults.filterChipColors(
                containerColor = if (category == null) MaterialTheme.colorScheme.primary.copy(0.25f) else MaterialTheme.colorScheme.primary,
                selectedContainerColor = MaterialTheme.colorScheme.primary,
                selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                selectedTrailingIconColor = MaterialTheme.colorScheme.primary,
            ),
        selected = category != null,
        onClick = ::toggle,
        label = {
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text =
                        if (category != null) {
                            category.name.lowercase()
                                .replaceFirstChar { it.uppercase() }
                        } else {
                            "Category"
                        },
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.labelMedium,
                )
                Icon(
                    modifier = Modifier.padding(start = 2.dp),
                    imageVector = Icons.Rounded.KeyboardArrowDown,
                    contentDescription = "collapse",
                )
            }
        },
    )
}

@Composable
fun TaskCategory(
    state: ItemUiState<CategoryDomain>?,
    name: String,
    category: CategoryDomain?,
    categories: List<CategoryDomain>,
    onItemSelected: (CategoryDomain) -> Unit,
    onClickCreateCategory: () -> Unit,
    onValueChangeName: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var isOpen by remember { mutableStateOf(false) }

    fun toggle() {
        isOpen = isOpen.not()
    }

    if (isOpen) {
        TaskCategoryComponent(
            state = state,
            name = name,
            category = category,
            categories = categories,
            onItemSelected = {
                onItemSelected(it)
                toggle()
            },
            onDismiss = ::toggle,
            enabled = name.isNotBlank() && categories.any { it.name.lowercase() == name }.not(),
            onClickCreateCategory = onClickCreateCategory,
            onValueChangeName = onValueChangeName,
        )
    }

    Card(
        modifier = modifier,
        onClick = ::toggle,
        shape = RoundedCornerShape(0),
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.onBackground,
            ),
    ) {
        Row(modifier = Modifier.padding(8.dp)) {
            Text(
                text = category?.name?.replaceFirstChar { it.uppercase() } ?: "Category",
                style = MaterialTheme.typography.titleLarge,
            )
            Icon(
                modifier = Modifier.padding(start = 8.dp),
                imageVector = Icons.Rounded.KeyboardArrowDown,
                contentDescription = "select",
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskCategoryComponent(
    category: CategoryDomain?,
    categories: List<CategoryDomain>,
    onItemSelected: (CategoryDomain) -> Unit,
    onDismiss: () -> Unit,
    title: String = "Categories",
    name: String = "",
    enabled: Boolean = false,
    state: ItemUiState<CategoryDomain>? = null,
    onValueChangeName: (String) -> Unit = {},
    onClickCreateCategory: (() -> Unit)? = null,
) {
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    fun hideSheet() {
        scope.launch {
            sheetState.hide()
        }
    }

    ModalBottomSheet(
        modifier = Modifier.padding(bottom = 24.dp),
        sheetState = sheetState,
        onDismissRequest = {
            hideSheet()
            onDismiss()
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                hideSheet()
                                onDismiss()
                            },
                        ) {
                            Icon(imageVector = Icons.Rounded.Close, contentDescription = "close")
                        }
                    },
                    title = { Text(text = title) },
                )
            },
        ) { innerPadding ->
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
            ) {
                AnimatedContent(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .weight(1f),
                    targetState = categories,
                ) { list ->
                    when {
                        list.isEmpty() -> {
                            CenteredColumn(modifier = Modifier.fillMaxSize()) {
                                Icon(
                                    modifier = Modifier.size(200.dp),
                                    imageVector = Icons.Rounded.Tag,
                                    contentDescription = "tag",
                                )
                                Text(text = "No categories found")
                            }
                        }

                        else -> {
                            Picker(
                                title = "",
                                modifier = Modifier.fillMaxSize(),
                                selected = category,
                                list = categories,
                                onItemSelected = {
                                    onItemSelected(it)
                                    hideSheet()
                                },
                                item = { item, selected, name, block ->
                                    PickerItem(
                                        item = item,
                                        isSelected = selected,
                                        text =
                                            item.name.lowercase()
                                                .replaceFirstChar { it.uppercase() },
                                        onClick = block,
                                    )
                                },
                            )
                        }
                    }
                }
                if (onClickCreateCategory != null) {
                    HorizontalDivider()
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = name,
                        onValueChange = onValueChangeName,
                        label = {
                            Text(text = "Name")
                        },
                        placeholder = {
                            Text(text = "Category Name...")
                        },
                        trailingIcon = {
                            CenteredColumn(modifier = Modifier.size(48.dp)) {
                                when (state) {
                                    is ItemUiState.Error -> {
                                        Icon(
                                            imageVector = Icons.Rounded.Error,
                                            contentDescription = "error",
                                        )
                                    }

                                    ItemUiState.Loading -> {
                                        CircularProgressIndicator()
                                    }

                                    is ItemUiState.Success -> {
                                        Icon(
                                            imageVector = Icons.Rounded.CheckCircle,
                                            contentDescription = "success",
                                        )
                                    }

                                    null -> {
                                        IconButton(
                                            onClick = onClickCreateCategory,
                                            enabled = enabled,
                                            colors =
                                                IconButtonDefaults.iconButtonColors(
                                                    containerColor = MaterialTheme.colorScheme.primary,
                                                    contentColor = MaterialTheme.colorScheme.onPrimary,
                                                ),
                                        ) {
                                            Icon(
                                                imageVector = Icons.Rounded.Add,
                                                contentDescription = "add",
                                            )
                                        }
                                    }
                                }
                            }
                        },
                        colors =
                            OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = MaterialTheme.colorScheme.surface,
                                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                                focusedBorderColor = MaterialTheme.colorScheme.surface,
                                unfocusedBorderColor = MaterialTheme.colorScheme.surface,
                                focusedTextColor = MaterialTheme.colorScheme.onBackground,
                                unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                                cursorColor = MaterialTheme.colorScheme.onBackground,
                                focusedLabelColor = MaterialTheme.colorScheme.primary,
                                unfocusedLabelColor = MaterialTheme.colorScheme.primary,
                            ),
                    )
                }
            }
        }
    }
}
