package tests.notificationRouteTests

import io.ktor.http.*
import org.junit.Test
import tests.handleGetRequest
import tests.runServer
import tests.runWithLoggedUser
import kotlin.test.assertEquals

class NotificationsRoute {

    @Test
    fun `should return Unauthorised if not logged`() {
        runServer {
            handleGetRequest("/notification") {
                assertEquals(HttpStatusCode.Unauthorized, response.status())
            }
        }
    }

    @Test
    fun `should return Ok if logged`() {
        runServer {
            runWithLoggedUser {
                handleGetRequest("/notification") {
                    assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }
}