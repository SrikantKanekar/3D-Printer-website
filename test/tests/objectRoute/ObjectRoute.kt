package tests.objectRoute

import data.TestConstants.TEST_CREATED_OBJECT
import data.TestConstants.TEST_INVALID_ID
import data.TestConstants.TEST_UNSLICED_OBJECT
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.Test
import org.koin.test.KoinTest
import tests.`create object before user login`
import tests.handleGetRequest
import tests.runTest
import tests.runWithLoggedUser
import kotlin.test.assertEquals

class ObjectRoute : KoinTest {

    @Test
    fun `should return ok if object found before login`() {
        runTest {
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
        runTest {
            handleGetRequest("/object/$TEST_INVALID_ID") {
                assertEquals(HttpStatusCode.NotFound, response.status())
            }
        }
    }

    @Test
    fun `should return ok if object found after login`() {
        runTest {
            runWithLoggedUser {
                handleGetRequest("/object/$TEST_UNSLICED_OBJECT") {
                    assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }

    @Test
    fun `should return Not found if invalid Id after login`() {
        runTest {
            runWithLoggedUser {
                handleGetRequest("/object/$TEST_INVALID_ID") {
                    assertEquals(HttpStatusCode.NotFound, response.status())
                }
            }
        }
    }
}