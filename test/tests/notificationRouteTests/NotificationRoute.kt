package tests.notificationRouteTests

import data.TestConstants.TEST_INVALID_ID
import data.TestConstants.TEST_NOTIFICATION_ID
import io.ktor.http.*
import org.junit.Test
import tests.handleGetRequest
import tests.runServer
import tests.runWithLoggedUser
import kotlin.test.assertEquals

class NotificationRoute {

    @Test
    fun `should return Unauthorised if not logged`() {
        runServer {
                handleGetRequest("/notification/$TEST_NOTIFICATION_ID") {
                    assertEquals(HttpStatusCode.Unauthorized, response.status())
                }
        }
    }

    @Test
    fun `should return Ok if logged`() {
        runServer {
            runWithLoggedUser {
                handleGetRequest("/notification/$TEST_NOTIFICATION_ID") {
                    assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }

    @Test
    fun `should return Not Found for invalid notification Id`() {
        runServer {
            runWithLoggedUser {
                handleGetRequest("/notification/$TEST_INVALID_ID") {
                    assertEquals(HttpStatusCode.NotFound, response.status())
                }
            }
        }
    }
}