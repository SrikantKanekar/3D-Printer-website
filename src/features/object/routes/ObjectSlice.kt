package com.example.features.`object`.presentation

import com.example.features.`object`.data.ObjectRepository
import com.example.model.ObjectsCookie
import com.example.model.SlicingDetails
import com.example.model.UserPrincipal
import com.example.util.enums.ObjectStatus.NONE
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*

fun Route.objectSlice(objectRepository: ObjectRepository) {
    patch("/slice/{id}") {

        val id = call.parameters["id"] ?: return@patch call.respond(
            status = HttpStatusCode.BadRequest,
            message = "Missing or malformed id"
        )

        val principal = call.principal<UserPrincipal>()

        var result: SlicingDetails? = null

        when (principal) {
            null -> {
                val cookie = call.sessions.get<ObjectsCookie>() ?: ObjectsCookie()
                val obj = cookie.objects
                    .filter { it.status == NONE && !it.slicingDetails.uptoDate }
                    .find { it.id == id }

                if (obj != null){
                    result = objectRepository.slice(obj.fileUrl)
                    result?.let {
                        cookie.objects
                            .find { it.id == id }
                            ?.apply {
                                slicingDetails.uptoDate = true
                                slicingDetails.time = result!!.time
                                slicingDetails.materialWeight = result!!.materialWeight
                                slicingDetails.materialCost = result!!.materialCost
                                slicingDetails.electricityCost = result!!.electricityCost
                                slicingDetails.totalPrice = result!!.totalPrice
                            }
                        call.sessions.set(cookie)
                    }
                }
            }
            else -> {
                result = objectRepository.sliceUserObject(principal.email, id)
            }
        }
        if (result != null){
            call.respond(result)
        } else {
            call.respond(HttpStatusCode.InternalServerError, "Slicing error")
        }
    }
}