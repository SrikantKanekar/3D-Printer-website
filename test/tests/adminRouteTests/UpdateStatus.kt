package tests.adminRouteTests

import com.example.features.`object`.domain.ObjectStatus.COMPLETED
import com.example.features.order.domain.OrderStatus.*
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
import tests.handlePostRequest
import tests.runServer
import tests.runWithAdminUser
import tests.runWithLoggedUser
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class UpdateStatus : KoinTest {

    @Test
    fun `should return Unauthorised if not logged`() {
        runServer {
            handlePostRequest(
                "/admin/update/order-status",
                listOf(
                    "id" to TEST_PLACED_ORDER,
                    "order_status" to "1"
                )
            ) {
                assertEquals(HttpStatusCode.Unauthorized, response.status())
            }
        }
    }

    @Test
    fun `should return Unauthorised for non admin user`() {
        runServer {
            runWithLoggedUser {
                handlePostRequest(
                    "/admin/update/order-status",
                    listOf(
                        "id" to TEST_PLACED_ORDER,
                        "order_status" to "1"
                    )
                ) {
                    assertEquals(HttpStatusCode.Unauthorized, response.status())
                }
            }
        }
    }

    @Test
    fun `should return false for invalid order Id`() {
        runServer {
            runWithAdminUser {
                handlePostRequest(
                    "/admin/update/order-status",
                    listOf(
                        "id" to TEST_INVALID_ID,
                        "order_status" to "1"
                    )
                ) {
                    assertEquals("false", response.content)
                }
            }
        }
    }

    @Test
    fun `should return false if Processed without Confirmed`() {
        runServer {
            runWithAdminUser {
                handlePostRequest(
                    "/admin/update/order-status",
                    listOf(
                        "id" to TEST_PLACED_ORDER,
                        "order_status" to "2"
                    )
                ) {
                    assertEquals("false", response.content)
                }
            }
        }
    }

    @Test
    fun `should return false if Delivering without printing all objects`() {
        runServer {
            runWithAdminUser {
                handlePostRequest(
                    "/admin/update/order-status",
                    listOf(
                        "id" to TEST_PROCESSING_ORDER,
                        "order_status" to "3"
                    )
                ) {
                    assertEquals("false", response.content)
                }
            }
        }
    }

    @Test
    fun `should return true for Confirmed status`() {
        runServer {
            runWithAdminUser {
                handlePostRequest(
                    "/admin/update/order-status",
                    listOf(
                        "id" to TEST_PLACED_ORDER,
                        "order_status" to "1"
                    )
                ) {
                    runBlocking {
                        val testRepository by inject<TestRepository>()
                        val order = testRepository.getActiveOrder(TEST_PLACED_ORDER)!!
                        assertEquals(CONFIRMED, order.status)

                        val notification = testRepository.getUser(TEST_USER_EMAIL).notification[1]
                        assertTrue { notification.title.contains("Confirmed") }

                        assertEquals("true", response.content)
                    }
                }
            }
        }
    }

    @Test
    fun `should return true for Processing status`() {
        runServer {
            runWithAdminUser {
                handlePostRequest(
                    "/admin/update/order-status",
                    listOf(
                        "id" to TEST_CONFIRMED_ORDER,
                        "order_status" to "2"
                    )
                ) {
                    runBlocking {
                        val testRepository by inject<TestRepository>()
                        val order = testRepository.getActiveOrder(TEST_CONFIRMED_ORDER)!!
                        assertEquals(PROCESSING, order.status)

                        assertEquals("true", response.content)
                    }
                }
            }
        }
    }

    @Test
    fun `should return true for Delivering status`() {
        runServer {
            runWithAdminUser {
                handlePostRequest(
                    "/admin/update/order-status",
                    listOf(
                        "id" to TEST_PROCESSED_ORDER,
                        "order_status" to "3"
                    )
                ) {
                    runBlocking {
                        val testRepository by inject<TestRepository>()
                        val order = testRepository.getActiveOrder(TEST_PROCESSED_ORDER)!!
                        assertEquals(DELIVERING, order.status)

                        val notification = testRepository.getUser(TEST_USER_EMAIL).notification[1]
                        assertTrue { notification.title.contains("delivery") }

                        assertEquals("true", response.content)
                    }
                }
            }
        }
    }

    @Test
    fun `should return true for Delivered status`() {
        runServer {
            runWithAdminUser {
                handlePostRequest(
                    "/admin/update/order-status",
                    listOf(
                        "id" to TEST_DELIVERING_ORDER,
                        "order_status" to "4"
                    )
                ) {
                    runBlocking {
                        val testRepository by inject<TestRepository>()
                        val order = testRepository.getCompletedOrder(TEST_DELIVERING_ORDER)!!
                        assertEquals(DELIVERED, order.status)

                        val user = testRepository.getUser(TEST_USER_EMAIL)
                        val notification = user.notification[1]
                        assertTrue { notification.title.contains("delivered") }

                        user.objects
                            .filter { order.objectIds.contains(it.id) }
                            .forEach { assertTrue { it.status == COMPLETED } }

                        assertNotNull(order.deliveredOn)

                        assertEquals("true", response.content)
                    }
                }
            }
        }
    }
}