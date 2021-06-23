package com.example.util

import kotlinx.datetime.Clock

fun now(): String {
    return Clock.System.now().toString()
}