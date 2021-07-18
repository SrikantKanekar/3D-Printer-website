package tests.orderRouteTests

import com.example.util.enums.PrintingStatus
import data.TestConstants.TEST_CONFIRMED_ORDER
import data.TestConstants.TEST_INVALID_ID
import data.TestConstants.TEST_PENDING_OBJECT
import data.TestConstants.TEST_PRINTING_OBJECT
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

class PrintingStatus : KoinTest {

    @Test
    fun `should return Unauthorised in not logged`() {
        runServer {
            handlePostRequest(
                "/order/update/printing-status",
                mapOf(
                    "orderId" to TEST_PROCESSING_ORDER,
                    "objectId" to TEST_PENDING_OBJECT,
                    "printing_status" to "1"
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
                    "/order/update/printing-status",
                    mapOf(
                        "orderId" to TEST_PROCESSING_ORDER,
                        "objectId" to TEST_PENDING_OBJECT,
                        "printing_status" to "1"
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
                    "/order/update/printing-status",
                    mapOf(
                        "orderId" to TEST_INVALID_ID,
                        "objectId" to TEST_PENDING_OBJECT,
                        "printing_status" to "1"
                    )
                ) {
                    assertEquals("false", response.content)
                }
            }
        }
    }

    @Test
    fun `should return false for non processing order`() {
        runServer {
            runWithAdminUser {
                handlePostRequest(
                    "/order/update/printing-status",
                    mapOf(
                        "orderId" to TEST_CONFIRMED_ORDER,
                        "objectId" to TEST_PENDING_OBJECT,
                        "printing_status" to "1"
                    )
                ) {
                    assertEquals("false", response.content)
                }
            }
        }
    }

    @Test
    fun `should return false for invalid object Id`() {
        runServer {
            runWithAdminUser {
                handlePostRequest(
                    "/order/update/printing-status",
                    mapOf(
                        "orderId" to TEST_PROCESSING_ORDER,
                        "objectId" to TEST_INVALID_ID,
                        "printing_status" to "1"
                    )
                ) {
                    assertEquals("false", response.content)
                }
            }
        }
    }

    @Test
    fun `should return false if printed without printing`() {
        runServer {
            runWithAdminUser {
                handlePostRequest(
                    "/order/update/printing-status",
                    mapOf(
                        "orderId" to TEST_PROCESSING_ORDER,
                        "objectId" to TEST_PENDING_OBJECT,
                        "printing_status" to "2"
                    )
                ) {
                    assertEquals("false", response.content)
                }
            }
        }
    }

    @Test
    fun `should return true if printing started`() {
        runServer {
            runWithAdminUser {
                handlePostRequest(
                    "/order/update/printing-status",
                    mapOf(
                        "orderId" to TEST_PROCESSING_ORDER,
                        "objectId" to TEST_PENDING_OBJECT,
                        "printing_status" to "1"
                    )
                ) {
                    runBlocking {
                        val testRepository by inject<TestRepository>()
                        val user = testRepository.getUser(TEST_USER_EMAIL)

                        val obj = user.objects.find { it.id == TEST_PENDING_OBJECT }!!
                        assertEquals(obj.printingStatus, PrintingStatus.PRINTING)
                        assertNotNull(obj.trackingDetails.started_at)

                        val notification = user.notification[1]
                        assertTrue { notification.title.contains("Started") }

                        assertEquals("true", response.content)
                    }
                }
            }
        }
    }

    @Test
    fun `should return true if printing completed`() {
        runServer {
            runWithAdminUser {
                handlePostRequest(
                    "/order/update/printing-status",
                    mapOf(
                        "orderId" to TEST_PROCESSING_ORDER,
                        "objectId" to TEST_PRINTING_OBJECT,
                        "printing_status" to "2"
                    )
                ) {
                    runBlocking {
                        val testRepository by inject<TestRepository>()
                        val user = testRepository.getUser(TEST_USER_EMAIL)

                        val obj = user.objects.find { it.id == TEST_PRINTING_OBJECT }!!
                        assertEquals(obj.printingStatus, PrintingStatus.PRINTED)
                        assertNotNull(obj.trackingDetails.completed_at)

                        assertEquals("true", response.content)
                    }
                }
            }
        }
    }
}