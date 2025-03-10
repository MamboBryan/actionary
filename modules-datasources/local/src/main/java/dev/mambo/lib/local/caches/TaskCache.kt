package dev.mambo.lib.local.caches

import dev.mambo.lib.local.entities.TaskEntity

data class TaskCache(
    val id: Int,
    val title: String,
    val description: String,
    val createdAt: Long,
    val updatedAt: Long = createdAt,
    val completedAt: Long?,
    val dueAt: Long?,
    val priority: String? = null
)

fun TaskCache.toTaskEntity() = TaskEntity(
    id = id,
    title = title,
    description = description,
    createdAt = createdAt,
    updatedAt = updatedAt,
    completedAt = completedAt,
    dueAt = dueAt,
    priority = priority
)

fun TaskEntity.toTaskCache() = TaskCache(
    id = id,
    title = title,
    description = description,
    createdAt = createdAt,
    updatedAt = updatedAt,
    completedAt = completedAt,
    dueAt = dueAt,
    priority = priority
)
