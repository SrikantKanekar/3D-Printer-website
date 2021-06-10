package com.example.features.`object`.presentation

import com.example.features.`object`.data.ObjectRepository
import com.example.features.`object`.domain.*
import com.example.features.`object`.domain.ObjectStatus.*
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
 * This page will show the details about a object depending upon the ObjectStatus of the object.
 * 1) [NONE]/[CART] - user can edit object properties.
 * 2) [TRACKING]
 * 3) [COMPLETED]
 */
fun Route.getObjectRoute(objectRepository: ObjectRepository) {
    get("/object/{id}") {

        val id = call.parameters["id"]!!
        val principal = call.sessions.get<UserPrincipal>()

        val obj = when (principal) {
            null -> call.sessions.get<ObjectsCookie>()?.objects?.find { it.id == id }
            else -> objectRepository.getUserObject(principal.email, id)
        }
        when (obj) {
            null -> call.respond(HttpStatusCode.NotFound)
            else -> call.respond(
                FreeMarkerContent(
                    "object.ftl", mapOf(
                        "object" to obj,
                        "user" to (principal?.email ?: "")
                    )
                )
            )
        }
    }
}

fun Route.slice(objectRepository: ObjectRepository) {
    post("/object/slice") {
        val param = call.receiveParameters()
        val id = param["id"] ?: return@post call.respond(HttpStatusCode.BadRequest)

        val principal = call.sessions.get<UserPrincipal>()
        var result: SlicingDetails?
        when (principal) {
            null -> {
                result = objectRepository.slice(id)
                result?.let {
                    val cookie = call.sessions.get<ObjectsCookie>() ?: ObjectsCookie()
                    cookie.objects
                        .filter { it.status == NONE }
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

fun Route.addToCart(objectRepository: ObjectRepository) {
    post("/object/add-to-cart") {
        val param = call.receiveParameters()
        val id = param["id"] ?: return@post call.respond(HttpStatusCode.BadRequest)

        val principal = call.sessions.get<UserPrincipal>()
        when (principal) {
            null -> call.respondText("/auth/login?returnUrl=/object/$id")
            else -> {
                val result = objectRepository.addToCart(principal.email, id)
                call.respond(result.toString())
            }
        }
    }
}

fun Route.removeFromCart(objectRepository: ObjectRepository) {
    post("/object/remove-from-cart") {
        val param = call.receiveParameters()
        val id = param["id"] ?: return@post call.respond(HttpStatusCode.BadRequest)

        val principal = call.sessions.get<UserPrincipal>()!!
        val result = objectRepository.removeFromCart(principal.email, id)
        call.respond(result)
    }
}

fun Route.updateBasicSettings(objectRepository: ObjectRepository) {
    post("/object/update/basic-settings") {

        val params = call.receiveParameters()

        val id = params["id"] ?: return@post call.respond(HttpStatusCode.BadRequest)
        val quality = params["quality"] ?: return@post call.respond(HttpStatusCode.BadRequest)
        val infill = params["infill"]?.toFloat() ?: return@post call.respond(HttpStatusCode.BadRequest)
        val gradualInfill = params["gradual_infill"]
        val support = params["support"]

        val basicSettings = BasicSetting(
            quality = Quality.valueOf(quality),
            infill = infill,
            gradualInfill = gradualInfill == "on",
            support = support == "on"
        )

        val principal = call.sessions.get<UserPrincipal>()
        var updated = false
        when (principal) {
            null -> {
                val cookie = call.sessions.get<ObjectsCookie>() ?: ObjectsCookie()
                cookie.objects
                    .filter { it.status == NONE || it.status == CART }
                    .find { it.id == id }
                    ?.let {
                        it.basicSetting = basicSettings
                        it.slicingDetails.uptoDate = false
                        updated = true
                    }
                call.sessions.set(cookie)
            }
            else -> updated =
                objectRepository.updateBasicSettings(principal.email, id, basicSettings)
        }
        call.respond(updated)
    }
}

fun Route.updateIntermediateSettings(objectRepository: ObjectRepository) {
    post("/object/update/intermediate-settings") {

        val params = call.receiveParameters()

        val id = params["id"] ?: return@post call.respond(HttpStatusCode.BadRequest)
        val layerHeight = params["layer_height"]?.toFloat() ?: return@post call.respond(HttpStatusCode.BadRequest)
        val infillDensity = params["infill_density"]?.toFloat() ?: return@post call.respond(HttpStatusCode.BadRequest)
        val infillPattern = params["infill_pattern"] ?: return@post call.respond(HttpStatusCode.BadRequest)
        val generateSupport = params["generate_support"]
        val supportStructure = params["support_structure"] ?: return@post call.respond(HttpStatusCode.BadRequest)
        val supportPlacement = params["support_placement"] ?: return@post call.respond(HttpStatusCode.BadRequest)
        val supportOverhangAngle = params["support_overhang_angle"]?.toFloat() ?: return@post call.respond(HttpStatusCode.BadRequest)
        val supportPattern = params["support_pattern"] ?: return@post call.respond(HttpStatusCode.BadRequest)
        val supportDensity = params["support_density"]?.toFloat() ?: return@post call.respond(HttpStatusCode.BadRequest)

        val basicSettings = IntermediateSetting(
            layerHeight = layerHeight,
            infillDensity = infillDensity,
            infillPattern = InfillPattern.valueOf(infillPattern),
            generateSupport = generateSupport == "on",
            supportStructure = SupportStructure.valueOf(supportStructure),
            supportPlacement = SupportPlacement.valueOf(supportPlacement),
            supportOverhangAngle = supportOverhangAngle,
            supportPattern = SupportPattern.valueOf(supportPattern),
            supportDensity = supportDensity
        )
        val principal = call.sessions.get<UserPrincipal>()
        var updated = false
        when (principal) {
            null -> {
                val cookie = call.sessions.get<ObjectsCookie>() ?: ObjectsCookie()
                cookie.objects
                    .filter { it.status == NONE || it.status == CART }
                    .find { it.id == id }
                    ?.let {
                        it.intermediateSetting = basicSettings
                        it.slicingDetails.uptoDate = false
                        updated = true
                    }
                call.sessions.set(cookie)
            }
            else -> updated =
                objectRepository.updateIntermediateSettings(principal.email, id, basicSettings)
        }
        call.respond(updated)
    }
}

fun Route.updateAdvancedSettings(objectRepository: ObjectRepository) {
    post("/object/update/advanced-settings") {
        val params = call.receiveParameters()
        val id = params["id"] ?: return@post call.respond(HttpStatusCode.BadRequest)

        val wallLineWidth = params["wall_line_width"]?.toFloat() ?: return@post call.respond(HttpStatusCode.BadRequest)
        val topBottomLineWidth = params["top_bottom_line_width"]?.toFloat() ?: return@post call.respond(HttpStatusCode.BadRequest)
        val wallThickness = params["wall_thickness"]?.toFloat() ?: return@post call.respond(HttpStatusCode.BadRequest)
        val wallLineCount = params["wall_line_count"]?.toInt() ?: return@post call.respond(HttpStatusCode.BadRequest)
        val topThickness = params["top_thickness"]?.toFloat() ?: return@post call.respond(HttpStatusCode.BadRequest)
        val bottomThickness = params["bottom_thickness"]?.toFloat() ?: return@post call.respond(HttpStatusCode.BadRequest)
        val infillSpeed = params["infill_speed"]?.toFloat() ?: return@post call.respond(HttpStatusCode.BadRequest)
        val outerWallSpeed = params["outer_wall_speed"]?.toFloat() ?: return@post call.respond(HttpStatusCode.BadRequest)
        val innerWallSpeed = params["inner_wall_speed"]?.toFloat() ?: return@post call.respond(HttpStatusCode.BadRequest)
        val topBottomSpeed = params["top_bottom_speed"]?.toFloat() ?: return@post call.respond(HttpStatusCode.BadRequest)
        val supportSpeed = params["support_speed"]?.toFloat() ?: return@post call.respond(HttpStatusCode.BadRequest)
        val printSequence = params["print_sequence"] ?: return@post call.respond(HttpStatusCode.BadRequest)

        val advancedSettings = AdvancedSetting(
            wallLineWidth = wallLineWidth,
            topBottomLineWidth = topBottomLineWidth,
            wallThickness = wallThickness,
            wallLineCount = wallLineCount,
            topThickness = topThickness,
            bottomThickness = bottomThickness,
            infillSpeed = infillSpeed,
            outerWallSpeed = outerWallSpeed,
            innerWallSpeed = innerWallSpeed,
            topBottomSpeed = topBottomSpeed,
            supportSpeed = supportSpeed,
            printSequence = PrintSequence.valueOf(printSequence)
        )

        val principal = call.sessions.get<UserPrincipal>()

        var updated = false
        when (principal) {
            null -> {
                val cookie = call.sessions.get<ObjectsCookie>() ?: ObjectsCookie()
                cookie.objects
                    .filter { it.status == NONE || it.status == CART }
                    .find { it.id == id }
                    ?.let {
                        it.advancedSetting = advancedSettings
                        it.slicingDetails.uptoDate = false
                        updated = true
                    }
                call.sessions.set(cookie)
            }
            else -> updated =
                objectRepository.updateAdvancedSettings(principal.email, id, advancedSettings)
        }
        call.respond(updated)
    }
}
