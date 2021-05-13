package com.example.features.`object`.presentation

import com.example.features.`object`.data.ObjectRepository
import io.ktor.application.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject

fun Application.registerObjectRoutes() {

    val objectRepository by inject<ObjectRepository>()

    routing {
        getCreateObjectRoute()
        createObjectRoute(objectRepository)

        getUpdateObjectRoute(objectRepository)
        updateBasicSettingsRoute(objectRepository)
        updateAdvancedSettingsRoute(objectRepository)
    }
}