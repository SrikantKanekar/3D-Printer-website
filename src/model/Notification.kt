package com.example.model

import com.example.util.now
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class Notification(
    val title: String,
    val message: String,
    val posted_at: String = now(),
    @BsonId
    val id: String = ObjectId().toString()
)
