package data

import com.example.features.`object`.domain.Object
import com.example.features.`object`.domain.ObjectStatus.*
import com.example.features.`object`.domain.SlicingDetails
import com.example.features.account.domain.User
import com.example.features.checkout.domain.Address
import com.example.features.notification.domain.Notification
import data.TestConstants.TEST_CART_OBJECT1
import data.TestConstants.TEST_CART_OBJECT2
import data.TestConstants.TEST_COMPLETED_OBJECT
import data.TestConstants.TEST_NOTIFICATION
import data.TestConstants.TEST_OBJECT_IMAGE_LINK
import data.TestConstants.TEST_PROCESSING_ORDER
import data.TestConstants.TEST_SLICED_OBJECT
import data.TestConstants.TEST_TRACKING_OBJECT
import data.TestConstants.TEST_UNSLICED_OBJECT
import data.TestConstants.TEST_USER_ADDRESS
import data.TestConstants.TEST_USER_EMAIL
import data.TestConstants.TEST_USER_HASHED_PASSWORD
import data.TestConstants.TEST_USER_USERNAME
import java.util.*

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
                id = TEST_UNSLICED_OBJECT,
                name = UUID.randomUUID().toString(),
                fileUrl = "",
                fileExtension = "",
                imageUrl = TEST_OBJECT_IMAGE_LINK,
                status = NONE,
            ),
            Object(
                id = TEST_SLICED_OBJECT,
                name = UUID.randomUUID().toString(),
                fileUrl = "",
                fileExtension = "",
                imageUrl = TEST_OBJECT_IMAGE_LINK,
                status = NONE,
                slicingDetails = SlicingDetails(uptoDate = true)
            ),
            Object(
                id = TEST_CART_OBJECT1,
                name = UUID.randomUUID().toString(),
                fileUrl = "",
                fileExtension = "",
                imageUrl = TEST_OBJECT_IMAGE_LINK,
                status = CART
            ),
            Object(
                id = TEST_CART_OBJECT2,
                name = UUID.randomUUID().toString(),
                fileUrl = "",
                fileExtension = "",
                imageUrl = TEST_OBJECT_IMAGE_LINK,
                status = CART
            ),
            Object(
                id = TEST_TRACKING_OBJECT,
                name = UUID.randomUUID().toString(),
                fileUrl = "",
                fileExtension = "",
                imageUrl = TEST_OBJECT_IMAGE_LINK,
                status = TRACKING
            ),
            Object(
                id = TEST_COMPLETED_OBJECT,
                name = UUID.randomUUID().toString(),
                fileUrl = "",
                fileExtension = "",
                imageUrl = TEST_OBJECT_IMAGE_LINK,
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

val testUsers = List(4) {
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
            List(2) {
                Object(
                    id = UUID.randomUUID().toString(),
                    name = UUID.randomUUID().toString(),
                    fileUrl = "",
                    fileExtension = "",
                    imageUrl = UUID.randomUUID().toString()
                )
            }
        )
    )
}

val testUserData = (testUsers + testUser).shuffled()