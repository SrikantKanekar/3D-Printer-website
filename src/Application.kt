package com.example

import com.example.data.models.UserIdPrincipal
import com.example.di.helloAppModule
import com.example.routes.*
import freemarker.cache.ClassTemplateLoader
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.features.*
import io.ktor.freemarker.*
import io.ktor.http.content.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.sessions.*
import org.koin.ktor.ext.Koin
import org.koin.logger.SLF4JLogger
import javax.naming.AuthenticationException

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused")
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {

    install(Koin) {
        SLF4JLogger()
        modules(helloAppModule)
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
        session<UserIdPrincipal>("SESSION_AUTH"){
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
            cookie.path = "/"
            cookie.extensions["SameSite"] = "lax"
        }
    }

    registerTestRoutes()

    routing {
        homeRoute()
        authRoutes()
        accountRoutes()
        statusRoutes()

        static("/static") {
            resources("files")
        }
    }
}