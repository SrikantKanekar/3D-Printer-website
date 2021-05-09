package com.example.features.history.presentation

import com.example.features.auth.domain.UserPrincipal
import com.example.features.history.data.HistoryRepository
import com.example.util.AUTH.USER_SESSION_AUTH
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.freemarker.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject

fun Application.registerHistoryRoutes() {

    val historyRepository by inject<HistoryRepository>()

    routing {
        authenticate(USER_SESSION_AUTH) {
            getHistoryRoute(historyRepository)
        }
    }
}

fun Route.getHistoryRoute(historyRepository: HistoryRepository) {
    get("/history"){

        val principal = call.principal<UserPrincipal>()!!
        val objects = historyRepository.getUserHistoryobjects(principal.email)
        call.respond(
            FreeMarkerContent(
                "history.ftl",
                mapOf("objects" to objects, "user" to principal)
            )
        )
    }
}
