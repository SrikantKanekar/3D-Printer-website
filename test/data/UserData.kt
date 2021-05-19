package data

import com.example.features.`object`.domain.Object
import com.example.features.`object`.domain.ObjectStatus.*
import com.example.features.account.domain.User
import com.example.features.checkout.domain.Address
import com.example.features.notification.domain.Notification
import data.Constants.TEST_CART_OBJECT1
import data.Constants.TEST_CART_OBJECT2
import data.Constants.TEST_COMPLETED_OBJECT
import data.Constants.TEST_NOTIFICATION
import data.Constants.TEST_OBJECT_IMAGE_LINK
import data.Constants.TEST_PROCESSING_ORDER
import data.Constants.TEST_USER_OBJECT
import data.Constants.TEST_TRACKING_OBJECT
import data.Constants.TEST_USER_ADDRESS
import data.Constants.TEST_USER_EMAIL
import data.Constants.TEST_USER_HASHED_PASSWORD
import data.Constants.TEST_USER_USERNAME
import java.util.*
import kotlin.collections.ArrayList

val testUser = User(
    email = TEST_USER_EMAIL,
    password = TEST_USER_HASHED_PASSWORD,
    username = TEST_USER_USERNAME,
    address = Address(
        city = TEST_USER_ADDRESS,
        state = TEST_USER_ADDRESS,
        country = TEST_USER_ADDRESS,
        phoneNumber = 1234567899,
        pinCode = 123456
    ),
    objects = ArrayList(
        listOf(
            Object(
                id = TEST_USER_OBJECT,
                filename = UUID.randomUUID().toString(),
                image = TEST_OBJECT_IMAGE_LINK,
                price = 1,
                status = NONE
            ),
            Object(
                id = TEST_CART_OBJECT1,
                filename = UUID.randomUUID().toString(),
                image = TEST_OBJECT_IMAGE_LINK,
                price = 1,
                status = CART
            ),
            Object(
                id = TEST_CART_OBJECT2,
                filename = UUID.randomUUID().toString(),
                image = TEST_OBJECT_IMAGE_LINK,
                price = 1,
                status = CART
            ),
            Object(
                id = TEST_TRACKING_OBJECT,
                filename = UUID.randomUUID().toString(),
                image = TEST_OBJECT_IMAGE_LINK,
                price = 1,
                status = TRACKING
            ),
            Object(
                id = TEST_COMPLETED_OBJECT,
                filename = UUID.randomUUID().toString(),
                image = TEST_OBJECT_IMAGE_LINK,
                price = 1,
                status = COMPLETED
            )
        )
    ),
    orderIds = ArrayList(
        listOf(
            TEST_PROCESSING_ORDER
        )
    ),
    notification = ArrayList(
        listOf(
            Notification(
                title = "TEST_NOTIFICATION_TITLE",
                message = "TEST_NOTIFICATION_MESSAGE",
                id = TEST_NOTIFICATION
            )
        )
    )
)

val testUsers = List(10) {
    User(
        email = UUID.randomUUID().toString(),
        password = UUID.randomUUID().toString(),
        username = UUID.randomUUID().toString(),
        address = Address(
            city = UUID.randomUUID().toString(),
            state = UUID.randomUUID().toString(),
            country = UUID.randomUUID().toString()
        ),
        objects = ArrayList(
            List(3) {
                Object(
                    filename = UUID.randomUUID().toString(),
                    image = UUID.randomUUID().toString()
                )
            }
        )
    )
}

val testUserData = (testUsers + testUser).shuffled()