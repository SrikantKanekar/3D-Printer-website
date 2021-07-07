package tests.orderRouteTests

import data.TestConstants.TEST_USER_EMAIL
import fakeDataSource.TestRepository
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject
import tests.handlePostRequest
import tests.runServer
import tests.runWithAdminUser
import tests.runWithLoggedUser
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class SendMessage : KoinTest {

    @Test
    fun `should return Unauthorised if not logged`() {
        runServer {
            handlePostRequest(
                "/order/send-message",
                listOf(
                    "email" to TEST_USER_EMAIL,
                    "title" to "title1234",
                    "message" to "message1234"
                )
            ) {
                assertEquals(HttpStatusCode.Unauthorized, response.status())
            }
        }
    }

    @Test
    fun `should return Unauthorised for logged user`() {
        runServer {
            runWithLoggedUser {
                handlePostRequest(
                    "/order/send-message",
                    listOf(
                        "email" to TEST_USER_EMAIL,
                        "title" to "title1234",
                        "message" to "message1234"
                    )
                ) {
                    assertEquals(HttpStatusCode.Unauthorized, response.status())
                }
            }
        }
    }

    @Test
    fun `should return true if notification sent`() {
        runServer {
            runWithAdminUser {
                handlePostRequest(
                    "/order/send-message",
                    listOf(
                        "email" to TEST_USER_EMAIL,
                        "title" to "title1234",
                        "message" to "message1234"
                    )
                ) {
                    runBlocking {
                        val testRepository by inject<TestRepository>()
                        val user = testRepository.getUser(TEST_USER_EMAIL)
                        val notification = user.notification.find { it.message == "message1234" }
                        assertNotNull(notification)

                        assertEquals("true", response.content)
                    }
                }
            }
        }
    }
}