package com.example.features.`object`.presentation

import com.example.features.`object`.data.ObjectRepository
import com.example.features.`object`.routes.objectGet
import com.example.features.cart.presentation.objectUpdateQuality
import com.example.features.cart.presentation.objectUpdateQuantity
import com.example.features.objects.routes.objectsGet
import com.example.util.constants.Auth.USER_AUTH
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject

fun Application.registerObjectRoute() {

    val objectRepository by inject<ObjectRepository>()

    routing {
        route("/objects") {
            authenticate(USER_AUTH) {
                objectsGet(objectRepository)
                objectGet(objectRepository)
                objectCreate(objectRepository)
                objectUpdateQuality(objectRepository)
                objectUpdateQuantity(objectRepository)
                objectDelete(objectRepository)
                objectSpecialRequest(objectRepository)
            }
        }
    }
}

