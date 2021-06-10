package com.example.features.`object`.presentation

import com.example.features.`object`.data.ObjectRepository
import com.example.features.auth.domain.UserPrincipal
import com.example.features.objects.domain.ObjectsCookie
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

/**
 * *User can create new objects without logging in...
 * 1) if user is not logged in, newly created objects will be stored in 'MY_OBJECTS_COOKIE'.
 * 2) users will be prompted to login when user tried to add object to Cart.
 * 3) after logging in, all the cookie objects will be synced with account object.
 */
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

/**
 * * As soon as user uploads file...
 * 1) the file will be stored inside project root folder 'uploads'.
 * 2) the file is named as its unique generated Id.
 * 3) After file is uploaded, the software(octo-print or any other) will scan the design.
 * 4) If the design cannot be printed on our printer, it will show error
 * 5) If any error occurs during file upload or by the software, the uploaded file will be deleted.
 */
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

                // check file using octoPrint or any software
                try {
                    delay(1500)
                    // uncomment to mock error from software
                    //throw Exception()

                    //get the image of the file
                    obj.apply {
                        image = "/static/images/3d-image.jpg"
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