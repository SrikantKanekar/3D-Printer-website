package features.`object`

import com.example.features.account.data.AccountRepository
import com.example.features.`object`.data.OrderRepository
import com.example.module
import data.Constants.TEST_CREATED_ORDER
import data.Constants.TEST_FILE_UPLOAD_NAME
import data.Constants.TEST_USER_EMAIL
import data.Constants.TEST_WISHLIST_ORDER
import di.testAuthModule
import features.auth.runWithTestUser
import features.auth.testUserLogin
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class OrderRouteTest : KoinTest {

    private val orderRepository by inject<OrderRepository>()
    private val accountRepository by inject<AccountRepository>()

    @Test
    fun `get order route test with or without login`() {
        withTestApplication({ module(testing = true, koinModules = listOf(testAuthModule)) }) {
            handleRequest(HttpMethod.Get, "/order").apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }
            runWithTestUser {
                handleRequest(HttpMethod.Get, "/order").apply {
                    assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }

    @Test
    fun `create order without login check cookie`() {
        withTestApplication({ module(testing = true, koinModules = listOf(testAuthModule)) }) {
            `create order without login`(orderRepository)
            confirmAndDeleteFile(TEST_CREATED_ORDER)
        }
    }

    @Test
    fun `create order after login success`() {
        withTestApplication({ module(testing = true, koinModules = listOf(testAuthModule)) }) {
            runWithTestUser {
                `create order after login`(accountRepository, orderRepository)
                confirmAndDeleteFile(TEST_CREATED_ORDER)
            }
        }
    }

    @Test
    fun `wishlist cookie sync test success`() {
        withTestApplication({ module(testing = true, koinModules = listOf(testAuthModule)) }) {
            cookiesSession {
                `create order without login`(orderRepository)
                testUserLogin()

                runBlocking {

                    val order = orderRepository.getOrder(TEST_CREATED_ORDER)!!
                    assertEquals(TEST_FILE_UPLOAD_NAME, order.fileName)

                    confirmAndDeleteFile(order.id)
                }
            }
        }
    }

    @Test
    fun `get update order route success`() {
        withTestApplication({ module(testing = true, koinModules = listOf(testAuthModule)) }) {
            handleRequest(HttpMethod.Get, "/order/$TEST_WISHLIST_ORDER").apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }
        }
    }

    @Test
    fun `get update order route failure`() {
        withTestApplication({ module(testing = true, koinModules = listOf(testAuthModule)) }) {
            handleRequest(HttpMethod.Get, "/order/invalid-order-id").apply {
                assertEquals(HttpStatusCode.NotAcceptable, response.status())
            }
        }
    }

    @Test
    fun `update order file success`() {
        withTestApplication({ module(testing = true, koinModules = listOf(testAuthModule)) }) {

            // Temporarily add empty test file before test
            createFileAndConfirm(TEST_WISHLIST_ORDER)

            handleRequest(HttpMethod.Post, "/order/$TEST_WISHLIST_ORDER/file") {
                val boundary = "***bbb***"
                addHeader(
                    HttpHeaders.ContentType,
                    ContentType.MultiPart.FormData.withParameter("boundary", boundary).toString()
                )
                setBody(boundary, listOf(uploadFile))
            }.apply {
                runBlocking {
                    val order = orderRepository.getOrder(TEST_WISHLIST_ORDER)!!
                    assertEquals(TEST_FILE_UPLOAD_NAME, order.fileName)
                    confirmAndDeleteFile(order.id)
                }
            }
        }
    }

    @Test
    fun `update order file invalid ID`() {
        withTestApplication({ module(testing = true, koinModules = listOf(testAuthModule)) }) {

            handleRequest(HttpMethod.Post, "/order/invalid-order-id/file") {
                val boundary = "***bbb***"
                addHeader(
                    HttpHeaders.ContentType,
                    ContentType.MultiPart.FormData.withParameter("boundary", boundary).toString()
                )
                setBody(boundary, listOf(uploadFile))
            }.apply {
                assertEquals(HttpStatusCode.NotAcceptable, response.status())
            }
        }
    }

    @Test
    fun `update basic settings success`() {
        withTestApplication({ module(testing = true, koinModules = listOf(testAuthModule)) }) {
            handleRequest(HttpMethod.Post, "/order/$TEST_WISHLIST_ORDER/basic") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded.toString())
                setBody(
                    listOf(
                        "size" to "100"
                    ).formUrlEncode()
                )
            }.apply {
                runBlocking {
                    assertEquals(100, orderRepository.getOrder(TEST_WISHLIST_ORDER)?.basicSettings?.size)
                }
            }
        }
    }

    @Test
    fun `update basic settings invalid ID`() {
        withTestApplication({ module(testing = true, koinModules = listOf(testAuthModule)) }) {
            handleRequest(HttpMethod.Post, "/order/invalid-order-id/basic") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded.toString())
                setBody(
                    listOf(
                        "size" to "100"
                    ).formUrlEncode()
                )
            }.apply {
                assertEquals(HttpStatusCode.NotAcceptable, response.status())
                runBlocking {
                    assertNotEquals(100, orderRepository.getOrder(TEST_WISHLIST_ORDER)?.basicSettings?.size)
                }
            }
        }
    }

    @Test
    fun `update advanced settings success`() {
        withTestApplication({ module(testing = true, koinModules = listOf(testAuthModule)) }) {
            handleRequest(HttpMethod.Post, "/order/$TEST_WISHLIST_ORDER/advanced") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded.toString())
                setBody(
                    listOf(
                        "weight" to "100"
                    ).formUrlEncode()
                )
            }.apply {
                runBlocking {
                    assertEquals(100, orderRepository.getOrder(TEST_WISHLIST_ORDER)?.advancedSettings?.weight)
                }
            }
        }
    }

    @Test
    fun `update advanced settings invalid ID`() {
        withTestApplication({ module(testing = true, koinModules = listOf(testAuthModule)) }) {
            handleRequest(HttpMethod.Post, "/order/invalid-order-id/advanced") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded.toString())
                setBody(
                    listOf(
                        "weight" to "100"
                    ).formUrlEncode()
                )
            }.apply {
                assertEquals(HttpStatusCode.NotAcceptable, response.status())
                runBlocking {
                    assertNotEquals(100, orderRepository.getOrder(TEST_WISHLIST_ORDER)?.advancedSettings?.weight)
                }
            }
        }
    }
}
