package tests.ordersRouteTests

import io.ktor.http.*
import org.junit.Test
import tests.handleGetRequest
import tests.runServer
import tests.runWithLoggedUser
import kotlin.test.assertEquals

class OrdersRoute {

    @Test
    fun `should return Unauthorised if not logged`() {
        runServer {
            handleGetRequest("/orders") {
                assertEquals(HttpStatusCode.Unauthorized, response.status())
            }
        }
    }

    @Test
    fun `should return Ok if logged`() {
        runServer {
            runWithLoggedUser {
                handleGetRequest("/orders") {
                    assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }
}