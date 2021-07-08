package tests.adminRouteTests

import io.ktor.http.*
import org.junit.Test
import org.koin.test.KoinTest
import tests.handleGetRequest
import tests.runServer
import tests.runWithAdminUser
import tests.runWithLoggedUser
import kotlin.test.assertEquals

class AdminRoute : KoinTest {

    @Test
    fun `should return Unauthorised if not logged`() {
        runServer {
            handleGetRequest("/admin") {
                assertEquals(HttpStatusCode.Unauthorized, response.status())
            }
        }
    }

    @Test
    fun `should return Unauthorised if not admin user`() {
        runServer {
            runWithLoggedUser {
                handleGetRequest("/admin") {
                    assertEquals(HttpStatusCode.Unauthorized, response.status())
                }
            }
        }
    }

    @Test
    fun `should return Ok if admin user`() {
        runServer {
            runWithAdminUser {
                handleGetRequest("/admin") {
                    assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }
}