package tests

import com.example.features.`object`.domain.ObjectStatus.*
import com.example.features.account.data.AccountRepository
import com.example.features.cart.data.CartRepository
import com.example.features.myObjects.data.MyObjectsRepository
import com.example.module
import data.Constants.TEST_CART_OBJECT
import data.Constants.TEST_USER_EMAIL
import di.testModule
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CartRouteTest : KoinTest {

    private val accountRepository by inject<AccountRepository>()
    private val cartRepository by inject<CartRepository>()

    @Test
    fun `access cart without login`() {
        withTestApplication({ module(testing = true, koinModules = listOf(testModule)) }) {
            handleRequest(HttpMethod.Get, "/cart").apply {
                assertEquals(HttpStatusCode.Unauthorized, response.status())
            }
        }
    }

    @Test
    fun `access cart after login`() {
        withTestApplication({ module(testing = true, koinModules = listOf(testModule)) }) {
            runWithTestUser {
                handleRequest(HttpMethod.Get, "/cart").apply {
                    assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }

    @Test
    fun `remove from cart success`() {
        withTestApplication({ module(testing = true, koinModules = listOf(testModule)) }) {
            runWithTestUser {
                handleRequest(HttpMethod.Get, "/cart/$TEST_CART_OBJECT/remove").apply {
                    runBlocking {

                        val cartObjects = cartRepository.getUserCartOrders(TEST_USER_EMAIL)
                        assertEquals(NONE, cartObjects.find { it.id == TEST_CART_OBJECT }?.status)

//                        val user = accountRepository.getUser(TEST_USER_EMAIL)
//                        assertTrue { user.wishlist.contains(TEST_CART_OBJECT) }
//                        assertFalse { user.cartOrders.contains(TEST_CART_OBJECT) }

                        assertEquals(HttpStatusCode.Found, response.status())
                    }
                }
            }
        }
    }

    @Test
    fun `remove from cart invalid ID`() {
        withTestApplication({ module(testing = true, koinModules = listOf(testModule)) }) {
            runWithTestUser {
                handleRequest(HttpMethod.Get, "/cart/invalid-order-id/remove").apply {
                    runBlocking {

                        val cartObjects = cartRepository.getUserCartOrders(TEST_USER_EMAIL)
                        assertEquals(CART, cartObjects.find { it.id == TEST_CART_OBJECT }?.status)

//                        val user = accountRepository.getUser(TEST_USER_EMAIL)
//                        assertFalse { user.wishlist.contains(TEST_CART_OBJECT) }
//                        assertTrue { user.cartOrders.contains(TEST_CART_OBJECT) }

                        assertEquals(HttpStatusCode.NotAcceptable, response.status())
                    }
                }
            }
        }
    }
}