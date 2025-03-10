package dev.mambo.lib.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = TaskEntity.TABLE_NAME)
data class TaskEntity(
    val title: String,
    val description: String,
    @ColumnInfo(name = "created_at") val createdAt: Long,
    @ColumnInfo(name = "updated_at") val updatedAt: Long = createdAt,
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "completed_at") val completedAt: Long? = null,
    @ColumnInfo(name = "due_at") val dueAt: Long? = null,
    val priority: String? = null
) {
    companion object {
        const val TABLE_NAME = "tasks"
    }
}
