package com.example.features.admin.presentation

import com.example.features.admin.domain.AdminPrincipal
import com.example.features.auth.domain.AuthConstants
import com.example.features.auth.domain.UserPrincipal
import io.ktor.application.*
import io.ktor.freemarker.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*

fun Route.getAdminLoginRoute() {
    get("/admin/login") {

        /**
         * redirect to admin screen if already logged in as admin
         */
        val adminPrincipal = call.sessions.get<AdminPrincipal>()
        if (adminPrincipal != null) {
            call.respondRedirect("/admin")
        } else {
            val userPrincipal = call.sessions.get<UserPrincipal>()
            call.respond(
                FreeMarkerContent(
                    "admin_login.ftl",
                    mapOf("user" to (userPrincipal?.email ?: ""))
                )
            )
        }
    }
}

fun Route.postAdminLoginRoute() {
    post("/admin/login") {
        val params = call.receiveParameters()
        val name = params["name"] ?: return@post call.respond(HttpStatusCode.BadRequest)
        val password = params["password"] ?: return@post call.respond(HttpStatusCode.BadRequest)

        if (name == "admin" && password == "password") {
            call.sessions.set(AdminPrincipal(name))
            call.respondText("/admin")
        } else {
            call.respondText(AuthConstants.EMAIL_PASSWORD_INCORRECT)
        }
    }
}
