package com.example

import com.example.di.authModule
import com.example.features.wishlist.domain.WishlistCookie
import com.example.features.account.presentation.registerAccountRoutes
import com.example.features.auth.domain.Login
import com.example.features.auth.domain.UserIdPrincipal
import com.example.features.auth.domain.loginProviders
import com.example.features.auth.presentation.registerAuthRoutes
import com.example.features.cart.presentation.registerCartRoutes
import com.example.features.checkout.presentation.registerCheckoutRoutes
import com.example.features.history.presentation.registerHistoryRoutes
import com.example.features.util.presentation.registerHomeRoute
import com.example.features.notification.presentation.registerNotificationRoutes
import com.example.features.order.presentation.registerOrderRoutes
import com.example.features.tracker.presentation.registerTrackerRoutes
import com.example.features.wishlist.presentation.registerWishlistRoutes
import com.example.features.util.presentation.registerStatusRoutes
import freemarker.cache.ClassTemplateLoader
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.features.*
import io.ktor.freemarker.*
import io.ktor.http.content.*
import io.ktor.locations.*
import io.ktor.locations.url
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.sessions.*
import io.ktor.util.*
import org.koin.core.module.Module
import org.koin.ktor.ext.Koin
import org.koin.logger.SLF4JLogger
import javax.naming.AuthenticationException

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused")
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false, koinModules: List<Module> = listOf(authModule)) {

    install(Koin) {
        SLF4JLogger()
        modules(koinModules)
    }

    install(ContentNegotiation) {
        json()
    }

    install(FreeMarker) {
        templateLoader = ClassTemplateLoader(
            this::class.java.classLoader,
            "templates"
        )
    }

    install(Locations)

    install(Authentication) {
        oauth("OAuth") {
            client = HttpClient(Apache)
            providerLookup = { loginProviders[application.locations.resolve<Login>(Login::class, this).type] }
            urlProvider = { url(Login(it.name)) }
        }

        session<UserIdPrincipal>("SESSION_AUTH") {
            challenge {
                throw AuthenticationException()
            }
            validate { session: UserIdPrincipal ->
                session
            }
        }
    }

    install(Sessions) {
        cookie<UserIdPrincipal>(
            name = "AUTH_COOKIE",
            storage = SessionStorageMemory()
        ) {
            cookie.extensions["SameSite"] = "lax"
        }
        cookie<WishlistCookie>(
            name = "WISHLIST_COOKIE",
            storage = SessionStorageMemory()
        ) {
            cookie.extensions["SameSite"] = "lax"
        }
    }

    routing {
        static("static") {
            resources("static")
        }
    }

    registerAccountRoutes()
    registerAuthRoutes()
    registerCartRoutes()
    registerCheckoutRoutes()
    registerHistoryRoutes()
    registerNotificationRoutes()
    registerOrderRoutes()
    registerTrackerRoutes()
    registerWishlistRoutes()
    registerHomeRoute()
    registerStatusRoutes()
}