package tests.accountRouteTests

import io.ktor.http.*
import org.junit.Test
import org.koin.test.KoinTest
import tests.handleGetRequest
import tests.runServer
import tests.runWithLoggedUser
import kotlin.test.assertEquals

class AccountRoute : KoinTest {

    @Test
    fun `should return unauthorised if user is not logged`() {
        runServer {
            handleGetRequest("/account") {
                assertEquals(HttpStatusCode.Unauthorized, response.status())
            }
        }
    }

    @Test
    fun `should return ok if user is logged`() {
        runServer {
            runWithLoggedUser {
                handleGetRequest("/account") {
                    assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }

    @Test
    fun `should redirect if logout is successful`() {
        runServer {
            runWithLoggedUser {
                handleGetRequest("/account/logout") {
                    assertEquals(HttpStatusCode.Found, response.status())
                }
            }
        }
    }
}