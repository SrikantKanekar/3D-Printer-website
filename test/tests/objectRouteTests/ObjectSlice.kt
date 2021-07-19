package tests.objectRouteTests

import com.example.model.ObjectsCookie
import data.TestConstants.TEST_CART_OBJECT
import data.TestConstants.TEST_CREATED_OBJECT
import data.TestConstants.TEST_INVALID_ID
import data.TestConstants.TEST_SLICED_OBJECT
import data.TestConstants.TEST_UNSLICED_OBJECT
import data.TestConstants.TEST_USER_EMAIL
import fakeDataSource.TestRepository
import io.ktor.http.*
import io.ktor.server.testing.*
import io.ktor.sessions.*
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject
import tests.`create object before user login`
import tests.handlePatchRequest
import tests.runServer
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class ObjectSlice : KoinTest {

    @Test
    fun `should return error for invalid Id if not logged`() {
        runServer {
            handlePatchRequest(
                uri = "objects/slice/$TEST_INVALID_ID",
                body = Unit
            ) {
                assertEquals(HttpStatusCode.InternalServerError, response.status())
            }
        }
    }

    @Test
    fun `should return error for invalid Id if logged`() {
        runServer {
            handlePatchRequest(
                uri = "objects/slice/$TEST_INVALID_ID",
                body = Unit,
                logged = true
            ) {
                assertEquals(HttpStatusCode.InternalServerError, response.status())
            }
        }
    }

    @Test
    fun `should return slicing details if not logged`() {
        runServer {
            cookiesSession {
                `create object before user login`()
                handlePatchRequest(
                    uri = "objects/slice/$TEST_CREATED_OBJECT",
                    body = Unit,
                ) {
                    val cookie = response.call.sessions.get<ObjectsCookie>()!!
                    val obj = cookie.objects.find { it.id == TEST_CREATED_OBJECT }
                    assertEquals(true, obj?.slicingDetails?.uptoDate)
                }
            }
        }
    }

    @Test
    fun `should return error for sliced object if not logged`() {
        runServer {
            cookiesSession {
                `create object before user login`()
                handlePatchRequest(
                    uri = "objects/slice/$TEST_CREATED_OBJECT",
                    body = Unit
                ) {
                    assertNotEquals(HttpStatusCode.InternalServerError, response.status())
                }
                handlePatchRequest(
                    uri = "objects/slice/$TEST_CREATED_OBJECT",
                    body = Unit
                ) {
                    assertEquals(HttpStatusCode.InternalServerError, response.status())
                }
            }
        }
    }

    @Test
    fun `should return error for sliced object if logged`() {
        runServer {
            handlePatchRequest(
                uri = "objects/slice/$TEST_SLICED_OBJECT",
                body = Unit,
                logged = true
            ) {
                assertEquals(HttpStatusCode.InternalServerError, response.status())
            }
        }
    }

    @Test
    fun `should return error for cart object if logged`() {
        runServer {
            handlePatchRequest(
                uri = "objects/slice/$TEST_CART_OBJECT",
                body = Unit,
                logged = true
            ) {
                assertEquals(HttpStatusCode.InternalServerError, response.status())
            }
        }
    }

    @Test
    fun `should return slicing details if logged`() {
        runServer {
            handlePatchRequest(
                uri = "objects/slice/$TEST_UNSLICED_OBJECT",
                body = Unit,
                logged = true
            ) {
                runBlocking {
                    val testRepository by inject<TestRepository>()
                    val obj = testRepository.getUserObject(TEST_USER_EMAIL, TEST_SLICED_OBJECT)
                    assertEquals(true, obj?.slicingDetails?.uptoDate)
                }
            }
        }
    }
}
