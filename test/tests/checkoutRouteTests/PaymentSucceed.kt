package tests.checkoutRouteTests

import com.example.features.`object`.domain.ObjectStatus.TRACKING
import com.example.features.order.domain.OrderStatus.PLACED
import data.TestConstants.TEST_CART_OBJECT
import data.TestConstants.TEST_CREATED_ORDER
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
import kotlin.test.assertTrue

class PaymentSucceed : KoinTest {

    @Test
    fun `should return Unauthorised if not logged`() {
        runServer {
            handlePostRequest(
                "/checkout/success",
                listOf("success" to "true")
            ) {
                assertEquals(HttpStatusCode.Unauthorized, response.status())
            }
        }
    }

    @Test
    fun `should return error if payment verification fails`() {
        runServer {
            runWithLoggedUser {
                handlePostRequest(
                    "/checkout/success",
                    listOf("success" to "false")
                ) {
                    assertNotEquals("true", response.content)
                }
            }
        }
    }

    @Test
    fun `should return true if success`() {
        runServer {
            runWithLoggedUser {
                handlePostRequest(
                    "/checkout/success",
                    listOf("success" to "true")
                ) {
                    runBlocking {
                        val testRepository by inject<TestRepository>()

                        val order = testRepository.getActiveOrder(TEST_CREATED_ORDER)!!
                        assertEquals(PLACED, order.status)
                        assertEquals(TEST_USER_EMAIL, order.userEmail)
                        assertEquals(TEST_CART_OBJECT, order.objectIds[0])
                        assertEquals(4, order.price)

                        val user = testRepository.getUser(TEST_USER_EMAIL)
                        assertTrue { user.orderIds.contains(order.id) }
                        assertTrue {
                            user.objects
                                .filter { it.status == TRACKING }
                                .map { it.id }
                                .containsAll(order.objectIds)
                        }

                        assertEquals("true", response.content)
                    }
                }
            }
        }
    }
}