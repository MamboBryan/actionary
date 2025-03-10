package dev.mambo.lib.data.core.mappers

import dev.mambo.lib.data.domain.helpers.asEpochMilliseconds
import dev.mambo.lib.data.domain.helpers.asLocalDateTime
import dev.mambo.lib.data.domain.helpers.tryOrNull
import dev.mambo.lib.data.domain.models.PriorityDomain
import dev.mambo.lib.data.domain.models.TaskDomain
import dev.mambo.lib.local.caches.TaskCache

fun TaskCache.toTaskDomain() = TaskDomain(
    id = id,
    title = title,
    description = description,
    createdAt = createdAt.asLocalDateTime(),
    updatedAt = updatedAt.asLocalDateTime(),
    completedAt = completedAt?.asLocalDateTime(),
    dueAt = dueAt?.asLocalDateTime(),
    priority = tryOrNull { priority?.let { PriorityDomain.valueOf(it) } }
)

fun TaskDomain.toTaskCache() = TaskCache(
    id = id,
    title = title,
    description = description,
    createdAt = createdAt.asEpochMilliseconds(),
    updatedAt = updatedAt.asEpochMilliseconds(),
    completedAt = completedAt?.asEpochMilliseconds(),
    dueAt = dueAt?.asEpochMilliseconds(),
    priority = priority?.name
)
