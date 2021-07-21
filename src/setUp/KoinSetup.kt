package com.example.di

import io.ktor.application.*
import org.koin.core.module.Module
import org.koin.ktor.ext.Koin
import org.koin.logger.SLF4JLogger

fun Application.koinSetup(koinModules: List<Module>) {
    install(Koin) {
        SLF4JLogger()
        modules(koinModules)
    }
}