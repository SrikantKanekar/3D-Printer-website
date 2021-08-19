package com.example.features.`object`.presentation

import com.example.features.`object`.data.ObjectRepository
import com.example.features.`object`.routes.objectGet
import com.example.features.cart.presentation.objectUpdateQuantity
import com.example.features.objects.routes.objectsGet
import com.example.util.constants.Auth.USER_AUTH
import com.example.util.enums.ObjectStatus.*
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject

/**
 * This page will show the details about a object depending upon the ObjectStatus of the object.
 * 1) [NONE]/[CART] - user can edit object properties.
 * 2) [TRACKING]
 * 3) [COMPLETED]
 */
fun Application.registerObjectRoute() {

    val objectRepository by inject<ObjectRepository>()

    routing {
        route("/objects") {
            authenticate(USER_AUTH, optional = true) {
                objectsGet(objectRepository)
                objectGet(objectRepository)
                objectCreate(objectRepository)
                objectUpdateSetting(objectRepository)
                objectUpdateQuantity(objectRepository)
                objectDelete(objectRepository)
            }
        }
    }
}

