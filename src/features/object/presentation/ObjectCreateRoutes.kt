package com.example.features.`object`.presentation

import com.example.features.`object`.data.ObjectRepository
import com.example.features.auth.domain.UserPrincipal
import com.example.features.userObject.domain.ObjectsCookie
import com.example.util.FileHandler.createFile
import com.example.util.FileHandler.deleteFile
import io.ktor.application.*
import io.ktor.freemarker.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import kotlinx.coroutines.delay

fun Route.getCreateObjectRoute() {
    get("/object/create") {
        val principal = call.sessions.get<UserPrincipal>()
        call.respond(
            FreeMarkerContent(
                "object_create.ftl",
                mapOf(
                    "user" to (principal?.email ?: "")
                )
            )
        )
    }
}

fun Route.createObjectRoute(objectRepository: ObjectRepository) {
    post("/object/create") {
        val multipartData = call.receiveMultipart()
        val partData = multipartData.readAllParts().first()
        if (partData is PartData.FileItem) {
            val filename = partData.originalFileName!!
            val obj = objectRepository.createNewObject(filename)
            val file = createFile(obj.id)
            try {
                //throw Exception()
                partData.streamProvider().use { inputStream ->
                    file.outputStream().buffered().use { outputStream ->
                        inputStream.copyTo(outputStream)
                        partData.dispose.invoke()
                    }
                }

                // get details from octoPrint
                try {
                    //delay(3000)
                    //throw Exception()
                    obj.apply {
                        image = "/static/images/3d-image.jpg"
                        price = 100
                        timeToPrint = 100
                    }

                    //save object to user or cookie
                    val success: Boolean
                    val principal = call.sessions.get<UserPrincipal>()
                    if (principal != null) {
                        success = objectRepository.addUserObject(principal.email, obj)
                    } else {
                        val cookie = call.sessions.get<ObjectsCookie>() ?: ObjectsCookie()
                        success = cookie.objects.add(obj)
                        call.sessions.set(cookie)
                    }
                    call.respond(mapOf("success" to success.toString(), "id" to obj.id))
                    if (!success) deleteFile(obj.id)
                } catch (e: Exception) {
                    deleteFile(obj.id)
                    call.respond(HttpStatusCode.InternalServerError, "Slicing error")
                }
            } catch (e: Exception) {
                deleteFile(obj.id)
                call.respond(HttpStatusCode.Conflict, "File upload not successful")
            }
        }
    }
}