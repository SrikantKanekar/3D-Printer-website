package com.example.features.`object`.presentation

import com.example.features.`object`.data.ObjectRepository
import com.example.model.*
import com.example.util.enums.ObjectStatus
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*

fun Route.updateSetting(objectRepository: ObjectRepository) {
    post("/object/update-setting") {

        val params = call.receiveParameters()

        val id = params["id"] ?: return@post call.respond(HttpStatusCode.BadRequest)

        val advanced = params["advanced"]

        // basic
        val quality = params["quality"] ?: return@post call.respond(HttpStatusCode.BadRequest)
        val infill = params["infill"]?.toFloat() ?: return@post call.respond(HttpStatusCode.BadRequest)
        val gradualInfill = params["gradual_infill"]
        val support = params["support"]

        // intermediate
        val layerHeight = params["layer_height"]?.toFloat() ?: return@post call.respond(HttpStatusCode.BadRequest)
        val infillDensity = params["infill_density"]?.toFloat() ?: return@post call.respond(HttpStatusCode.BadRequest)
        val infillPattern = params["infill_pattern"] ?: return@post call.respond(HttpStatusCode.BadRequest)
        val generateSupport = params["generate_support"]
        val supportStructure = params["support_structure"] ?: return@post call.respond(HttpStatusCode.BadRequest)
        val supportPlacement = params["support_placement"] ?: return@post call.respond(HttpStatusCode.BadRequest)
        val supportOverhangAngle = params["support_overhang_angle"]?.toFloat() ?: return@post call.respond(
            HttpStatusCode.BadRequest)
        val supportPattern = params["support_pattern"] ?: return@post call.respond(HttpStatusCode.BadRequest)
        val supportDensity = params["support_density"]?.toFloat() ?: return@post call.respond(HttpStatusCode.BadRequest)

        // advanced
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

        val setting = Setting(
            advanced = advanced == "on",
            quality = Quality.valueOf(quality),
            infill = infill,
            gradualInfill = gradualInfill == "on",
            support = support == "on",
            layerHeight = layerHeight,
            infillDensity = infillDensity,
            infillPattern = InfillPattern.valueOf(infillPattern),
            generateSupport = generateSupport == "on",
            supportStructure = SupportStructure.valueOf(supportStructure),
            supportPlacement = SupportPlacement.valueOf(supportPlacement),
            supportOverhangAngle = supportOverhangAngle,
            supportPattern = SupportPattern.valueOf(supportPattern),
            supportDensity = supportDensity,
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
                    .filter { it.status == ObjectStatus.NONE }
                    .find { it.id == id }
                    ?.let {
                        it.setting = setting
                        it.slicingDetails.uptoDate = false
                        updated = true
                    }
                call.sessions.set(cookie)
            }
            else -> updated =
                objectRepository.updateSetting(principal.email, id, setting)
        }
        call.respond(updated)
    }
}
