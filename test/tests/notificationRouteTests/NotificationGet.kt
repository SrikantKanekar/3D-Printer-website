package tests.notificationRouteTests

import com.example.model.Notification
import data.TestConstants.TEST_INVALID_ID
import data.TestConstants.TEST_NOTIFICATION_ID
import data.TestConstants.TEST_USER_EMAIL
import fakeDataSource.TestRepository
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.junit.Test
import org.koin.ktor.ext.inject
import tests.handleGetRequest
import tests.runServer
import kotlin.test.assertEquals

class NotificationGet {

    @Test
    fun `should return Unauthorised if not logged`() {
        runServer {
            handleGetRequest("/notifications/$TEST_NOTIFICATION_ID") {
                assertEquals(HttpStatusCode.Unauthorized, response.status())
            }
        }
    }

    @Test
    fun `should return Not Found for invalid notification Id`() {
        runServer {
            handleGetRequest(
                uri = "/notifications/$TEST_INVALID_ID",
                logged = true
            ) {
                assertEquals(HttpStatusCode.NotFound, response.status())
            }
        }
    }

    @Test
    fun `should return notification if logged`() {
        runServer {
            handleGetRequest(
                uri = "/notifications/$TEST_NOTIFICATION_ID",
                logged = true
            ) {
                runBlocking {
                    val testRepository by inject<TestRepository>()
                    val user = testRepository.getUser(TEST_USER_EMAIL)
                    val notification = user.notification[0]

                    val res = Json.decodeFromString<Notification>(response.content!!)
                    assertEquals(res, notification)
                }
            }
        }
    }
}