package tests.cartRouteTests

import com.example.features.`object`.domain.ObjectStatus.CART
import com.example.features.`object`.domain.ObjectStatus.TRACKING
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
import kotlin.test.assertNotEquals

class UpdateQuantity : KoinTest {

    @Test
    fun `should return Unauthorised if not logged`() {
        runServer {
            handlePostRequest(
                "/cart/quantity",
                mapOf(
                    "id" to TEST_CART_OBJECT,
                    "quantity" to "2"
                )
            ) {
                assertEquals(HttpStatusCode.Unauthorized, response.status())
            }
        }
    }

    @Test
    fun `should return false for invalid object Id`() {
        runServer {
            runWithLoggedUser {
                handlePostRequest(
                    "/cart/quantity",
                    mapOf(
                        "id" to TEST_INVALID_ID,
                        "quantity" to "2"
                    )
                ) {
                    assertEquals("false", response.content)
                }
            }
        }
    }

    @Test
    fun `should return false if quantity less than 1`() {
        runServer {
            runWithLoggedUser {
                handlePostRequest(
                    "/cart/quantity",
                    mapOf(
                        "id" to TEST_CART_OBJECT,
                        "quantity" to "0"
                    )
                ) {
                    runBlocking {
                        val testRepository by inject<TestRepository>()
                        val user = testRepository.getUser(TEST_USER_EMAIL)
                        val obj = user.objects
                            .filter { it.status == CART }
                            .find { it.id == TEST_CART_OBJECT }
                        assertNotEquals(obj?.quantity, 0)

                        assertEquals("false", response.content)
                    }
                }
            }
        }
    }

    @Test
    fun `should return false for tracking object`() {
        runServer {
            runWithLoggedUser {
                handlePostRequest(
                    "/cart/quantity",
                    mapOf(
                        "id" to TEST_TRACKING_OBJECT,
                        "quantity" to "2"
                    )
                ) {
                    runBlocking {
                        val testRepository by inject<TestRepository>()
                        val user = testRepository.getUser(TEST_USER_EMAIL)
                        val obj = user.objects
                            .filter { it.status == TRACKING }
                            .find { it.id == TEST_TRACKING_OBJECT }
                        assertNotEquals(obj?.quantity, 2)

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
                    "/cart/quantity",
                    mapOf(
                        "id" to TEST_CART_OBJECT,
                        "quantity" to "2"
                    )
                ) {
                    runBlocking {
                        val testRepository by inject<TestRepository>()
                        val user = testRepository.getUser(TEST_USER_EMAIL)
                        val obj = user.objects
                            .filter { it.status == CART }
                            .find { it.id == TEST_CART_OBJECT }
                        assertEquals(obj?.quantity, 2)

                        assertEquals("true", response.content)
                    }
                }
            }
        }
    }
}