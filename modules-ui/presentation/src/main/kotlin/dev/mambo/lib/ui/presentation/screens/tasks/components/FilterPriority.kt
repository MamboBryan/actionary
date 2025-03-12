package dev.mambo.lib.ui.presentation.screens.tasks.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
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
import dev.mambo.lib.data.domain.models.PriorityDomain
import dev.mambo.lib.ui.design.components.PickerItem
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterPriority(
    priority: PriorityDomain?,
    priorities: List<PriorityDomain>,
    onItemSelected: (PriorityDomain) -> Unit,
    modifier: Modifier = Modifier,
) {
    var isOpen by remember { mutableStateOf(false) }

    fun toggle() {
        isOpen = isOpen.not()
    }

    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()

    fun hideSheet() {
        scope.launch { sheetState.hide() }
    }

    if (isOpen) {
        ModalBottomSheet(
            modifier = Modifier.padding(bottom = 24.dp),
            sheetState = sheetState,
            onDismissRequest = {
                hideSheet()
                toggle()
            },
            containerColor = MaterialTheme.colorScheme.background,
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                TopAppBar(
                    modifier = Modifier.fillMaxWidth(),
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                hideSheet()
                                toggle()
                            },
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Close,
                                contentDescription = "close",
                            )
                        }
                    },
                    title = {
                        Text(text = "Priorities")
                    },
                )
                LazyColumn {
                    items(priorities) {
                        PickerItem(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                            item = it,
                            isSelected = it == priority,
                            text = it.name.lowercase().replaceFirstChar { it.uppercase() },
                            onClick = {
                                onItemSelected(it)
                                toggle()
                                hideSheet()
                            },
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.padding(16.dp))
                    }
                }
            }
        }
    }

    FilterChip(
        modifier = modifier,
        border = BorderStroke(0.dp, Color.Transparent),
        colors =
            FilterChipDefaults.filterChipColors(
                containerColor = if (priority == null) MaterialTheme.colorScheme.primary.copy(0.25f) else MaterialTheme.colorScheme.primary,
                selectedContainerColor = MaterialTheme.colorScheme.primary,
                selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                selectedTrailingIconColor = MaterialTheme.colorScheme.primary,
            ),
        selected = priority != null,
        onClick = ::toggle,
        label = {
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text =
                        if (priority != null) {
                            priority.name.lowercase()
                                .replaceFirstChar { it.uppercase() }
                        } else {
                            "Priority"
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
