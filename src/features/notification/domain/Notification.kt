package com.example.features.notification.domain

import kotlinx.datetime.Clock
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class Notification(
    val title: String,
    val message: String,
    val posted_at: String = Clock.System.now().toString(),
    @BsonId
    val id: String = ObjectId().toString()
)
