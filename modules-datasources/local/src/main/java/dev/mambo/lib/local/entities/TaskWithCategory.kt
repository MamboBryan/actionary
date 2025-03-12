package dev.mambo.lib.local.entities

import androidx.room.Embedded
import androidx.room.Relation
import dev.mambo.lib.local.caches.TaskCache
import dev.mambo.lib.local.caches.toCategoryCache

data class TaskWithCategory(
    @Embedded val task: TaskEntity,
    @Relation(
        parentColumn = "category_id",
        entityColumn = "id",
    )
    val category: CategoryEntity?,
)

fun TaskWithCategory.toTaskCache() =
    TaskCache(
        id = task.id,
        title = task.title,
        description = task.description,
        createdAt = task.createdAt,
        updatedAt = task.updatedAt,
        completedAt = task.completedAt,
        dueAt = task.dueAt,
        priority = task.priority,
        category = category?.toCategoryCache(),
    )
