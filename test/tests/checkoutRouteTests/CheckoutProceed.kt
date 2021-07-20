package tests.checkoutRouteTests

import com.example.features.checkout.requests.CheckoutProceedRequest
import com.example.model.Order
import com.example.util.enums.ObjectStatus.TRACKING
import com.example.util.enums.OrderStatus.PLACED
import data.TestConstants.TEST_CART_OBJECT
import data.TestConstants.TEST_CREATED_ORDER
import data.TestConstants.TEST_USER_EMAIL
import fakeDataSource.TestRepository
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject
import tests.handlePostRequest
import tests.runServer
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CheckoutProceed : KoinTest {

    @Test
    fun `should return Unauthorised if not logged`() {
        runServer {
            handlePostRequest(
                uri = "/checkout/proceed",
                body = CheckoutProceedRequest(true)
            ) {
                assertEquals(HttpStatusCode.Unauthorized, response.status())
            }
        }
    }

    @Test
    fun `should return bad request if payment verification fails`() {
        runServer {
            handlePostRequest(
                uri = "/checkout/proceed",
                body = CheckoutProceedRequest(false),
                logged = true
            ) {
                assertEquals(HttpStatusCode.BadRequest, response.status())
            }
        }
    }

    @Test
    fun `should return created order if success`() {
        runServer {
            handlePostRequest(
                uri = "/checkout/proceed",
                body = CheckoutProceedRequest(true),
                logged = true
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

                    val responseOrder = Json.decodeFromString<Order>(response.content!!)
                    assertEquals(responseOrder, order)

                    assertEquals(HttpStatusCode.Created, response.status())
                }
            }
        }
    }
}