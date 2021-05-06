package com.example.features.order.domain

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Order(
    val fileName: String,
    val status: OrderStatus? = null,
    val basicSettings: BasicSettings = BasicSettings(),
    val advancedSettings: AdvancedSettings = AdvancedSettings(),
    val price: Int? = null,
    val timeInMin: Int? = null,
    val dueDelivery: String? = null,
    @BsonId
    val id: String = ObjectId().toString()
)