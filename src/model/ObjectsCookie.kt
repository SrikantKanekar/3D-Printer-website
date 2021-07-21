package com.example.model

import io.ktor.sessions.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class ObjectsCookie(
    val objects: ArrayList<Object> = ArrayList()
)

class ObjectCookieSerializer: SessionSerializer<ObjectsCookie> {
    override fun deserialize(text: String): ObjectsCookie {
        return Json.decodeFromString(text)
    }

    override fun serialize(session: ObjectsCookie): String {
        return Json.encodeToString(session)
    }
}
