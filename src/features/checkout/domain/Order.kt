package com.example.features.checkout.domain

import com.example.features.checkout.domain.OrderStatus.*
import com.example.features.`object`.domain.Object
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Order(
    val userEmail: String,
    var status: OrderStatus = PLACED,
    val objects: ArrayList<Object>,
    @BsonId
    val id: String = ObjectId().toString()
)
