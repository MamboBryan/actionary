package dev.mambo.lib.data.domain.helpers

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

private val timeZone = TimeZone.currentSystemDefault()

val Instant.LocalDateTime: LocalDateTime
    get() = toLocalDateTime(timeZone)

val LocalDateTime.Instant: Instant
    get() = toInstant(timeZone)

fun LocalDateTime.asEpochMilliseconds(): Long = this.Instant.toEpochMilliseconds()

fun Long.asLocalDateTime(): LocalDateTime = Instant.fromEpochMilliseconds(this).LocalDateTime
