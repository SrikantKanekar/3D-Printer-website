package com.example.features.order.domain

import com.example.features.order.domain.OrderStatus.*
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class Order(
    val userEmail: String,
    var status: OrderStatus = PLACED,
    val objectIds: ArrayList<String> = ArrayList(),
    val price: Int,
    val deliveryDays: Int,
    @BsonId
    val id: String = ObjectId().toString()
)