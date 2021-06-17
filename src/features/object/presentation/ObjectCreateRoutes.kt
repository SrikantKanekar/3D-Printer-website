package com.example.features.`object`.presentation

import com.example.features.`object`.data.ObjectRepository
import com.example.features.auth.domain.UserPrincipal
import com.example.features.objects.domain.ObjectsCookie
import io.ktor.application.*
import io.ktor.freemarker.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*

/**
 * *User can create new objects without logging in...
 * 1) if user is not logged in, newly created objects will be stored in 'OBJECTS_COOKIE'.
 * 2) users will be prompted to login when user tried to add object to Cart.
 * 3) after logging in, all the cookie objects will be synced with account object.
 */
fun Route.getCreateObjectRoute() {
    get("/object/create") {
        val principal = call.sessions.get<UserPrincipal>()
        call.respond(
            FreeMarkerContent(
                "object_create.ftl",
                mapOf("user" to (principal?.email ?: ""))
            )
        )
    }
}

/**
 * * all steps happen on client side except the last line.
    User Uploads
    1) display object in editor
    2) check dimensions of object
    3) if any error...(shoe error message + retry button)
    4) else show create button

    User clicks Create button
    1) take snapshot of object
    2) generate unique ID
    3) upload file to firebase (design/Id)
    4) upload image to firebase (image/Id)
    5) send Id, filename, file link, image link here to server
 */
fun Route.createObjectRoute(objectRepository: ObjectRepository) {
    post("/object/create") {
        val params = call.receiveParameters()
        val id = params["id"] ?: return@post call.respond(HttpStatusCode.BadRequest)
        val name = params["name"] ?: return@post call.respond(HttpStatusCode.BadRequest)
        val fileUrl = params["file_url"] ?: return@post call.respond(HttpStatusCode.BadRequest)
        val imageUrl = params["image_url"] ?: return@post call.respond(HttpStatusCode.BadRequest)

        val obj = objectRepository.createObject(id, name, fileUrl, imageUrl)

        val success: Boolean
        when (val principal = call.sessions.get<UserPrincipal>()) {
            null -> {
                val cookie = call.sessions.get<ObjectsCookie>() ?: ObjectsCookie()
                success = cookie.objects.add(obj)
                call.sessions.set(cookie)
            }
            else -> success = objectRepository.addUserObject(principal.email, obj)
        }
        if (success) {
            call.respondText(obj.id)
        } else {
            call.respondText("error")
        }
    }
}