package dev.mambo.lib.local

import androidx.room.Room
import dev.mambo.lib.local.dao.TaskDAO
import dev.mambo.lib.local.database.ActionaryDatabase
import dev.mambo.lib.local.sources.TaskLocalSource
import dev.mambo.lib.local.sources.TaskLocalSourceImpl
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

val LocalModule =
    module {
        // database
        single<ActionaryDatabase> {
            Room.databaseBuilder(
                context = get(),
                klass = ActionaryDatabase::class.java,
                name = ActionaryDatabase.DATABASE_NAME,
            ).fallbackToDestructiveMigration().build()
        }
        // DAO (Data Access Objects)
        single<TaskDAO> { get<ActionaryDatabase>().taskDAO() }
        // local sources
        single<TaskLocalSource> { TaskLocalSourceImpl(dispatcher = Dispatchers.IO, dao = get()) }
    }
