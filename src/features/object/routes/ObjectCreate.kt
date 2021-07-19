package com.example.features.`object`.presentation

import com.example.features.`object`.data.ObjectRepository
import com.example.features.`object`.requests.ObjectCreateRequest
import com.example.model.ObjectsCookie
import com.example.model.UserPrincipal
import io.ktor.application.*
import io.ktor.auth.*
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

/**
 * * All steps happens on the client side except the last one
User Uploads object...
1) display object in the editor
2) check dimensions of object
3) if any error...(show error message and retry button)
4) else show create button

User clicks Create button
1) takes snapshot of object
2) generate unique ID for object
3) upload file to firebase (Id/filename)
4) upload image to firebase (Id/image.png)
5) send Id, filename, extension, file link, image link here to the server
 */
fun Route.objectCreate(objectRepository: ObjectRepository) {
    post {
        val body = call.receive<ObjectCreateRequest>()
        val obj = objectRepository.createObject(body)

        val principal = call.principal<UserPrincipal>()
        when (principal) {
            null -> {
                val cookie = call.sessions.get<ObjectsCookie>() ?: ObjectsCookie()
                cookie.objects.add(obj)
                call.sessions.set(cookie)
            }
            else -> objectRepository.addUserObject(principal.email, obj)
        }
        call.respond(obj)
    }
}