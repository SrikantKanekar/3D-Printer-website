package com.example.setUp

import com.example.model.ObjectsCookie
import com.example.model.ObjectsCookie.ObjectCookieSerializer
import com.example.util.constants.Cookies.OBJECTS_COOKIE
import io.ktor.application.*
import io.ktor.sessions.*

fun Application.sessionSetup() {
    install(Sessions) {
        cookie<ObjectsCookie>(
            name = OBJECTS_COOKIE,
            storage = SessionStorageMemory()
        ) {
            serializer = ObjectCookieSerializer()
        }
    }
}