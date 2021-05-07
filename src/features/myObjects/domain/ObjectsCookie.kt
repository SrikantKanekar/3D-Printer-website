package com.example.features.myObjects.domain

import com.example.features.`object`.domain.Object
import com.google.gson.Gson
import io.ktor.sessions.*

data class ObjectsCookie(
    val objects: ArrayList<Object> = ArrayList()
)

class ObjectCookieSerializer: SessionSerializer<ObjectsCookie> {
    override fun deserialize(text: String): ObjectsCookie {
        return Gson().fromJson(text, ObjectsCookie::class.java)
    }

    override fun serialize(session: ObjectsCookie): String {
        return Gson().toJson(session)
    }
}
