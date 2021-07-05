package com.example.features.`object`.presentation

import com.example.features.`object`.data.ObjectRepository
import io.ktor.application.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject

fun Application.registerObjectRoute() {

    val objectRepository by inject<ObjectRepository>()

    routing {
        getCreateObjectRoute()
        postCreateObjectRoute(objectRepository)

        getObjectRoute(objectRepository)
        slice(objectRepository)
        addToCart(objectRepository)
        removeFromCart(objectRepository)
        updateSetting(objectRepository)
        deleteObject(objectRepository)
    }
}