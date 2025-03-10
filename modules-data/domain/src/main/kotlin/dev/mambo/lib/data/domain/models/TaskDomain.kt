package dev.mambo.lib.data.domain.models

import kotlinx.datetime.LocalDateTime

data class TaskDomain(
    val id: Int,
    val title: String,
    val description: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime = createdAt,
    val completedAt: LocalDateTime?,
    val dueAt: LocalDateTime?,
    val priority: PriorityDomain? = null,
)
