package com.example

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.http.*
import freemarker.cache.*
import io.ktor.freemarker.*
import io.ktor.auth.*
import io.ktor.features.*
import io.ktor.serialization.*
import io.ktor.sessions.*
import kotlinx.serialization.Serializable
import javax.naming.AuthenticationException

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused")
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {

    install(ContentNegotiation) {
        json()
    }

    install(FreeMarker) {
        templateLoader = ClassTemplateLoader(this::class.java.classLoader, "templates")
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

        form("FORM_AUTH") {
            userParamName = "Username"
            passwordParamName = "Password"
            challenge {
                throw AuthenticationException()
            }
            validate { cred: UserPasswordCredential ->
                AuthProvider.tryAuth(cred.name, cred.password)
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

    routing {
        get("/") {
            call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
        }

        get("/html-freemarker") {
            call.respond(FreeMarkerContent("index.ftl", mapOf("data" to IndexData(listOf(1, 2, 3))), ""))
        }

        route("/login") {
            authenticate("FORM_AUTH") {
                post {
                    val principal = call.principal<UserIdPrincipal>()!!
                    call.sessions.set(principal)
                    call.respond(HttpStatusCode.OK, "OK")
                }
            }
        }

        route("/user_info") {
            authenticate("SESSION_AUTH") {
                get {
                    val principal = call.principal<UserIdPrincipal>()!!
                    call.respond(HttpStatusCode.OK, principal)
                }
            }
        }

        install(StatusPages) {
            exception<AuthenticationException> { cause ->
                call.respond(HttpStatusCode.Unauthorized)
            }
        }
    }
}

data class IndexData(val items: List<Int>)

@Serializable
data class UserIdPrincipal(val name: String) : Principal

object AuthProvider {

    const val TEST_USER_NAME = "test_user_name"
    const val TEST_USER_PASSWORD = "test_user_password"

    fun tryAuth(userName: String, password: String): UserIdPrincipal? {

        //Here you can use DB or other ways to check user and create a Principal
        if (userName == TEST_USER_NAME && password == TEST_USER_PASSWORD) {
            return UserIdPrincipal(TEST_USER_NAME)
        }

        return null
    }
}