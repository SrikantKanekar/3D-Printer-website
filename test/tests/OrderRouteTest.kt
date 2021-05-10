package tests

import com.example.module
import data.Constants.TEST_PLACED_ORDER
import data.Constants.TEST_PROCESSING_ORDER
import data.Constants.TEST_TRACKING_OBJECT
import di.testModule
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.Test
import kotlin.test.assertEquals

class OrderRouteTest {

    @Test
    fun `get order route test`() {
        withTestApplication({ module(testing = true, koinModules = listOf(testModule)) }) {
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
        withTestApplication({ module(testing = true, koinModules = listOf(testModule)) }) {
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
        withTestApplication({ module(testing = true, koinModules = listOf(testModule)) }) {
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
}