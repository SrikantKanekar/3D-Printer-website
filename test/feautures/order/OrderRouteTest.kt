package feautures.order

import com.example.feautures.account.data.AccountRepository
import com.example.feautures.account.domain.CartCookie
import com.example.feautures.order.data.OrderRepository
import com.example.module
import data.Constants
import data.Constants.TEST_CART_ORDER
import data.Constants.TEST_FILE_UPLOAD_NAME
import data.Constants.TEST_USER_EMAIL
import di.testAuthModule
import feautures.auth.runWithTestUser
import feautures.auth.testUserLogin
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.server.testing.*
import io.ktor.sessions.*
import io.ktor.utils.io.streams.*
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class OrderRouteTest : KoinTest {

    private val orderRepository by inject<OrderRepository>()
    private val accountRepository by inject<AccountRepository>()

    private val uploadFile = PartData.FileItem(
        { byteArrayOf(1, 2, 3).inputStream().asInput() },
        { },
        headersOf(
            HttpHeaders.ContentDisposition,
            ContentDisposition.Inline
                .withParameter(ContentDisposition.Parameters.FileName, TEST_FILE_UPLOAD_NAME)
                .toString()
        )
    )

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
            handleRequest(HttpMethod.Post, "/order/create") {
                val boundary = "***bbb***"
                addHeader(
                    HttpHeaders.ContentType,
                    ContentType.MultiPart.FormData.withParameter("boundary", boundary).toString()
                )
                setBody(boundary, listOf(uploadFile))
            }.apply {
                runBlocking {
                    val cartCookie = response.call.sessions.get<CartCookie>()
                    val orderId = cartCookie!!.orders[0]
                    val order = orderRepository.getOrder(orderId)!!
                    assertEquals(TEST_FILE_UPLOAD_NAME, order.fileName)
                    confirmAndDeleteFile(order.id)
                }
            }
        }
    }

    @Test
    fun `create order after login success`() {
        withTestApplication({ module(testing = true, koinModules = listOf(testAuthModule)) }) {
            runWithTestUser {
                handleRequest(HttpMethod.Post, "/order/create") {
                    val boundary = "***bbb***"
                    addHeader(
                        HttpHeaders.ContentType,
                        ContentType.MultiPart.FormData.withParameter("boundary", boundary).toString()
                    )
                    setBody(boundary, listOf(uploadFile))
                }.apply {
                    runBlocking {
                        val testUserCart = accountRepository.getUser(TEST_USER_EMAIL)!!.cartOrders
                        val orderId = testUserCart[0]
                        val order = orderRepository.getOrder(orderId)!!
                        assertEquals(TEST_FILE_UPLOAD_NAME, order.fileName)
                        confirmAndDeleteFile(order.id)
                    }
                }
            }
        }
    }

    @Test
    fun `cart cookie sync test success`() {
        withTestApplication({ module(testing = true, koinModules = listOf(testAuthModule)) }) {
            cookiesSession {
                handleRequest(HttpMethod.Post, "/order/create") {
                    val boundary = "***bbb***"
                    addHeader(
                        HttpHeaders.ContentType,
                        ContentType.MultiPart.FormData.withParameter("boundary", boundary).toString()
                    )
                    setBody(boundary, listOf(uploadFile))
                }.apply {
                    runBlocking {
                        val cartCookie = response.call.sessions.get<CartCookie>()
                        val cookieOrderId = cartCookie!!.orders[0]
                        val cookieOrder = orderRepository.getOrder(cookieOrderId)!!
                        assertEquals(TEST_FILE_UPLOAD_NAME, cookieOrder.fileName)

                        testUserLogin()
                        val testUserCart = accountRepository.getUser(TEST_USER_EMAIL)!!.cartOrders
                        val orderId = testUserCart[0]
                        val order = orderRepository.getOrder(orderId)!!
                        assertEquals(TEST_FILE_UPLOAD_NAME, order.fileName)
                        confirmAndDeleteFile(order.id)
                    }
                }
            }
        }
    }

    @Test
    fun `get update order route success`() {
        withTestApplication({ module(testing = true, koinModules = listOf(testAuthModule)) }) {
            handleRequest(HttpMethod.Get, "/order/$TEST_CART_ORDER").apply {
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
            createFileAndConfirm(TEST_CART_ORDER)

            handleRequest(HttpMethod.Post, "/order/$TEST_CART_ORDER/file") {
                val boundary = "***bbb***"
                addHeader(
                    HttpHeaders.ContentType,
                    ContentType.MultiPart.FormData.withParameter("boundary", boundary).toString()
                )
                setBody(boundary, listOf(uploadFile))
            }.apply {
                runBlocking {
                    val order = orderRepository.getOrder(TEST_CART_ORDER)!!
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
            handleRequest(HttpMethod.Post, "/order/$TEST_CART_ORDER/basic") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded.toString())
                setBody(
                    listOf(
                        "size" to "100"
                    ).formUrlEncode()
                )
            }.apply {
                runBlocking {
                    assertEquals(100, orderRepository.getOrder(TEST_CART_ORDER)?.basicSettings?.size)
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
                    assertNotEquals(100, orderRepository.getOrder(TEST_CART_ORDER)?.basicSettings?.size)
                }
            }
        }
    }

    @Test
    fun `update advanced settings success`() {
        withTestApplication({ module(testing = true, koinModules = listOf(testAuthModule)) }) {
            handleRequest(HttpMethod.Post, "/order/$TEST_CART_ORDER/advanced") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded.toString())
                setBody(
                    listOf(
                        "weight" to "100"
                    ).formUrlEncode()
                )
            }.apply {
                runBlocking {
                    assertEquals(100, orderRepository.getOrder(TEST_CART_ORDER)?.advancedSettings?.weight)
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
                    assertNotEquals(100, orderRepository.getOrder(TEST_CART_ORDER)?.advancedSettings?.weight)
                }
            }
        }
    }

    private fun createFileAndConfirm(id: String) {
        val file = File("uploads/$id")
        file.createNewFile()
        assertTrue { file.exists() }
    }

    private fun confirmAndDeleteFile(id: String) {
        val file = File("uploads/$id")
        assertTrue { file.exists() }
        file.delete()
        assertFalse { file.exists() }
    }
}
