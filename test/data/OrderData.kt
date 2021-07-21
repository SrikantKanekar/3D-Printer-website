package data

import com.example.model.Order
import com.example.util.enums.OrderStatus.*
import data.TestConstants.TEST_COMPLETED_OBJECT
import data.TestConstants.TEST_CONFIRMED_ORDER
import data.TestConstants.TEST_DELIVERED_ORDER
import data.TestConstants.TEST_DELIVERING_ORDER
import data.TestConstants.TEST_PENDING_OBJECT
import data.TestConstants.TEST_PLACED_ORDER
import data.TestConstants.TEST_PRINTED_OBJECT
import data.TestConstants.TEST_PRINTING_OBJECT
import data.TestConstants.TEST_PROCESSED_ORDER
import data.TestConstants.TEST_PROCESSING_ORDER
import data.TestConstants.TEST_TRACKING_OBJECT
import data.TestConstants.TEST_UNKNOWN_USER_ORDER
import data.TestConstants.TEST_USER_EMAIL

val testOrderData = listOf(
    Order(
        userEmail = TEST_USER_EMAIL,
        status = PLACED,
        objectIds = ArrayList(listOf(TEST_TRACKING_OBJECT, TEST_PENDING_OBJECT)),
        price = 100,
        deliveredOn = "10 May",
        id = TEST_PLACED_ORDER
    ),
    Order(
        userEmail = TEST_USER_EMAIL,
        status = CONFIRMED,
        objectIds = ArrayList(listOf(TEST_TRACKING_OBJECT, TEST_PENDING_OBJECT)),
        price = 100,
        deliveredOn = "10 May",
        id = TEST_CONFIRMED_ORDER
    ),
    Order(
        userEmail = TEST_USER_EMAIL,
        status = PROCESSING,
        objectIds = ArrayList(listOf(
            TEST_TRACKING_OBJECT,
            TEST_PENDING_OBJECT,
            TEST_PRINTING_OBJECT,
            TEST_PRINTED_OBJECT
        )),
        price = 100,
        deliveredOn = "10 May",
        id = TEST_PROCESSING_ORDER
    ),
    Order(
        userEmail = TEST_USER_EMAIL,
        status = PROCESSING,
        objectIds = ArrayList(listOf(
            TEST_PRINTED_OBJECT
        )),
        price = 100,
        deliveredOn = "10 May",
        id = TEST_PROCESSED_ORDER
    ),
    Order(
        userEmail = TEST_USER_EMAIL,
        status = DELIVERING,
        objectIds = ArrayList(listOf(TEST_PRINTED_OBJECT)),
        price = 100,
        deliveredOn = "10 May",
        id = TEST_DELIVERING_ORDER
    ),
    Order(
        userEmail = TEST_USER_EMAIL,
        status = DELIVERED,
        objectIds = ArrayList(listOf(TEST_COMPLETED_OBJECT, TEST_PRINTED_OBJECT)),
        price = 100,
        deliveredOn = "10 May",
        id = TEST_DELIVERED_ORDER
    ),
    Order(
        userEmail = "UNKNOWN_USER",
        status = PLACED,
        objectIds = ArrayList(listOf(TEST_TRACKING_OBJECT, TEST_PENDING_OBJECT)),
        price = 100,
        deliveredOn = "10 May",
        id = TEST_UNKNOWN_USER_ORDER
    )
).shuffled()