package com.example.routes

import com.example.data.database.Constants.HELLO_WORLD_RESPONSE
import com.example.data.models.IndexData
import com.example.service.HelloService
import io.ktor.application.*
import io.ktor.freemarker.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject

fun Application.registerTestRoutes(){

    val service: HelloService by inject()

    routing {
        route("/test"){
            testHelloWorldRoute()
            testKoinRoute(service.sayHello())
            testFreemarkerRoute()
        }
    }
}

fun Route.testHelloWorldRoute(){
    get("/hello-world") {
        call.respondText(text = HELLO_WORLD_RESPONSE, contentType = ContentType.Text.Plain)
    }
}

fun Route.testKoinRoute(string: String) {
    get("/koin") {
        call.respondText(text = string, contentType = ContentType.Text.Plain)
    }
}

fun Route.testFreemarkerRoute() {
    get("/freemarker") {
        call.respond(FreeMarkerContent("index.ftl", mapOf("data" to IndexData(listOf(1, 2, 3))), ""))
    }
}