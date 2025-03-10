package dev.mambo.lib.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.mambo.lib.local.dao.TaskDAO
import dev.mambo.lib.local.entities.TaskEntity

@Database(
    entities = [TaskEntity::class],
    exportSchema = false,
    version = 7
)
abstract class ActionaryDatabase : RoomDatabase() {

    abstract fun taskDAO(): TaskDAO

    companion object {
        const val DATABASE_NAME = "actionary_database"
    }

}
