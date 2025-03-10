package dev.mambo.lib.ui.presentation.screens.tasks

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen

object TasksScreen : Screen {
    @Composable
    override fun Content() {
        TasksScreenContent()
    }
}

@Composable
fun TasksScreenContent() {
    Scaffold { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Text(text = "Actionary")
        }
    }
}
