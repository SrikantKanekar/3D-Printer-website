package tests.adminRouteTests

import com.example.features.admin.requests.NotificationRequest
import data.TestConstants.TEST_USER_EMAIL
import fakeDataSource.TestRepository
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject
import tests.handlePostRequest
import tests.runServer
import kotlin.test.assertEquals

class SendNotification : KoinTest {

    private val notificationRequest = NotificationRequest(
        email = TEST_USER_EMAIL,
        subject = "Subject",
        body = "Body..."
    )

    @Test
    fun `should return Unauthorised if not logged`() {
        runServer {
            handlePostRequest(
                uri = "/admin/notification",
                body = notificationRequest
            ) {
                assertEquals(HttpStatusCode.Unauthorized, response.status())
            }
        }
    }

    @Test
    fun `should return Forbidden for logged user`() {
        runServer {
            handlePostRequest(
                uri = "/admin/notification",
                body = notificationRequest,
                logged = true
            ) {
                assertEquals(HttpStatusCode.Unauthorized, response.status())
            }
        }
    }

    @Test
    fun `should return notification if successful`() {
        runServer {
            handlePostRequest(
                uri = "/admin/notification",
                body = notificationRequest,
                admin = true
            ) {
                runBlocking {
                    val testRepository by inject<TestRepository>()
                    val user = testRepository.getUser(TEST_USER_EMAIL)
                    val notification = user.notification.find { it.body == "Body..." }!!

                    val res = Json.decodeFromString<NotificationRequest>(response.content!!)

                    assertEquals(res.subject, notification.subject)
                    assertEquals(res.body, notification.body)
                }
            }
        }
    }
}