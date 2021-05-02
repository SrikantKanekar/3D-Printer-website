package features.wishlist

import com.example.features.account.data.AccountRepository
import com.example.features.order.data.OrderRepository
import com.example.features.wishlist.domain.WishlistCookie
import com.example.module
import data.Constants.TEST_CREATED_ORDER
import data.Constants.TEST_USER_EMAIL
import data.Constants.TEST_WISHLIST_ORDER
import di.testAuthModule
import features.auth.runWithTestUser
import features.order.`create order after login`
import features.order.`create order without login`
import features.order.confirmAndDeleteFile
import io.ktor.http.*
import io.ktor.server.testing.*
import io.ktor.sessions.*
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.*

class WishlistRouteTest : KoinTest {

    private val orderRepository by inject<OrderRepository>()
    private val accountRepository by inject<AccountRepository>()

    @Test
    fun `get wishlist route test without login`() {
        withTestApplication({ module(testing = true, koinModules = listOf(testAuthModule)) }) {
            handleRequest(HttpMethod.Get, "/wishlist").apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }
        }
    }

    @Test
    fun `get wishlist route test with login`() {
        withTestApplication({ module(testing = true, koinModules = listOf(testAuthModule)) }) {
            runWithTestUser {
                handleRequest(HttpMethod.Get, "/wishlist").apply {
                    assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }

    @Test
    fun `delete wishlist without login success`() {
        withTestApplication({ module(testing = true, koinModules = listOf(testAuthModule)) }) {
            cookiesSession {
                `create order without login`(orderRepository)
                handleRequest(HttpMethod.Get, "/wishlist/$TEST_CREATED_ORDER/delete").apply {
                    runBlocking {
                        val cookie = response.call.sessions.get<WishlistCookie>()!!
                        assertFalse { cookie.orders.contains(TEST_CREATED_ORDER) }

                        val order = orderRepository.getOrder(TEST_CREATED_ORDER)
                        assertNull(order)

                        assertEquals(HttpStatusCode.Found, response.status())
                    }
                }
            }
        }
    }

    @Test
    fun `delete wishlist with login success`() {
        withTestApplication({ module(testing = true, koinModules = listOf(testAuthModule)) }) {
            runWithTestUser {
                `create order after login`(accountRepository, orderRepository)
                handleRequest(HttpMethod.Get, "/wishlist/$TEST_CREATED_ORDER/delete").apply {
                    runBlocking {
                        val wishlist = accountRepository.getUser(TEST_USER_EMAIL)!!.wishlist
                        assertFalse { wishlist.contains(TEST_CREATED_ORDER) }

                        val order = orderRepository.getOrder(TEST_CREATED_ORDER)
                        assertNull(order)

                        assertEquals(HttpStatusCode.Found, response.status())
                    }
                }
            }
        }
    }

    @Test
    fun `delete wishlist invalid ID failure without login`() {
        withTestApplication({ module(testing = true, koinModules = listOf(testAuthModule)) }) {
            cookiesSession {
                `create order without login`(orderRepository)
                handleRequest(HttpMethod.Get, "/wishlist/invalid-order-id/delete").apply {
                    runBlocking {
                        val cookie = response.call.sessions.get<WishlistCookie>()!!
                        assertTrue { cookie.orders.contains(TEST_CREATED_ORDER) }

                        val order = orderRepository.getOrder(TEST_CREATED_ORDER)
                        assertNotNull(order)

                        assertEquals(HttpStatusCode.NotAcceptable, response.status())
                    }
                }
                confirmAndDeleteFile(TEST_CREATED_ORDER)
            }
        }
    }

    @Test
    fun `delete wishlist invalid ID failure with login`() {
        withTestApplication({ module(testing = true, koinModules = listOf(testAuthModule)) }) {
            runWithTestUser {
                `create order after login`(accountRepository, orderRepository)
                handleRequest(HttpMethod.Get, "/wishlist/invalid-order-id/delete").apply {
                    runBlocking {

                        val wishlist = accountRepository.getUser(TEST_USER_EMAIL)!!.wishlist
                        assertTrue { wishlist.contains(TEST_CREATED_ORDER) }

                        val order = orderRepository.getOrder(TEST_CREATED_ORDER)
                        assertNotNull(order)

                        assertEquals(HttpStatusCode.NotAcceptable, response.status())
                    }
                }
                confirmAndDeleteFile(TEST_CREATED_ORDER)
            }
        }
    }

    @Test
    fun `add to cart without login`() {
        withTestApplication({ module(testing = true, koinModules = listOf(testAuthModule)) }) {
            handleRequest(HttpMethod.Get, "/wishlist/$TEST_WISHLIST_ORDER/cart").apply {
                assertEquals(HttpStatusCode.Found, response.status())
            }
        }
    }

    @Test
    fun `add to cart with login success`() {
        withTestApplication({ module(testing = true, koinModules = listOf(testAuthModule)) }) {
            runWithTestUser {
                handleRequest(HttpMethod.Get, "/wishlist/$TEST_WISHLIST_ORDER/cart").apply {
                    runBlocking {
                        val user = accountRepository.getUser(TEST_USER_EMAIL)!!
                        assertFalse { user.wishlist.contains(TEST_WISHLIST_ORDER) }
                        assertTrue { user.cartOrders.contains(TEST_WISHLIST_ORDER) }

                        val order = orderRepository.getOrder(TEST_WISHLIST_ORDER)
                        assertNotNull(order)

                        assertEquals(HttpStatusCode.Found, response.status())
                    }
                }
            }
        }
    }

    @Test
    fun `add to cart after login invalid ID`() {
        withTestApplication({ module(testing = true, koinModules = listOf(testAuthModule)) }) {
            runWithTestUser {
                handleRequest(HttpMethod.Get, "/wishlist/invalid-order-id/cart").apply {
                    runBlocking {
                        val user = accountRepository.getUser(TEST_USER_EMAIL)!!
                        assertTrue { user.wishlist.contains(TEST_WISHLIST_ORDER) }
                        assertFalse { user.cartOrders.contains(TEST_WISHLIST_ORDER) }

                        val order = orderRepository.getOrder(TEST_WISHLIST_ORDER)
                        assertNotNull(order)

                        assertEquals(HttpStatusCode.NotAcceptable, response.status())
                    }
                }
            }
        }
    }
}