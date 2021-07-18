package com.example.features.util.presentation

import com.example.util.AuthorizationException
import com.example.util.DatabaseException
import com.example.util.ValidationException
import com.mongodb.MongoTimeoutException
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.serialization.SerializationException

fun Application.registerStatusRoutes() {
    routing {
        statusRoutes()
    }
}

fun Route.statusRoutes() {

    install(StatusPages) {

        // Input validation
        exception<ValidationException> { e ->
            println("Validation exception: ${e.message}")
            call.respond(HttpStatusCode.BadRequest, e.message ?: "Bad request")
        }

        exception<SerializationException> { e ->
            println("SerializationException: ${e.message}")
            call.respond(HttpStatusCode.BadRequest, e.message ?: "Bad request")
        }

        // Database
        exception<MongoTimeoutException> {
            println("Mongo timeout exception")
            call.respond(HttpStatusCode.InternalServerError)
        }

        exception<DatabaseException> { e ->
            println("Mongo read write exception: ${e.message}")
            call.respond(HttpStatusCode.InternalServerError)
        }

        // Authorization
        exception<AuthorizationException> {
            call.respond(HttpStatusCode.Forbidden)
        }
    }
}