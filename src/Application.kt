package com.example

import com.example.di.appModule
import com.example.features.userObject.domain.ObjectsCookie
import com.example.features.account.presentation.registerAccountRoutes
import com.example.features.admin.domain.AdminPrincipal
import com.example.features.admin.presentation.registerAdminRoutes
import com.example.features.auth.domain.Login
import com.example.features.auth.domain.UserPrincipal
import com.example.features.auth.domain.loginProviders
import com.example.features.auth.presentation.registerAuthRoutes
import com.example.features.cart.presentation.registerCartRoutes
import com.example.features.checkout.presentation.registerCheckoutRoutes
import com.example.features.history.presentation.registerHistoryRoutes
import com.example.features.util.presentation.registerIndexRoute
import com.example.features.notification.presentation.registerNotificationRoutes
import com.example.features.`object`.presentation.registerObjectRoutes
import com.example.features.auth.data.AuthRepository
import com.example.features.order.presentation.registerOrderRoutes
import com.example.features.tracking.presentation.registerTrackingRoutes
import com.example.features.userObject.presentation.registerUserObjectsRoutes
import com.example.features.util.presentation.registerStatusRoutes
import com.example.features.userObject.domain.ObjectCookieSerializer
import com.example.util.AUTH.ADMIN_SESSION_AUTH
import com.example.util.AUTH.OAUTH
import com.example.util.AUTH.USER_SESSION_AUTH
import com.example.util.COOKIES.ADMIN_AUTH_COOKIE
import com.example.util.COOKIES.AUTH_COOKIE
import com.example.util.COOKIES.MY_OBJECTS_COOKIE
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
import org.koin.core.module.Module
import org.koin.ktor.ext.Koin
import org.koin.ktor.ext.inject
import org.koin.logger.SLF4JLogger
import javax.naming.AuthenticationException

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused")
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false, koinModules: List<Module> = listOf(appModule)) {

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
        oauth(OAUTH) {
            client = HttpClient(Apache)
            providerLookup = { loginProviders[application.locations.resolve<Login>(Login::class, this).type] }
            urlProvider = { url(Login(it.name)) }
        }

        session<UserPrincipal>(USER_SESSION_AUTH) {
            challenge {
                throw AuthenticationException()
            }
            validate { principal ->
                val authRepository by inject<AuthRepository>()
                val exists = authRepository.doesUserExist(principal.email)
                if (exists) principal else null
            }
        }
        session<AdminPrincipal>(ADMIN_SESSION_AUTH) {
            challenge {
                throw AuthenticationException()
            }
            validate { principal ->
                principal
            }
        }
    }

    install(Sessions) {
        cookie<UserPrincipal>(
            name = AUTH_COOKIE,
            storage = SessionStorageMemory()
        )
        cookie<AdminPrincipal>(
            name = ADMIN_AUTH_COOKIE,
            storage = SessionStorageMemory()
        )
        cookie<ObjectsCookie>(
            name = MY_OBJECTS_COOKIE,
            storage = SessionStorageMemory()
        ) {
            serializer = ObjectCookieSerializer()
        }
    }

    routing {
        static("static") {
            resources("static")
        }
    }

    registerAccountRoutes()
    registerAdminRoutes()
    registerAuthRoutes()
    registerCartRoutes()
    registerCheckoutRoutes()
    registerHistoryRoutes()
    registerNotificationRoutes()
    registerObjectRoutes()
    registerOrderRoutes()
    registerTrackingRoutes()
    registerUserObjectsRoutes()
    registerIndexRoute()
    registerStatusRoutes()
}