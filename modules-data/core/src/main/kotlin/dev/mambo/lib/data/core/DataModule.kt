package dev.mambo.lib.data.core

import dev.mambo.lib.data.core.repositories.CategoryRepositoryImpl
import dev.mambo.lib.data.core.repositories.TaskRepositoryImpl
import dev.mambo.lib.data.domain.repositories.CategoryRepository
import dev.mambo.lib.data.domain.repositories.TaskRepository
import dev.mambo.lib.local.LocalModule
import org.koin.dsl.module

val DataModule =
    module {
        includes(LocalModule)
        single<TaskRepository> { TaskRepositoryImpl(local = get()) }
        single<CategoryRepository> { CategoryRepositoryImpl(localSource = get()) }
    }
