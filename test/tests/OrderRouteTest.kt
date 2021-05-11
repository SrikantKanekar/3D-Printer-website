package tests

import com.example.features.account.data.AccountRepository
import com.example.module
import data.Constants.TEST_PLACED_ORDER
import data.Constants.TEST_PROCESSING_ORDER
import data.Constants.TEST_TRACKING_OBJECT
import data.Constants.TEST_USER_EMAIL
import di.testModules
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class OrderRouteTest: KoinTest {

    val accountRepository by inject<AccountRepository>()

    @Test
    fun `get order route test`() {
        withTestApplication({ module(testing = true, koinModules = testModules) }) {
            handleRequest(HttpMethod.Get, "/order/$TEST_PLACED_ORDER").apply {
                assertEquals(HttpStatusCode.NotFound, response.status())
            }
            runWithTestUser {
                handleRequest(HttpMethod.Get, "/order/$TEST_PROCESSING_ORDER").apply {
                    assertEquals(HttpStatusCode.OK, response.status())
                }
            }
            runWithAdminUser {
                handleRequest(HttpMethod.Get, "/order/$TEST_PLACED_ORDER").apply {
                    assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }

    @Test
    fun `get order route invalid ID`() {
        withTestApplication({ module(testing = true, koinModules = testModules) }) {
            handleRequest(HttpMethod.Get, "/order/invalid-order-id").apply {
                assertEquals(HttpStatusCode.NotFound, response.status())
            }
            runWithTestUser {
                handleRequest(HttpMethod.Get, "/order/invalid-order-id").apply {
                    assertEquals(HttpStatusCode.NotFound, response.status())
                }
            }
            runWithAdminUser {
                handleRequest(HttpMethod.Get, "/order/invalid-order-id").apply {
                    assertEquals(HttpStatusCode.NotFound, response.status())
                }
            }
        }
    }

    @Test
    fun `update Printing Status Test success`() {
        withTestApplication({ module(testing = true, koinModules = testModules) }) {
            runWithAdminUser {
                handleRequest(HttpMethod.Post, "/order/update/printing-status") {
                    addHeader(HttpHeaders.ContentType, formUrlEncoded)
                    setBody(
                        listOf(
                            "orderId" to TEST_PROCESSING_ORDER,
                            "objectId" to TEST_TRACKING_OBJECT,
                            "printing_status" to "1"
                        ).formUrlEncode()
                    )
                }.apply {
                    assertEquals("updated", response.content)
                }
            }
        }
    }

    @Test
    fun `send custom message to user test`() {
        withTestApplication({ module(testing = true, koinModules = testModules) }) {
            runWithAdminUser {
                handleRequest(HttpMethod.Post, "/order/send-message") {
                    addHeader(HttpHeaders.ContentType, formUrlEncoded)
                    setBody(
                        listOf(
                            "email" to TEST_USER_EMAIL,
                            "title" to "custom title",
                            "message" to "custom message"
                        ).formUrlEncode()
                    )
                }.apply {
                    runBlocking {
                        val user = accountRepository.getUser(TEST_USER_EMAIL)
                        val notification = user.notification.find { it.message == "custom message" }
                        assertNotNull(notification)
                        assertEquals("Notification sent", response.content)
                    }
                }
            }
        }
    }
}