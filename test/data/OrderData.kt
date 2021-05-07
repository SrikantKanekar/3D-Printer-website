package data

import com.example.features.`object`.domain.Object
import com.example.features.`object`.domain.ObjectStatus.TRACKING
import com.example.features.checkout.domain.Order
import com.example.features.checkout.domain.OrderStatus.*
import data.Constants.TEST_DELIVERED_ORDER
import data.Constants.TEST_DELIVERING_ORDER
import data.Constants.TEST_PLACED_ORDER
import data.Constants.TEST_PRINTED_ORDER
import data.Constants.TEST_PRINTING_ORDER
import data.Constants.TEST_USER_EMAIL
import java.util.*
import kotlin.random.Random

val testOrderData = List(15) {
    Order(
        id = when (it) {
            0 -> TEST_PLACED_ORDER
            1 -> TEST_PRINTING_ORDER
            2 -> TEST_PRINTED_ORDER
            3 -> TEST_DELIVERING_ORDER
            4 -> TEST_DELIVERED_ORDER
            else -> UUID.randomUUID().toString()
        },
        userEmail = when {
            it < 5 -> TEST_USER_EMAIL
            else -> UUID.randomUUID().toString()
        },
        status = when (it) {
            0 -> PLACED
            1 -> PRINTING
            2 -> PRINTED
            3 -> DELIVERING
            4 -> DELIVERED
            else -> DELIVERED
        },
        objects = ArrayList(
            List(Random.nextInt(2, 5)) {
                Object(
                    fileName = UUID.randomUUID().toString(),
                    status = TRACKING
                )
            }
        )
    )
}.shuffled()