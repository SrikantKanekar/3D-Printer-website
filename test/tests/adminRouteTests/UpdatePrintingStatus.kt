package tests.adminRouteTests

import com.example.features.admin.requests.PrintingStatusRequest
import com.example.util.enums.PrintingStatus.PRINTED
import com.example.util.enums.PrintingStatus.PRINTING
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
import tests.handlePutRequest
import tests.runServer
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class UpdatePrintingStatus : KoinTest {

    @Test
    fun `should return Unauthorised in not logged`() {
        runServer {
            handlePutRequest(
                "/admin/printing-status",
                body = PrintingStatusRequest(
                    orderId = TEST_PROCESSING_ORDER,
                    objectId = TEST_PENDING_OBJECT,
                    printingStatus = PRINTING
                )
            ) {
                assertEquals(HttpStatusCode.Unauthorized, response.status())
            }
        }
    }

    @Test
    fun `should return Unauthorised for non admin user`() {
        runServer {
            handlePutRequest(
                "/admin/printing-status",
                body = PrintingStatusRequest(
                    orderId = TEST_PROCESSING_ORDER,
                    objectId = TEST_PENDING_OBJECT,
                    printingStatus = PRINTING
                ),
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
                "/admin/printing-status",
                body = PrintingStatusRequest(
                    orderId = TEST_INVALID_ID,
                    objectId = TEST_PENDING_OBJECT,
                    printingStatus = PRINTING
                ),
                admin = true
            ) {
                assertEquals(HttpStatusCode.MethodNotAllowed, response.status())
            }
        }
    }


    @Test
    fun `should return MethodNotAllowed for non processing order`() {
        runServer {
            handlePutRequest(
                "/admin/printing-status",
                body = PrintingStatusRequest(
                    orderId = TEST_CONFIRMED_ORDER,
                    objectId = TEST_PENDING_OBJECT,
                    printingStatus = PRINTING
                ),
                admin = true
            ) {
                assertEquals(HttpStatusCode.MethodNotAllowed, response.status())
            }
        }
    }


    @Test
    fun `should return MethodNotAllowed for invalid object Id`() {
        runServer {
            handlePutRequest(
                "/admin/printing-status",
                body = PrintingStatusRequest(
                    orderId = TEST_PROCESSING_ORDER,
                    objectId = TEST_INVALID_ID,
                    printingStatus = PRINTING
                ),
                admin = true
            ) {
                assertEquals(HttpStatusCode.MethodNotAllowed, response.status())
            }
        }
    }


    @Test
    fun `should return MethodNotAllowed if printed without printing`() {
        runServer {
            handlePutRequest(
                "/admin/printing-status",
                body = PrintingStatusRequest(
                    orderId = TEST_PROCESSING_ORDER,
                    objectId = TEST_PENDING_OBJECT,
                    printingStatus = PRINTED
                ),
                admin = true
            ) {
                assertEquals(HttpStatusCode.MethodNotAllowed, response.status())
            }
        }
    }

    @Test
    fun `should return Ok if printing started`() {
        runServer {
            handlePutRequest(
                uri = "/admin/printing-status",
                body = PrintingStatusRequest(
                    orderId = TEST_PROCESSING_ORDER,
                    objectId = TEST_PENDING_OBJECT,
                    printingStatus = PRINTING
                ),
                admin = true
            ) {
                runBlocking {
                    val testRepository by inject<TestRepository>()
                    val user = testRepository.getUser(TEST_USER_EMAIL)

                    val obj = user.objects.find { it.id == TEST_PENDING_OBJECT }!!
                    assertEquals(obj.printingStatus, PRINTING)
                    assertNotNull(obj.trackingDetails.started_at)

                    val notification = user.notification[1]
                    assertTrue { notification.subject.contains("Started") }

                    assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }

    @Test
    fun `should return Ok if printing completed`() {
        runServer {
            handlePutRequest(
                "/admin/printing-status",
                body = PrintingStatusRequest(
                    orderId = TEST_PROCESSING_ORDER,
                    objectId = TEST_PRINTING_OBJECT,
                    printingStatus = PRINTED
                ),
                admin = true
            ) {
                runBlocking {
                    val testRepository by inject<TestRepository>()
                    val user = testRepository.getUser(TEST_USER_EMAIL)

                    val obj = user.objects.find { it.id == TEST_PRINTING_OBJECT }!!
                    assertEquals(obj.printingStatus, PRINTED)
                    assertNotNull(obj.trackingDetails.completed_at)

                    assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }
}