package tests.objectRouteTests

import data.TestConstants.TEST_CREATED_OBJECT
import data.TestConstants.TEST_INVALID_ID
import data.TestConstants.TEST_UNSLICED_OBJECT
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.Test
import org.koin.test.KoinTest
import tests.`create object before user login`
import tests.handleGetRequest
import tests.runServer
import tests.runWithLoggedUser
import kotlin.test.assertEquals

class ObjectRoute : KoinTest {

    @Test
    fun `should return ok if object found before login`() {
        runServer {
            cookiesSession {
                `create object before user login`()
                handleGetRequest("/object/$TEST_CREATED_OBJECT") {
                    assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }

    @Test
    fun `should return Not found if invalid Id before login`() {
        runServer {
            handleGetRequest("/object/$TEST_INVALID_ID") {
                assertEquals(HttpStatusCode.NotFound, response.status())
            }
        }
    }

    @Test
    fun `should return ok if object found after login`() {
        runServer {
            runWithLoggedUser {
                handleGetRequest("/object/$TEST_UNSLICED_OBJECT") {
                    assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }

    @Test
    fun `should return Not found if invalid Id after login`() {
        runServer {
            runWithLoggedUser {
                handleGetRequest("/object/$TEST_INVALID_ID") {
                    assertEquals(HttpStatusCode.NotFound, response.status())
                }
            }
        }
    }
}