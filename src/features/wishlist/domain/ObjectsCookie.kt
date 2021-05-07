package com.example.features.wishlist.domain

import com.example.features.`object`.domain.Object
import com.google.gson.Gson
import io.ktor.sessions.*
import kotlinx.serialization.Serializable
import java.lang.reflect.Type

data class ObjectsCookie(
    val objects: ArrayList<Object> = ArrayList()
)

class ObjectsCookieSerializer: SessionSerializer<ObjectsCookie> {
    override fun deserialize(text: String): ObjectsCookie {
        return Gson().fromJson(text, ObjectsCookie::class.java)
    }

    override fun serialize(session: ObjectsCookie): String {
        return Gson().toJson(session)
    }
}
