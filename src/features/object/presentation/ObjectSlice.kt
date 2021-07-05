package com.example.features.`object`.presentation

import com.example.features.`object`.data.ObjectRepository
import com.example.features.`object`.domain.*
import com.example.features.`object`.domain.ObjectStatus.*
import com.example.features.auth.domain.UserPrincipal
import com.example.features.objects.data.ObjectsRepository
import com.example.features.objects.domain.ObjectsCookie
import io.ktor.application.*
import io.ktor.freemarker.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*

fun Route.slice(objectRepository: ObjectRepository) {
    post("/object/slice") {
        val param = call.receiveParameters()
        val id = param["id"] ?: return@post call.respond(HttpStatusCode.BadRequest)

        val principal = call.sessions.get<UserPrincipal>()
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
            call.respondText("null")
        }
    }
}