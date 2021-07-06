package tests.cartRouteTests

import io.ktor.http.*
import org.junit.Test
import tests.handleGetRequest
import tests.runServer
import tests.runWithLoggedUser
import kotlin.test.assertEquals

class CartRoute {

    @Test
    fun `should return unauthorised if not logged`() {
        runServer {
            handleGetRequest("/cart") {
                assertEquals(HttpStatusCode.Unauthorized, response.status())
            }
        }
    }

    @Test
    fun `should return ok if logged`() {
        runServer {
            runWithLoggedUser {
                handleGetRequest("/cart") {
                    assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }
}