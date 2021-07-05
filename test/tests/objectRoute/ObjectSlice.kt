package tests.objectRoute

import data.TestConstants.TEST_CART_OBJECT1
import data.TestConstants.TEST_CREATED_OBJECT
import data.TestConstants.TEST_INVALID_ID
import data.TestConstants.TEST_SLICED_OBJECT
import data.TestConstants.TEST_UNSLICED_OBJECT
import io.ktor.server.testing.*
import org.junit.Test
import org.koin.test.KoinTest
import tests.`create object before user login`
import tests.handlePostRequest
import tests.runTest
import tests.runWithLoggedUser
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class ObjectSlice : KoinTest {

    @Test
    fun `should return null for invalid Id if not logged`() {
        runTest {
            handlePostRequest(
                "object/slice",
                listOf("id" to TEST_INVALID_ID)
            ) {
                assertEquals("null", response.content)
            }
        }
    }

    @Test
    fun `should return null for invalid Id if logged`() {
        runTest {
            runWithLoggedUser {
                handlePostRequest(
                    "object/slice",
                    listOf("id" to TEST_INVALID_ID)
                ) {
                    assertEquals("null", response.content)
                }
            }
        }
    }

    @Test
    fun `should return slicing details if not logged`() {
        runTest {
            cookiesSession {
                `create object before user login`()
                handlePostRequest(
                    "object/slice",
                    listOf("id" to TEST_CREATED_OBJECT)
                ) {
                    assertNotEquals("null", response.content)
                }
            }
        }
    }

    @Test
    fun `should return null for sliced object if not logged`() {
        runTest {
            cookiesSession {
                `create object before user login`()
                handlePostRequest(
                    "object/slice",
                    listOf("id" to TEST_CREATED_OBJECT)
                ) {
                    assertNotEquals("null", response.content)
                }
                handlePostRequest(
                    "object/slice",
                    listOf("id" to TEST_CREATED_OBJECT)
                ) {
                    assertEquals("null", response.content)
                }
            }
        }
    }

    @Test
    fun `should return null for sliced object if logged`() {
        runTest {
            runWithLoggedUser {
                handlePostRequest(
                    "object/slice",
                    listOf("id" to TEST_SLICED_OBJECT)
                ) {
                    assertEquals("null", response.content)
                }
            }
        }
    }

    @Test
    fun `should return null for cart object if logged`() {
        runTest {
            runWithLoggedUser {
                handlePostRequest(
                    "object/slice",
                    listOf("id" to TEST_CART_OBJECT1)
                ) {
                    assertEquals("null", response.content)
                }
            }
        }
    }

    @Test
    fun `should return slicing details if logged`() {
        runTest {
            runWithLoggedUser {
                handlePostRequest(
                    "object/slice",
                    listOf("id" to TEST_UNSLICED_OBJECT)
                ) {
                    assertNotEquals("null", response.content)
                }
            }
        }
    }
}
