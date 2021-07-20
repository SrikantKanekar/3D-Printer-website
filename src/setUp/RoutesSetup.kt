package com.example.setUp

import com.example.features.`object`.presentation.registerObjectRoute
import com.example.features.account.presentation.registerAccountRoute
import com.example.features.admin.presentation.registerAdminRoutes
import com.example.features.auth.presentation.registerAuthRoutes
import com.example.features.cart.presentation.registerCartRoute
import com.example.features.checkout.presentation.registerCheckoutRoute
import com.example.features.notification.presentation.registerNotificationRoutes
import com.example.features.order.presentation.registerOrderRoute
import com.example.features.util.presentation.registerStatusRoutes
import io.ktor.application.*

fun Application.routesSetup() {
    registerAccountRoute()
    registerAdminRoutes()
    registerAuthRoutes()
    registerCartRoute()
    registerCheckoutRoute()
    registerNotificationRoutes()
    registerObjectRoute()
    registerOrderRoute()
    registerStatusRoutes()
}