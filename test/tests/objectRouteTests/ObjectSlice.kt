package tests.objectRouteTests

import data.TestConstants.TEST_CART_OBJECT
import data.TestConstants.TEST_CREATED_OBJECT
import data.TestConstants.TEST_INVALID_ID
import data.TestConstants.TEST_SLICED_OBJECT
import data.TestConstants.TEST_UNSLICED_OBJECT
import io.ktor.server.testing.*
import org.junit.Test
import org.koin.test.KoinTest
import tests.`create object before user login`
import tests.handlePostRequest
import tests.runServer
import tests.runWithLoggedUser
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class ObjectSlice : KoinTest {

    @Test
    fun `should return null for invalid Id if not logged`() {
        runServer {
            handlePostRequest(
                "object/slice",
                mapOf("id" to TEST_INVALID_ID)
            ) {
                assertEquals("null", response.content)
            }
        }
    }

    @Test
    fun `should return null for invalid Id if logged`() {
        runServer {
            runWithLoggedUser {
                handlePostRequest(
                    "object/slice",
                    mapOf("id" to TEST_INVALID_ID)
                ) {
                    assertEquals("null", response.content)
                }
            }
        }
    }

    @Test
    fun `should return slicing details if not logged`() {
        runServer {
            cookiesSession {
                `create object before user login`()
                handlePostRequest(
                    "object/slice",
                    mapOf("id" to TEST_CREATED_OBJECT)
                ) {
                    assertNotEquals("null", response.content)
                }
            }
        }
    }

    @Test
    fun `should return null for sliced object if not logged`() {
        runServer {
            cookiesSession {
                `create object before user login`()
                handlePostRequest(
                    "object/slice",
                    mapOf("id" to TEST_CREATED_OBJECT)
                ) {
                    assertNotEquals("null", response.content)
                }
                handlePostRequest(
                    "object/slice",
                    mapOf("id" to TEST_CREATED_OBJECT)
                ) {
                    assertEquals("null", response.content)
                }
            }
        }
    }

    @Test
    fun `should return null for sliced object if logged`() {
        runServer {
            runWithLoggedUser {
                handlePostRequest(
                    "object/slice",
                    mapOf("id" to TEST_SLICED_OBJECT)
                ) {
                    assertEquals("null", response.content)
                }
            }
        }
    }

    @Test
    fun `should return null for cart object if logged`() {
        runServer {
            runWithLoggedUser {
                handlePostRequest(
                    "object/slice",
                    mapOf("id" to TEST_CART_OBJECT)
                ) {
                    assertEquals("null", response.content)
                }
            }
        }
    }

    @Test
    fun `should return slicing details if logged`() {
        runServer {
            runWithLoggedUser {
                handlePostRequest(
                    "object/slice",
                    mapOf("id" to TEST_UNSLICED_OBJECT)
                ) {
                    assertNotEquals("null", response.content)
                }
            }
        }
    }
}
