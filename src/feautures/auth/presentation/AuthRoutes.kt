package com.example.feautures.auth.presentation

import com.example.feautures.auth.data.AuthRepository
import com.example.feautures.auth.domain.User
import com.example.feautures.auth.domain.UserIdPrincipal
import com.example.util.getHashWithSalt
import io.ktor.application.*
import io.ktor.freemarker.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import org.koin.ktor.ext.inject

fun Application.registerAuthRoutes() {
    val authRepository by inject<AuthRepository>()
    routing {
        route("/auth") {
            getLoginRoute()
            postLoginRoute(authRepository)
            getRegisterRoute()
            postRegisterRoute(authRepository)
        }
    }
}

fun Route.getLoginRoute() {
    route("/login") {
        get {
            call.respond(FreeMarkerContent("auth_login.ftl", null))
        }
    }
}

fun Route.postLoginRoute(authRepository: AuthRepository) {
    route("/login") {
        post {
            val params = call.receiveParameters()
            val email = params["Email"] ?: return@post call.respond(HttpStatusCode.BadRequest)
            val password = params["Password"] ?: return@post call.respond(HttpStatusCode.BadRequest)

            val isPasswordCorrect = authRepository.login(email, password)
            if (isPasswordCorrect) {
                call.sessions.set(UserIdPrincipal(email))
                call.respond(HttpStatusCode.OK, "Your are now logged in")
            } else {
                call.respond(HttpStatusCode.NotAcceptable, "The E-Mail or password is incorrect")
            }
        }
    }
}

fun Route.getRegisterRoute() {
    route("/register") {
        get {
            call.respond(FreeMarkerContent("auth_register.ftl", null))
        }
    }
}

fun Route.postRegisterRoute(authRepository: AuthRepository) {
    route("/register") {
        post {
            val params = call.receiveParameters()
            val email = params["Email"] ?: return@post call.respond(HttpStatusCode.BadRequest)
            val password = params["Password"] ?: return@post call.respond(HttpStatusCode.BadRequest)

            val userExists = authRepository.checkIfUserExists(email)
            if (!userExists) {
                if (authRepository.register(User(email, getHashWithSalt(password)))) {
                    call.sessions.set(UserIdPrincipal(email))
                    call.respond(HttpStatusCode.OK, "Successfully created account!")
                } else {
                    call.respond(HttpStatusCode.InternalServerError, "An unknown error occurred")
                }
            } else {
                call.respond(HttpStatusCode.Conflict, "A user with that E-Mail already exists")
            }
        }
    }
}
