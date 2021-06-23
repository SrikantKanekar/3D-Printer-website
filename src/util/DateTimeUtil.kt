package com.example.util

import kotlinx.datetime.*

fun now(): String {
    return Clock.System.now().toString()
}

fun toLocalDate(date: Double): LocalDateTime {
    return Instant.fromEpochMilliseconds(date.toLong()).toLocalDateTime(TimeZone.UTC)
}

fun toEpochMilliseconds(date: LocalDateTime): Double {
    return date.toInstant(TimeZone.UTC).toEpochMilliseconds().toDouble()
}

fun humanizeDatetime(date: LocalDateTime?): String {
    val sb = StringBuilder()
    date?.run {
        val hour = if (this.hour > 12) {
            (this.hour - 12).toString() + "pm"
        } else {
            if (this.hour != 0) this.hour.toString() + "am" else "midnight"
        }
        val today = Clock.System.now().toLocalDateTime(TimeZone.UTC)
        val tomorrow = Clock.System.now().plus(1, DateTimeUnit.DAY, TimeZone.UTC).toLocalDateTime(TimeZone.UTC)
        when (this.date) {
            today.date -> {
                sb.append("Today at $hour")
            }
            tomorrow.date -> {
                sb.append("Tomorrow at $hour")
            }
            else -> {
                sb.append(this.date.month.name.lowercase() + " ${this.date.dayOfMonth}")
            }
        }
    } ?: sb.append("Unknown")
    return sb.toString()
}

fun main() {
    val instant = Clock.System.now()
    val local = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    println("instant   -> $local")
}