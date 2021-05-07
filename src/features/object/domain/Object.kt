package com.example.features.`object`.domain

import com.example.features.`object`.domain.ObjectStatus.NONE
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class Object(
    var fileName: String,
    var status: ObjectStatus = NONE,
    var basicSettings: BasicSettings = BasicSettings(),
    var advancedSettings: AdvancedSettings = AdvancedSettings(),
    val price: Int? = null,
    val timeInMin: Int? = null,
    val dueDelivery: String? = null,
    @BsonId
    val id: String = ObjectId().toString()
)