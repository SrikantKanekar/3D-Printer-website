package tests.objectRouteTests

import com.example.model.ObjectsCookie
import data.TestConstants.TEST_CART_OBJECT
import data.TestConstants.TEST_CREATED_OBJECT
import data.TestConstants.TEST_INVALID_ID
import data.TestConstants.TEST_SLICED_OBJECT
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
import tests.handleDeleteRequest
import tests.runServer
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class ObjectDelete : KoinTest {

    @Test
    fun `should return not found if invalid Id before login`() {
        runServer {
            handleDeleteRequest(
                uri = "/objects/$TEST_INVALID_ID"
            ) {
                assertEquals(HttpStatusCode.NotFound, response.status())
            }
        }
    }

    @Test
    fun `should return not found if invalid Id after login`() {
        runServer {
            handleDeleteRequest(
                uri = "/objects/$TEST_INVALID_ID",
                logged = true
            ) {
                assertEquals(HttpStatusCode.NotFound, response.status())
            }
        }
    }

    @Test
    fun `should return ok if object deleted before login`() {
        runServer {
            cookiesSession {
                `create object before user login`()
                handleDeleteRequest(
                    "/objects/$TEST_CREATED_OBJECT"
                ) {
                    val cookie = response.call.sessions.get<ObjectsCookie>()!!
                    assertNull(cookie.objects.find { it.id == TEST_CREATED_OBJECT })

                    assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }

    @Test
    fun `should return ok if object deleted after login`() {
        runServer {
            handleDeleteRequest(
                "/objects/$TEST_SLICED_OBJECT",
                logged = true
            ) {
                runBlocking {
                    val testRepository by inject<TestRepository>()
                    val obj = testRepository.getUserObjectById(TEST_USER_EMAIL, TEST_SLICED_OBJECT)
                    assertNull(obj)

                    assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }

    @Test
    fun `should return not found if cart object is deleted`() {
        runServer {
            handleDeleteRequest(
                "/objects/$TEST_CART_OBJECT",
                logged = true
            ) {
                runBlocking {
                    val testRepository by inject<TestRepository>()
                    val obj = testRepository.getUserObjectById(TEST_USER_EMAIL, TEST_CART_OBJECT)
                    assertNotNull(obj)

                    assertEquals(HttpStatusCode.NotFound, response.status())
                }
            }
        }
    }
}