package tests

import com.example.features.account.data.AccountRepository
import com.example.features.`object`.data.ObjectRepository
import com.example.module
import data.Constants.TEST_CREATED_OBJECT
import data.Constants.TEST_FILE_UPLOAD_NAME
import data.Constants.TEST_USER_EMAIL
import data.Constants.TEST_MY_OBJECT
import di.testModule
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class ObjectRouteTest : KoinTest {

    private val orderRepository by inject<ObjectRepository>()
    private val accountRepository by inject<AccountRepository>()

    @Test
    fun `get order route test with or without login`() {
        withTestApplication({ module(testing = true, koinModules = listOf(testModule)) }) {
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
        withTestApplication({ module(testing = true, koinModules = listOf(testModule)) }) {
            `create object without user login`(orderRepository)
            confirmAndDeleteFile(TEST_CREATED_OBJECT)
        }
    }

    @Test
    fun `create order after login success`() {
        withTestApplication({ module(testing = true, koinModules = listOf(testModule)) }) {
            runWithTestUser {
                `create object after user login`(accountRepository, orderRepository)
                confirmAndDeleteFile(TEST_CREATED_OBJECT)
            }
        }
    }

    @Test
    fun `wishlist cookie sync test success`() {
        withTestApplication({ module(testing = true, koinModules = listOf(testModule)) }) {
            cookiesSession {
                `create object without user login`(orderRepository)
                testUserLogin()

                runBlocking {

                    val order = orderRepository.getUserObject(TEST_USER_EMAIL, TEST_CREATED_OBJECT)!!
                    assertEquals(TEST_FILE_UPLOAD_NAME, order.fileName)

                    confirmAndDeleteFile(order.id)
                }
            }
        }
    }

    @Test
    fun `get update order route success`() {
        withTestApplication({ module(testing = true, koinModules = listOf(testModule)) }) {
            handleRequest(HttpMethod.Get, "/order/$TEST_MY_OBJECT").apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }
        }
    }

    @Test
    fun `get update order route failure`() {
        withTestApplication({ module(testing = true, koinModules = listOf(testModule)) }) {
            handleRequest(HttpMethod.Get, "/order/invalid-order-id").apply {
                assertEquals(HttpStatusCode.NotAcceptable, response.status())
            }
        }
    }

    @Test
    fun `update order file success`() {
        withTestApplication({ module(testing = true, koinModules = listOf(testModule)) }) {

            // Temporarily add empty test file before test
            insertNewFileAndConfirm(TEST_MY_OBJECT)

            handleRequest(HttpMethod.Post, "/order/$TEST_MY_OBJECT/file") {
                val boundary = "***bbb***"
                addHeader(
                    HttpHeaders.ContentType,
                    ContentType.MultiPart.FormData.withParameter("boundary", boundary).toString()
                )
                setBody(boundary, listOf(testUploadFile))
            }.apply {
                runBlocking {
                    val order = orderRepository.getUserObject(TEST_USER_EMAIL, TEST_MY_OBJECT)!!
                    assertEquals(TEST_FILE_UPLOAD_NAME, order.fileName)
                    confirmAndDeleteFile(order.id)
                }
            }
        }
    }

    @Test
    fun `update order file invalid ID`() {
        withTestApplication({ module(testing = true, koinModules = listOf(testModule)) }) {

            handleRequest(HttpMethod.Post, "/order/invalid-order-id/file") {
                val boundary = "***bbb***"
                addHeader(
                    HttpHeaders.ContentType,
                    ContentType.MultiPart.FormData.withParameter("boundary", boundary).toString()
                )
                setBody(boundary, listOf(testUploadFile))
            }.apply {
                assertEquals(HttpStatusCode.NotAcceptable, response.status())
            }
        }
    }

    @Test
    fun `update basic settings success`() {
        withTestApplication({ module(testing = true, koinModules = listOf(testModule)) }) {
            handleRequest(HttpMethod.Post, "/order/$TEST_MY_OBJECT/basic") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded.toString())
                setBody(
                    listOf(
                        "size" to "100"
                    ).formUrlEncode()
                )
            }.apply {
                runBlocking {
                    assertEquals(100, orderRepository.getUserObject(TEST_USER_EMAIL, TEST_MY_OBJECT)?.basicSettings?.size)
                }
            }
        }
    }

    @Test
    fun `update basic settings invalid ID`() {
        withTestApplication({ module(testing = true, koinModules = listOf(testModule)) }) {
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
                    assertNotEquals(100, orderRepository.getUserObject(TEST_USER_EMAIL, TEST_MY_OBJECT)?.basicSettings?.size)
                }
            }
        }
    }

    @Test
    fun `update advanced settings success`() {
        withTestApplication({ module(testing = true, koinModules = listOf(testModule)) }) {
            handleRequest(HttpMethod.Post, "/order/$TEST_MY_OBJECT/advanced") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded.toString())
                setBody(
                    listOf(
                        "weight" to "100"
                    ).formUrlEncode()
                )
            }.apply {
                runBlocking {
                    assertEquals(100, orderRepository.getUserObject(TEST_USER_EMAIL, TEST_MY_OBJECT)?.advancedSettings?.weight)
                }
            }
        }
    }

    @Test
    fun `update advanced settings invalid ID`() {
        withTestApplication({ module(testing = true, koinModules = listOf(testModule)) }) {
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
                    assertNotEquals(100, orderRepository.getUserObject(TEST_USER_EMAIL, TEST_MY_OBJECT)?.advancedSettings?.weight)
                }
            }
        }
    }
}
