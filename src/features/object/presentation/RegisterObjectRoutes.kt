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

        getObjectRoute(objectRepository)
        addToCart(objectRepository)
        removeFromCart(objectRepository)
        updateQuantity(objectRepository)
        updateBasicSettings(objectRepository)
        updateIntermediateSettings(objectRepository)
        updateAdvancedSettings(objectRepository)
    }
}