package dev.mambo.lib.ui.design.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun <T : Any> Picker(
    selected: T?,
    list: List<T>,
    modifier: Modifier = Modifier,
    item: @Composable (T, Boolean, String, () -> Unit) -> Unit = { value, isSelected, text, block ->
        PickerItem(
            item = value,
            isSelected = isSelected,
            onClick = block,
            text = text,
        )
    },
    onItemSelected: (T) -> Unit = {},
    title: String = "Select",
) {
    Column(modifier = modifier.padding(16.dp)) {
        Text(
            modifier = Modifier.padding(start = 24.dp, bottom = 16.dp),
            text = title,
            fontSize = MaterialTheme.typography.titleLarge.fontSize,
        )
        LazyColumn {
            items(list) {
                item(it, it == selected, it.toString()) {
                    onItemSelected(it)
                }
            }
        }
        Spacer(modifier = Modifier.padding(bottom = 24.dp))
    }
}

@Composable
fun <T> PickerItem(
    item: T,
    isSelected: Boolean,
    text: String,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.primary.copy(0.1f),
    contentColor: Color = MaterialTheme.colorScheme.onBackground,
    onClick: () -> Unit,
) {
    val (container, content) =
        if (isSelected) {
            Pair(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.onPrimary)
        } else {
            Pair(containerColor, contentColor)
        }

    Column(
        modifier =
        modifier
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(10))
            .clickable { onClick() },
    ) {
        Row(
            modifier =
            Modifier
                .background(container)
                .padding(8.dp)
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier =
                Modifier
                    .weight(1f)
                    .padding(8.dp),
                text = text,
                color = content,
                fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal,
            )
            AnimatedVisibility(visible = isSelected) {
                Icon(
                    imageVector = Icons.Rounded.Check,
                    contentDescription = "selected",
                    tint = content,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T : Any> BottomSheetPicker(
    selected: T?,
    list: List<T>,
    onDismiss: () -> Unit,
    onItemSelected: (T) -> Unit,
    name: @Composable (T) -> String,
    modifier: Modifier = Modifier,
    title: String = "Select",
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
        Picker(
            title = title,
            modifier = modifier,
            selected = selected,
            list = list,
            onItemSelected = {
                hideSheet()
                onItemSelected(it)
            },
            item = { value, isSelected, _, block ->
                PickerItem(
                    item = value,
                    isSelected = isSelected,
                    onClick = block,
                    text = name.invoke(value),
                )
            },
        )
    }
}
