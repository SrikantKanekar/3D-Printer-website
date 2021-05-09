package data

import com.example.features.`object`.domain.Object
import com.example.features.`object`.domain.ObjectStatus.*
import com.example.features.account.domain.User
import com.example.features.checkout.domain.Address
import data.Constants.TEST_CART_OBJECT
import data.Constants.TEST_COMPLETED_OBJECT
import data.Constants.TEST_USER_OBJECT
import data.Constants.TEST_TRACKING_OBJECT
import data.Constants.TEST_USER_ADDRESS
import data.Constants.TEST_USER_EMAIL
import data.Constants.TEST_USER_HASHED_PASSWORD
import data.Constants.TEST_USER_USERNAME
import java.util.*

val testUser = User(
    email = TEST_USER_EMAIL,
    password = TEST_USER_HASHED_PASSWORD,
    username = TEST_USER_USERNAME,
    address = Address(
        city = TEST_USER_ADDRESS,
        state = TEST_USER_ADDRESS,
        country = TEST_USER_ADDRESS
    ),
    objects = ArrayList(
        listOf(
            Object(
                id = TEST_USER_OBJECT,
                fileName = UUID.randomUUID().toString(),
                status = NONE
            ),
            Object(
                id = TEST_CART_OBJECT,
                fileName = UUID.randomUUID().toString(),
                status = CART
            ),
            Object(
                id = TEST_TRACKING_OBJECT,
                fileName = UUID.randomUUID().toString(),
                status = TRACKING
            ),
            Object(
                id = TEST_COMPLETED_OBJECT,
                fileName = UUID.randomUUID().toString(),
                status = COMPLETED
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
                Object(fileName = UUID.randomUUID().toString())
            }
        )
    )
}

val testUserData = (testUsers + testUser).shuffled()