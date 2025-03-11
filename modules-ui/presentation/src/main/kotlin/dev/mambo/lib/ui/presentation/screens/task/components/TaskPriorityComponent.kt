package dev.mambo.lib.ui.presentation.screens.task.components

import android.R.attr.contentDescription
import android.R.attr.name
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.OutlinedFlag
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.mambo.lib.data.domain.models.PriorityDomain
import dev.mambo.lib.ui.design.components.BottomSheetPicker
import dev.mambo.lib.ui.presentation.screens.task.TaskScreenState

val PriorityDomain.label: String
    get() = name.lowercase().replaceFirstChar { it.uppercase() }

@Composable
fun TaskPriorityComponent(
    state: TaskScreenState,
    modifier: Modifier = Modifier,
    onValueChangePriority: (PriorityDomain) -> Unit,
) {
    var isOpen by remember { mutableStateOf(false) }

    fun toggleBottomSheet() {
        isOpen = isOpen.not()
    }

    if (isOpen) {
        BottomSheetPicker(
            title = "Select Priority",
            selected = state.priority,
            onDismiss = ::toggleBottomSheet,
            onItemSelected = {
                onValueChangePriority(it)
                toggleBottomSheet()
            },
            list = PriorityDomain.entries.toList(),
            name = { it.label },
        )
    }

    Card(
        modifier = modifier,
        onClick = ::toggleBottomSheet,
        shape = RoundedCornerShape(0),
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.onBackground,
            ),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier,
                fontWeight = FontWeight.Bold,
                text = state.priority?.label ?: "Priority",
            )
            Icon(
                modifier = Modifier.padding(start = 8.dp),
                imageVector = Icons.Outlined.OutlinedFlag,
                contentDescription = "priority",
            )
        }
    }
}
