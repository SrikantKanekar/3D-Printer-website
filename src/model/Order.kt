package com.example.model

import com.example.util.enums.OrderStatus
import com.example.util.enums.OrderStatus.PLACED
import com.example.util.now
import kotlinx.serialization.Serializable

@Serializable
data class Order(
    val _id: String,
    val userEmail: String,
    val objectIds: ArrayList<String> = ArrayList(),
    var status: OrderStatus = PLACED,
    var price: Int = 0,
    var razorpay: Razorpay = Razorpay(),
    val created_at: String = now(),
    val deliveredOn: String? = null
)

@Serializable
data class Razorpay(
    var order_id: String? = null,
    var payment_id: String? = null,
    var signature: String? = null
)