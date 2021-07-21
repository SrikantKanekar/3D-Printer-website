package tests.adminRouteTests

import com.example.util.enums.ObjectStatus.COMPLETED
import com.example.util.enums.OrderStatus.*
import data.TestConstants.TEST_CONFIRMED_ORDER
import data.TestConstants.TEST_DELIVERING_ORDER
import data.TestConstants.TEST_INVALID_ID
import data.TestConstants.TEST_PLACED_ORDER
import data.TestConstants.TEST_PROCESSED_ORDER
import data.TestConstants.TEST_PROCESSING_ORDER
import data.TestConstants.TEST_USER_EMAIL
import fakeDataSource.TestRepository
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject
import tests.handlePutRequest
import tests.runServer
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class UpdateOrderStatus : KoinTest {

    @Test
    fun `should return Unauthorised if not logged`() {
        runServer {
            handlePutRequest(
                "/admin/order-status/$TEST_PLACED_ORDER",
                body = "CONFIRMED"
            ) {
                assertEquals(HttpStatusCode.Unauthorized, response.status())
            }
        }
    }

    @Test
    fun `should return Unauthorised for non admin user`() {
        runServer {
            handlePutRequest(
                uri = "/admin/order-status/$TEST_PLACED_ORDER",
                body = "CONFIRMED",
                logged = true
            ) {
                assertEquals(HttpStatusCode.Unauthorized, response.status())
            }
        }
    }

    @Test
    fun `should return MethodNotAllowed for invalid order Id`() {
        runServer {
            handlePutRequest(
                uri = "/admin/order-status/$TEST_INVALID_ID",
                body = "CONFIRMED",
                admin = true
            ) {
                assertEquals(HttpStatusCode.MethodNotAllowed, response.status())
            }
        }
    }

    @Test
    fun `should return MethodNotAllowed if Processed without Confirmed`() {
        runServer {
            handlePutRequest(
                "/admin/order-status/$TEST_PLACED_ORDER",
                body = "PROCESSING",
                admin = true
            ) {
                assertEquals(HttpStatusCode.MethodNotAllowed, response.status())
            }
        }
    }

    @Test
    fun `should return MethodNotAllowed if delivering without printing all objects`() {
        runServer {
            handlePutRequest(
                "/admin/order-status/$TEST_PROCESSING_ORDER",
                body = "DELIVERING",
                admin = true
            ) {
                assertEquals(HttpStatusCode.MethodNotAllowed, response.status())
            }
        }
    }

    @Test
    fun `should return Ok for Confirmed status`() {
        runServer {
            handlePutRequest(
                "/admin/order-status/$TEST_PLACED_ORDER",
                body = "CONFIRMED",
                admin = true
            ) {
                runBlocking {
                    val testRepository by inject<TestRepository>()
                    val order = testRepository.getActiveOrderById(TEST_PLACED_ORDER)!!
                    assertEquals(CONFIRMED, order.status)

                    val notification = testRepository.getUser(TEST_USER_EMAIL).notification[1]
                    assertTrue { notification.subject.contains("Confirmed") }

                    assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }

    @Test
    fun `should return Ok for Processing status`() {
        runServer {
            handlePutRequest(
                "/admin/order-status/$TEST_CONFIRMED_ORDER",
                body = "PROCESSING",
                admin = true
            ) {
                runBlocking {
                    val testRepository by inject<TestRepository>()
                    val order = testRepository.getActiveOrderById(TEST_CONFIRMED_ORDER)!!
                    assertEquals(PROCESSING, order.status)

                    assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }

    @Test
    fun `should return Ok for Delivering status`() {
        runServer {
            handlePutRequest(
                "/admin/order-status/$TEST_PROCESSED_ORDER",
                body = "DELIVERING",
                admin = true
            ) {
                runBlocking {
                    val testRepository by inject<TestRepository>()
                    val order = testRepository.getActiveOrderById(TEST_PROCESSED_ORDER)!!
                    assertEquals(DELIVERING, order.status)

                    val notification = testRepository.getUser(TEST_USER_EMAIL).notification[1]
                    assertTrue { notification.subject.contains("delivery") }

                    assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }

    @Test
    fun `should return Ok for Delivered status`() {
        runServer {
            handlePutRequest(
                uri = "/admin/order-status/$TEST_DELIVERING_ORDER",
                body = "DELIVERED",
                admin = true
            ) {
                runBlocking {
                    val testRepository by inject<TestRepository>()
                    val order = testRepository.getCompletedOrderById(TEST_DELIVERING_ORDER)!!
                    assertEquals(DELIVERED, order.status)

                    val user = testRepository.getUser(TEST_USER_EMAIL)
                    val notification = user.notification[1]
                    assertTrue { notification.subject.contains("delivered") }

                    user.objects
                        .filter { order.objectIds.contains(it.id) }
                        .forEach { assertTrue { it.status == COMPLETED } }

                    assertNotNull(order.deliveredOn)

                    assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }
}