package dev.mambo.lib.data.core

import dev.mambo.lib.data.core.repositories.TaskRepositoryImpl
import dev.mambo.lib.data.domain.repositories.TaskRepository
import org.koin.dsl.module

val DataModule =
    module {
        single<TaskRepository> { TaskRepositoryImpl(local = get()) }
    }
