package data

import com.example.features.order.domain.Order
import com.example.features.order.domain.OrderStatus.*
import data.Constants.TEST_DELIVERED_ORDER
import data.Constants.TEST_DELIVERING_ORDER
import data.Constants.TEST_PLACED_ORDER
import data.Constants.TEST_PROCESSING_ORDER
import data.Constants.TEST_TRACKING_OBJECT
import data.Constants.TEST_USER_EMAIL
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random

val testOrderData = List(15) {
    Order(
        id = when (it) {
            0 -> TEST_PLACED_ORDER
            1 -> TEST_PROCESSING_ORDER
            2 -> TEST_DELIVERING_ORDER
            3 -> TEST_DELIVERED_ORDER
            else -> UUID.randomUUID().toString()
        },
        userEmail = when {
            it < 5 -> TEST_USER_EMAIL
            else -> UUID.randomUUID().toString()
        },
        status = when (it) {
            0 -> PLACED
            1 -> PROCESSING
            2 -> DELIVERING
            3 -> DELIVERED
            else -> DELIVERED
        },
        objectIds = when(it){
            1 -> ArrayList(listOf(TEST_TRACKING_OBJECT))
            else -> ArrayList(
                List(Random.nextInt(2, 5)) {
                    UUID.randomUUID().toString()
                }
            )
        },
        price = Random.nextInt(1000, 10000),
        deliveryDays = Random.nextInt(4, 15)
    )
}.shuffled()