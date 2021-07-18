package com.example.model

import com.example.util.enums.OrderStatus
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class Order(
    val userEmail: String,
    var status: OrderStatus = OrderStatus.PLACED,
    val objectIds: ArrayList<String> = ArrayList(),
    val image: String = "/static/images/3d-order-image.jpeg",
    var price: Int = 0,
    val deliveredOn: String? = null,
    @BsonId
    val id: String = ObjectId().toString()
)