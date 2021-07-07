package tests.orderRouteTests

import data.TestConstants.TEST_INVALID_ID
import data.TestConstants.TEST_PLACED_ORDER
import io.ktor.http.*
import org.junit.Test
import org.koin.test.KoinTest
import tests.handleGetRequest
import tests.runServer
import tests.runWithAdminUser
import tests.runWithLoggedUser
import kotlin.test.assertEquals

class OrderRoute: KoinTest {

    @Test
    fun `should return Not Found for not logged user`() {
        runServer {
            handleGetRequest("/order/$TEST_PLACED_ORDER") {
                assertEquals(HttpStatusCode.NotFound, response.status())
            }
        }
    }

    @Test
    fun `should return Not Found if invalid order Id and logged user`() {
        runServer {
            runWithLoggedUser {
                handleGetRequest("/order/$TEST_INVALID_ID") {
                    assertEquals(HttpStatusCode.NotFound, response.status())
                }
            }
        }
    }

    @Test
    fun `should return Not Found if invalid order Id and admin user`() {
        runServer {
            runWithAdminUser {
                handleGetRequest("/order/$TEST_INVALID_ID") {
                    assertEquals(HttpStatusCode.NotFound, response.status())
                }
            }
        }
    }
    @Test
    fun `should return ok for logged user`() {
        runServer {
            runWithLoggedUser {
                handleGetRequest("/order/$TEST_PLACED_ORDER") {
                    assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }

    @Test
    fun `should return ok for admin user`() {
        runServer {
            runWithAdminUser {
                handleGetRequest("/order/$TEST_PLACED_ORDER") {
                    assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }
}