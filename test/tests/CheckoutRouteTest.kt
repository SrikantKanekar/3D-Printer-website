package tests

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

class CheckoutRouteTest : KoinTest {

    private val accountRepository by inject<AccountRepository>()
    private val myObjectsRepository by inject<MyObjectsRepository>()
    private val cartRepository by inject<CartRepository>()

    @Test
    fun `access checkout without login`() {
        withTestApplication({ module(testing = true, koinModules = listOf(testModule)) }) {
            handleRequest(HttpMethod.Get, "/checkout").apply {
                assertEquals(HttpStatusCode.Unauthorized, response.status())
            }
        }
    }

    @Test
    fun `access checkout after login`() {
        withTestApplication({ module(testing = true, koinModules = listOf(testModule)) }) {
            runWithTestUser {
                handleRequest(HttpMethod.Get, "/checkout").apply {
                    assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }

    @Test
    fun `remove from checkout success`() {
        withTestApplication({ module(testing = true, koinModules = listOf(testModule)) }) {
            runWithTestUser {
                handleRequest(HttpMethod.Get, "/checkout/$TEST_CART_OBJECT/remove").apply {
                    runBlocking {

                        // change this
                        val wishlistOrders = myObjectsRepository.getMyObjects(TEST_USER_EMAIL).map { it.id }
                        assertTrue { wishlistOrders.contains(TEST_CART_OBJECT) }

                        // change this
                        val cartOrders = cartRepository.getUserCartOrders(TEST_USER_EMAIL)
                        cartOrders.forEach {
                            assertFalse { it.id == TEST_CART_OBJECT }
                        }

//                        val user = accountRepository.getUser(TEST_USER_EMAIL)!!
//                        assertTrue { user.wishlist.contains(TEST_CART_OBJECT) }
//                        assertFalse { user.cartOrders.contains(TEST_CART_OBJECT) }

                        assertEquals(HttpStatusCode.Found, response.status())
                    }
                }
            }
        }
    }

    @Test
    fun `remove from checkout invalid ID`() {
        withTestApplication({ module(testing = true, koinModules = listOf(testModule)) }) {
            runWithTestUser {
                handleRequest(HttpMethod.Get, "/checkout/invalid-order-id/remove").apply {
                    runBlocking {

                        // change this
                        val wishlistOrders = myObjectsRepository.getMyObjects(TEST_USER_EMAIL).map { it.id }
                        assertFalse { wishlistOrders.contains(TEST_CART_OBJECT) }

                        // change this
                        val cartOrders = cartRepository.getUserCartOrders(TEST_USER_EMAIL)

//                        val user = accountRepository.getUser(TEST_USER_EMAIL)
//                        assertFalse { user.wishlist.contains(TEST_CART_OBJECT) }
//                        assertTrue { user.cartOrders.contains(TEST_CART_OBJECT) }

                        assertEquals(HttpStatusCode.NotAcceptable, response.status())
                    }
                }
            }
        }
    }

    @Test
    fun `proceed to pay success`() {
        withTestApplication({ module(testing = true, koinModules = listOf(testModule)) }) {
            runWithTestUser {
                handleRequest(HttpMethod.Post, "/checkout/pay") {
                    addHeader(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded.toString())
                    setBody(
                        listOf(
                            "city" to "city",
                            "state" to "state",
                            "country" to "country"
                        ).formUrlEncode()
                    )
                }.apply {
                    runBlocking {
                        val user = accountRepository.getUser(TEST_USER_EMAIL)
                        assertTrue { user.address.city == "city" }
                        assertTrue { user.address.state == "state" }
                        assertTrue { user.address.country == "country" }

                        assertEquals(HttpStatusCode.OK, response.status())
                    }
                }
            }
        }
    }
}