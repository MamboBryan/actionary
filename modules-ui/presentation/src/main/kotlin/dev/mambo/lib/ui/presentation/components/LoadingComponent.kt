package dev.mambo.lib.ui.presentation.components

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun LoadingComponent(modifier: Modifier = Modifier) {
    CenteredColumn(modifier = modifier){
        CircularProgressIndicator()
    }
}
