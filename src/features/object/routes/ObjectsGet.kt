package com.example.features.objects.routes

import com.example.features.`object`.data.ObjectRepository
import com.example.model.UserPrincipal
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.objectsGet(objectsRepository: ObjectRepository) {
    get {
        val principal = call.principal<UserPrincipal>()!!
        val objs = objectsRepository.getObjects(principal.email)
        call.respond(objs)
    }
}