package tests.notificationRouteTests

import com.example.model.Notification
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

class NotificationsGet {

    @Test
    fun `should return Unauthorised if not logged`() {
        runServer {
            handleGetRequest("/notifications") {
                assertEquals(HttpStatusCode.Unauthorized, response.status())
            }
        }
    }

    @Test
    fun `should return notifications if logged`() {
        runServer {
            handleGetRequest(
                uri = "/notifications",
                logged = true
            ) {
                runBlocking {
                    val testRepository by inject<TestRepository>()
                    val user = testRepository.getUser(TEST_USER_EMAIL)
                    val notifications = user.notification

                    val res = Json.decodeFromString<List<Notification>>(response.content!!)
                    assertEquals(res, notifications)
                }
            }
        }
    }
}