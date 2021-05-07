package tests

import com.example.features.account.data.AccountRepository
import com.example.features.`object`.data.ObjectRepository
import com.example.features.myObjects.domain.ObjectsCookie
import com.example.module
import data.Constants.TEST_CREATED_OBJECT
import data.Constants.TEST_USER_EMAIL
import data.Constants.TEST_MY_OBJECT
import di.testModule
import io.ktor.http.*
import io.ktor.server.testing.*
import io.ktor.sessions.*
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.*

class MyObjectRouteTest : KoinTest {

    private val objectRepository by inject<ObjectRepository>()
    private val accountRepository by inject<AccountRepository>()

    @Test
    fun `get wishlist route test without login`() {
        withTestApplication({ module(testing = true, koinModules = listOf(testModule)) }) {
            handleRequest(HttpMethod.Get, "/wishlist").apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }
        }
    }

    @Test
    fun `get wishlist route test with login`() {
        withTestApplication({ module(testing = true, koinModules = listOf(testModule)) }) {
            runWithTestUser {
                handleRequest(HttpMethod.Get, "/wishlist").apply {
                    assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }

    @Test
    fun `delete wishlist without login success`() {
        withTestApplication({ module(testing = true, koinModules = listOf(testModule)) }) {
            cookiesSession {
                `create object without user login`(objectRepository)
                handleRequest(HttpMethod.Get, "/wishlist/$TEST_CREATED_OBJECT/delete").apply {
                    runBlocking {
                        val cookie = response.call.sessions.get<ObjectsCookie>()!!
                        assertNotNull(cookie.objects.find { it.id == TEST_CREATED_OBJECT })

                        val order = objectRepository.getUserObject(TEST_USER_EMAIL, TEST_CREATED_OBJECT)
                        assertNull(order)

                        assertEquals(HttpStatusCode.Found, response.status())
                    }
                }
            }
        }
    }

    @Test
    fun `delete wishlist with login success`() {
        withTestApplication({ module(testing = true, koinModules = listOf(testModule)) }) {
            runWithTestUser {
                `create object after user login`(accountRepository, objectRepository)
                handleRequest(HttpMethod.Get, "/wishlist/$TEST_CREATED_OBJECT/delete").apply {
                    runBlocking {
                        val wishlist = accountRepository.getUser(TEST_USER_EMAIL).objects.map { it.id }
                        assertFalse { wishlist.contains(TEST_CREATED_OBJECT) }

                        val order = objectRepository.getUserObject(TEST_USER_EMAIL, TEST_CREATED_OBJECT)
                        assertNull(order)

                        assertEquals(HttpStatusCode.Found, response.status())
                    }
                }
            }
        }
    }

    @Test
    fun `delete wishlist invalid ID failure without login`() {
        withTestApplication({ module(testing = true, koinModules = listOf(testModule)) }) {
            cookiesSession {
                `create object without user login`(objectRepository)
                handleRequest(HttpMethod.Get, "/wishlist/invalid-order-id/delete").apply {
                    runBlocking {
                        val cookie = response.call.sessions.get<ObjectsCookie>()!!
                        assertNotNull(cookie.objects.find { it.id == TEST_CREATED_OBJECT })

                        val order = objectRepository.getUserObject(TEST_USER_EMAIL, TEST_CREATED_OBJECT)
                        assertNotNull(order)

                        assertEquals(HttpStatusCode.NotAcceptable, response.status())
                    }
                }
                confirmAndDeleteFile(TEST_CREATED_OBJECT)
            }
        }
    }

    @Test
    fun `delete wishlist invalid ID failure with login`() {
        withTestApplication({ module(testing = true, koinModules = listOf(testModule)) }) {
            runWithTestUser {
                `create object after user login`(accountRepository, objectRepository)
                handleRequest(HttpMethod.Get, "/wishlist/invalid-order-id/delete").apply {
                    runBlocking {

                        val wishlist = accountRepository.getUser(TEST_USER_EMAIL).objects.map { it.id }
                        assertTrue { wishlist.contains(TEST_CREATED_OBJECT) }

                        val order = objectRepository.getUserObject(TEST_USER_EMAIL, TEST_CREATED_OBJECT)
                        assertNotNull(order)

                        assertEquals(HttpStatusCode.NotAcceptable, response.status())
                    }
                }
                confirmAndDeleteFile(TEST_CREATED_OBJECT)
            }
        }
    }

    @Test
    fun `add to cart without login`() {
        withTestApplication({ module(testing = true, koinModules = listOf(testModule)) }) {
            handleRequest(HttpMethod.Get, "/wishlist/$TEST_MY_OBJECT/cart").apply {
                assertEquals(HttpStatusCode.Found, response.status())
            }
        }
    }

    @Test
    fun `add to cart with login success`() {
        withTestApplication({ module(testing = true, koinModules = listOf(testModule)) }) {
            runWithTestUser {
                handleRequest(HttpMethod.Get, "/wishlist/$TEST_MY_OBJECT/cart").apply {
                    runBlocking {
                        //val user = accountRepository.getUser(TEST_USER_EMAIL)
                        //assertFalse { user.wishlist.contains(TEST_MY_OBJECT) }
                        //assertTrue { user.cartOrders.contains(TEST_MY_OBJECT) }

                        val order = objectRepository.getUserObject(TEST_USER_EMAIL, TEST_MY_OBJECT)
                        assertNotNull(order)

                        assertEquals(HttpStatusCode.Found, response.status())
                    }
                }
            }
        }
    }

    @Test
    fun `add to cart after login invalid ID`() {
        withTestApplication({ module(testing = true, koinModules = listOf(testModule)) }) {
            runWithTestUser {
                handleRequest(HttpMethod.Get, "/wishlist/invalid-order-id/cart").apply {
                    runBlocking {
                        //val user = accountRepository.getUser(TEST_USER_EMAIL)
                        //assertTrue { user.wishlist.contains(TEST_MY_OBJECT) }
                        //assertFalse { user.cartOrders.contains(TEST_MY_OBJECT) }

                        val order = objectRepository.getUserObject(TEST_USER_EMAIL, TEST_MY_OBJECT)
                        assertNotNull(order)

                        assertEquals(HttpStatusCode.NotAcceptable, response.status())
                    }
                }
            }
        }
    }
}