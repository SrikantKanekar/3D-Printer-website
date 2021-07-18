package com.example

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.config.AppConfig
import com.example.config.setupConfig
import com.example.di.productionModules
import com.example.features.`object`.presentation.registerObjectRoute
import com.example.features.account.presentation.registerAccountRoute
import com.example.features.admin.presentation.registerAdminRoutes
import com.example.features.auth.data.AuthRepository
import com.example.features.auth.domain.UserPrincipal
import com.example.features.auth.presentation.registerAuthRoutes
import com.example.features.cart.presentation.registerCartRoute
import com.example.features.checkout.presentation.registerCheckoutRoute
import com.example.features.notification.presentation.registerNotificationRoutes
import com.example.features.objects.domain.ObjectCookieSerializer
import com.example.features.objects.domain.ObjectsCookie
import com.example.features.objects.presentation.registerObjectsRoute
import com.example.features.order.presentation.registerOrderRoute
import com.example.features.orders.presentation.registerOrdersRoute
import com.example.features.util.presentation.registerIndexRoute
import com.example.features.util.presentation.registerStatusRoutes
import com.example.util.AUTH.ADMIN_AUTH
import com.example.util.AUTH.USER_AUTH
import com.example.util.AuthorizationException
import com.example.util.COOKIES.OBJECTS_COOKIE
import freemarker.cache.ClassTemplateLoader
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.features.*
import io.ktor.freemarker.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.sessions.*
import org.koin.core.module.Module
import org.koin.ktor.ext.Koin
import org.koin.ktor.ext.inject
import org.koin.logger.SLF4JLogger

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused")
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false, koinModules: List<Module> = productionModules) {

    install(Koin) {
        SLF4JLogger()
        modules(koinModules)
    }

    setupConfig(testing)

    install(ContentNegotiation) {
        json()
    }

    install(CORS){
        method(HttpMethod.Options)
        method(HttpMethod.Put)
        method(HttpMethod.Patch)
        method(HttpMethod.Delete)
        header(HttpHeaders.Authorization)
        header(HttpHeaders.ContentType)
        anyHost()
    }

    install(FreeMarker) {
        templateLoader = ClassTemplateLoader(
            this::class.java.classLoader,
            "templates"
        )
    }

    install(Authentication) {
        val appConfig by inject<AppConfig>()
        val jwt = appConfig.jwtConfig

        jwt(USER_AUTH) {
            realm = jwt.realm
            verifier(
                JWT.require(Algorithm.HMAC256(jwt.secret))
                    .withIssuer(jwt.issuer)
                    .withAudience(jwt.audience)
                    .build()
            )
            validate { credential ->
                if (credential.payload.audience.contains(jwt.audience)) {
                    val email = credential.payload.getClaim("email").asString()
                    val username = credential.payload.getClaim("username").asString()

                    val authRepository by inject<AuthRepository>()
                    val exists = authRepository.doesUserExist(email)
                    if (exists) UserPrincipal(email, username) else null
                } else {
                    null
                }
            }
        }

        jwt(ADMIN_AUTH) {
            realm = jwt.realm
            verifier(
                JWT.require(Algorithm.HMAC256(jwt.secret))
                    .withIssuer(jwt.issuer)
                    .withAudience(jwt.audience)
                    .build()
            )
            validate { credential ->
                if (credential.payload.audience.contains(jwt.audience)) {
                    val isAdmin = credential.payload.getClaim("is_admin").asBoolean()
                    if (!isAdmin) throw AuthorizationException()
                    val email = credential.payload.getClaim("email").asString()
                    val username = credential.payload.getClaim("username").asString()

                    val authRepository by inject<AuthRepository>()
                    val exists = authRepository.doesUserExist(email)
                    if (exists) UserPrincipal(email, username) else null
                } else {
                    null
                }
            }
        }
    }

    install(Sessions) {
        cookie<ObjectsCookie>(
            name = OBJECTS_COOKIE,
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

    registerAccountRoute()
    registerAdminRoutes()
    registerAuthRoutes()
    registerCartRoute()
    registerCheckoutRoute()
    registerNotificationRoutes()
    registerObjectRoute()
    registerObjectsRoute()
    registerOrderRoute()
    registerOrdersRoute()
    registerIndexRoute()
    registerStatusRoutes()
}