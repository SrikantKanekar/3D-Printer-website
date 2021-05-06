package com.example.features.`object`.domain

import com.example.features.`object`.domain.ObjectStatus.CREATED
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Object(
    val fileName: String,
    val status: ObjectStatus = CREATED,
    val basicSettings: BasicSettings = BasicSettings(),
    val advancedSettings: AdvancedSettings = AdvancedSettings(),
    val price: Int? = null,
    val timeInMin: Int? = null,
    val dueDelivery: String? = null,
    @BsonId
    val id: String = ObjectId().toString()
)