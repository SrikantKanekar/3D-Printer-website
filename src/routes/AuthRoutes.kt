package com.example.routes

import com.example.data.database.checkIfUserExists
import com.example.data.database.checkPasswordForEmail
import com.example.data.database.registerUser
import com.example.data.models.User
import com.example.data.models.UserIdPrincipal
import com.example.data.database.getHashWithSalt
import io.ktor.application.*
import io.ktor.freemarker.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*

fun Route.authRoutes() {

    route("/login") {
        get {
            call.respondTemplate("login.html")
        }

        post {
            val params = call.receiveParameters()
            val username = params["Username"] ?: return@post call.respond(HttpStatusCode.BadRequest)
            val password = params["Password"] ?: return@post call.respond(HttpStatusCode.BadRequest)

            val isPasswordCorrect = checkPasswordForEmail(username, password)
            if (isPasswordCorrect) {
                call.sessions.set(UserIdPrincipal(username))
                call.respond(HttpStatusCode.OK, "Your are now logged in!")
            } else {
                call.respond(HttpStatusCode.OK, "The E-Mail or password is incorrect")
            }
        }
    }

    route("/register") {
        get {
            call.respondTemplate("register.html")
        }

        post {
            val params = call.receiveParameters()
            val username = params["Username"] ?: return@post call.respond(HttpStatusCode.BadRequest)
            val password = params["Password"] ?: return@post call.respond(HttpStatusCode.BadRequest)

            val userExists = checkIfUserExists(username)
            if (!userExists) {
                if (registerUser(User(username, getHashWithSalt(password)))) {
                    call.sessions.set(UserIdPrincipal(username))
                    call.respond(HttpStatusCode.OK, "Successfully created account!")
                } else {
                    call.respond(HttpStatusCode.OK, "An unknown error occurred")
                }
            } else {
                call.respond(HttpStatusCode.OK, "A user with that E-Mail already exists")
            }
        }
    }
}