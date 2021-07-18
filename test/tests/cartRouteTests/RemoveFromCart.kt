package tests.cartRouteTests

import com.example.util.enums.ObjectStatus.NONE
import data.TestConstants.TEST_CART_OBJECT
import data.TestConstants.TEST_INVALID_ID
import data.TestConstants.TEST_TRACKING_OBJECT
import data.TestConstants.TEST_USER_EMAIL
import fakeDataSource.TestRepository
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject
import tests.handlePostRequest
import tests.runServer
import tests.runWithLoggedUser
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class RemoveFromCart : KoinTest {

    @Test
    fun `should return Unauthorised for if not logged`() {
        runServer {
            handlePostRequest(
                "/cart/remove",
                mapOf("id" to TEST_CART_OBJECT)
            ) {
                assertEquals(HttpStatusCode.Unauthorized, response.status())
            }
        }
    }

    @Test
    fun `should return BadRequest for invalid object Id`() {
        runServer {
            runWithLoggedUser {
                handlePostRequest(
                    "/cart/remove",
                    mapOf("id" to TEST_INVALID_ID)
                ) {
                    assertEquals("false", response.content)
                }
            }
        }
    }

    @Test
    fun `should return false for tracking object`() {
        runServer {
            runWithLoggedUser {
                handlePostRequest(
                    "/cart/remove",
                    mapOf("id" to TEST_TRACKING_OBJECT)
                ) {
                    runBlocking {
                        val testRepository by inject<TestRepository>()
                        val user = testRepository.getUser(TEST_USER_EMAIL)
                        val obj = user.objects
                            .filter { it.status == NONE }
                            .find { it.id == TEST_TRACKING_OBJECT }
                        assertNull(obj)

                        assertEquals("false", response.content)
                    }
                }
            }
        }
    }

    @Test
    fun `should return true if success`() {
        runServer {
            runWithLoggedUser {
                handlePostRequest(
                    "/cart/remove",
                    mapOf("id" to TEST_CART_OBJECT)
                ) {
                    runBlocking {
                        val testRepository by inject<TestRepository>()
                        val user = testRepository.getUser(TEST_USER_EMAIL)
                        val obj = user.objects
                            .filter { it.status == NONE }
                            .find { it.id == TEST_CART_OBJECT }
                        assertNotNull(obj)

                        assertEquals("true", response.content)
                    }
                }
            }
        }
    }
}