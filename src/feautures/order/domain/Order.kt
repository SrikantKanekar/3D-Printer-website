package com.example.feautures.order.domain

import com.example.feautures.order.domain.Status.*
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Order(
    val fileName: String,
    val status: Status = CREATED,
    val basicSettings: BasicSettings = BasicSettings(),
    val advancedSettings: AdvancedSettings = AdvancedSettings(),
    val price: Int? = null,
    val timeInMin: Int? = null,
    val dueDelivery: String? = null,
    @BsonId
    val id: String = ObjectId().toString()
)

data class BasicSettings(
    val size: Int = 10
)

data class AdvancedSettings(
    val weight: Int = 20
)

enum class Status {
    CREATED, CONFIRMED, COMPLETED
}