package com.example.features.`object`.presentation

import com.example.features.`object`.data.ObjectRepository
import com.example.features.`object`.requests.ObjectCreateRequest
import com.example.model.UserPrincipal
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

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
        val principal = call.principal<UserPrincipal>()!!
        val obj = objectRepository.createObject(body, principal.email)

        objectRepository.addUserObject(principal.email, obj)

        call.respond(HttpStatusCode.Created, obj)
    }
}