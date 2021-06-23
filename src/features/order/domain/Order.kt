package com.example.features.order.domain

import com.example.features.order.domain.OrderStatus.PLACED
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class Order(
    val userEmail: String,
    var status: OrderStatus = PLACED,
    val objectIds: ArrayList<String> = ArrayList(),
    val image: String = "/static/images/3d-order-image.jpeg",
    var price: Int = 0,
    val deliveredOn: String? = null,
    @BsonId
    val id: String = ObjectId().toString()
)