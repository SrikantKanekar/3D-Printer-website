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
 * 2) users will be prompted to login when user tries to add object to the Cart.
 * 3) after logging in, all the cookie objects will be synced with account objects.
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
 * * All steps happens on the client side except the last one
    User Uploads object...
    1) display object in the editor
    2) check dimensions of object TODO()
    3) if any error...(show error message and retry button)
    4) else show create button

    User clicks Create button
    1) takes snapshot of object
    2) generate unique ID for object
    3) upload file to firebase (Id/filename)
    4) upload image to firebase (Id/image.png)
    5) send Id, filename, extension, file link, image link here to the server
 */
fun Route.createObjectRoute(objectRepository: ObjectRepository) {
    post("/object/create") {
        val params = call.receiveParameters()
        val id = params["id"] ?: return@post call.respond(HttpStatusCode.BadRequest)
        val name = params["name"] ?: return@post call.respond(HttpStatusCode.BadRequest)
        val fileUrl = params["file_url"] ?: return@post call.respond(HttpStatusCode.BadRequest)
        val imageUrl = params["image_url"] ?: return@post call.respond(HttpStatusCode.BadRequest)
        val fileExtension = params["file_extension"] ?: return@post call.respond(HttpStatusCode.BadRequest)

        val obj = objectRepository.createObject(id, name, fileUrl, imageUrl, fileExtension)

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