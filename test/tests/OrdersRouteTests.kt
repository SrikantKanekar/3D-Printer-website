package tests

import io.ktor.http.*
import org.junit.Test
import kotlin.test.assertEquals

class OrdersRouteTests {

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