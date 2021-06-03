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
import kotlin.random.Random

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
            try {

//                val file = createFile(obj.id)
                // uncomment to mock file upload error
                //throw Exception()
//                partData.streamProvider().use { inputStream ->
//                    file.outputStream().buffered().use { outputStream ->
//                        inputStream.copyTo(outputStream)
//                        partData.dispose.invoke()
//                    }
//                }

                // get details from octoPrint or any software
                try {
                    delay(1500)
                    // uncomment to mock error from software
                    //throw Exception()
                    obj.apply {
                        image = "/static/images/3d-image.jpg"
                        price = Random.nextInt(1000, 10000)
                        timeToPrint = Random.nextInt(1, 24)
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