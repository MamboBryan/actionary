package dev.mambo.lib.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.mambo.lib.local.dao.CategoryDAO
import dev.mambo.lib.local.dao.TaskDAO
import dev.mambo.lib.local.entities.CategoryEntity
import dev.mambo.lib.local.entities.TaskEntity

@Database(
    entities = [TaskEntity::class, CategoryEntity::class],
    exportSchema = false,
    version = 1,
)
abstract class ActionaryDatabase : RoomDatabase() {
    abstract fun taskDAO(): TaskDAO

    abstract fun categoryDAO(): CategoryDAO

    companion object {
        const val DATABASE_NAME = "actionary_database"
    }
}
