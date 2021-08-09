package com.example.model

import com.example.util.enums.OrderStatus
import kotlinx.serialization.Serializable

@Serializable
data class Order(
    val _id: String,
    val userEmail: String,
    var status: OrderStatus = OrderStatus.PLACED,
    val objectIds: ArrayList<String> = ArrayList(),
    val image: String = "/static/images/3d-order-image.jpeg",
    var price: Int = 0,
    val deliveredOn: String? = null,
    var razorpay: Razorpay = Razorpay()
)

@Serializable
data class Razorpay(
    var order_id: String? = null,
    var payment_id: String? = null,
    var signature: String? = null
)