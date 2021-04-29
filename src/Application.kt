package com.example

import com.example.di.authModule
import com.example.feautures.account.domain.CartCookie
import com.example.feautures.account.presentation.registerAccountRoutes
import com.example.feautures.auth.domain.UserIdPrincipal
import com.example.feautures.auth.presentation.registerAuthRoutes
import com.example.feautures.home.presentation.registerHomeRoute
import com.example.feautures.order.presentation.registerOrderRoutes
import com.example.util.registerStatusRoutes
import freemarker.cache.ClassTemplateLoader
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.features.*
import io.ktor.freemarker.*
import io.ktor.http.content.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.sessions.*
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

    install(Authentication) {
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
        cookie<CartCookie>(
            name = "CART_COOKIE",
            storage = SessionStorageMemory()
        ) {
            cookie.extensions["SameSite"] = "lax"
        }
    }

    registerHomeRoute()
    registerAuthRoutes()
    registerAccountRoutes()
    registerOrderRoutes()
    registerStatusRoutes()

    routing {
        static("static") {
            resources("static")
        }
    }
}