package tests.checkoutRouteTests

import io.ktor.http.*
import org.junit.Test
import org.koin.test.KoinTest
import tests.handleGetRequest
import tests.runServer
import tests.runWithLoggedUser
import kotlin.test.assertEquals

class CheckoutRoute : KoinTest {

    @Test
    fun `should return Unauthorised if not logged`() {
        runServer {
            handleGetRequest("/checkout") {
                assertEquals(HttpStatusCode.Unauthorized, response.status())
            }
        }
    }

    @Test
    fun `should return Ok if logged`() {
        runServer {
            runWithLoggedUser {
                handleGetRequest("/checkout") {
                    assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }
}