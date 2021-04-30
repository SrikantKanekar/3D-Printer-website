package com.example.feautures.order.domain

import com.example.feautures.order.domain.OrderStatus.*
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Order(
    val fileName: String,
    val status: OrderStatus = CREATED,
    val basicSettings: BasicSettings = BasicSettings(),
    val advancedSettings: AdvancedSettings = AdvancedSettings(),
    val price: Int? = null,
    val timeInMin: Int? = null,
    val dueDelivery: String? = null,
    @BsonId
    val id: String = ObjectId().toString()
)