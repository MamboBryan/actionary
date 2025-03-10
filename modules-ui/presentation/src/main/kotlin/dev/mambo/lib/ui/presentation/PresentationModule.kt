package dev.mambo.lib.ui.presentation

import dev.mambo.lib.ui.presentation.screens.tasks.TasksScreenModel
import org.koin.dsl.module

val PresentationModule =
    module {
        factory<TasksScreenModel> { TasksScreenModel(repository = get()) }
    }
