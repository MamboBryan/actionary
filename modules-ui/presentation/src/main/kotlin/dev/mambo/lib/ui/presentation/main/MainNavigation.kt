package dev.mambo.lib.ui.presentation.main

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import dev.mambo.lib.ui.presentation.screens.tasks.TasksScreen

@Composable
fun MainNavigation() {
    Navigator(TasksScreen)
}
