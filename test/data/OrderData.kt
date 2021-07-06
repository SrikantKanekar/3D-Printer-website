package data

import com.example.features.order.domain.Order
import com.example.features.order.domain.OrderStatus.*
import data.TestConstants.TEST_DELIVERED_ORDER
import data.TestConstants.TEST_DELIVERING_ORDER
import data.TestConstants.TEST_PLACED_ORDER
import data.TestConstants.TEST_PROCESSING_ORDER
import data.TestConstants.TEST_TRACKING_OBJECT
import data.TestConstants.TEST_USER_EMAIL
import kotlinx.datetime.Clock
import java.util.*
import kotlin.random.Random

val testOrderData = List(8) {
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
        deliveredOn = Clock.System.now().toString()
    )
}.shuffled()