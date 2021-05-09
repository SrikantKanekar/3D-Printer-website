package tests

import com.example.features.`object`.domain.ObjectStatus.NONE
import com.example.features.`object`.domain.ObjectStatus.TRACKING
import com.example.features.account.data.AccountRepository
import com.example.features.admin.data.AdminRepository
import com.example.features.checkout.domain.OrderStatus.*
import com.example.module
import data.Constants.TEST_CREATED_ORDER
import data.Constants.TEST_USER_EMAIL
import di.testModule
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class CheckoutRouteTest : KoinTest {

    private val accountRepository by inject<AccountRepository>()
    private val adminRepository by inject<AdminRepository>()

    @Test
    fun `get checkout route test`() {
        withTestApplication({ module(testing = true, koinModules = listOf(testModule)) }) {
            handleRequest(HttpMethod.Get, "/checkout").apply {
                assertEquals(HttpStatusCode.Unauthorized, response.status())
            }
            runWithTestUser {
                handleRequest(HttpMethod.Get, "/checkout").apply {
                    assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }

//    @Test
//    fun `remove object from checkout success`() {
//        withTestApplication({ module(testing = true, koinModules = listOf(testModule)) }) {
//            runWithTestUser {
//                handleRequest(HttpMethod.Get, "/checkout/$TEST_CART_OBJECT1/remove").apply {
//                    runBlocking {
//                        val obj = accountRepository.getUser(TEST_USER_EMAIL).objects
//                            .filter { it.status == NONE }
//                            .find { it.id == TEST_CART_OBJECT1 }
//                        assertNotNull(obj)
//                        assertEquals(HttpStatusCode.Found, response.status())
//                    }
//                }
//            }
//        }
//    }

    @Test
    fun `remove from checkout invalid ID`() {
        withTestApplication({ module(testing = true, koinModules = listOf(testModule)) }) {
            runWithTestUser {
                handleRequest(HttpMethod.Get, "/checkout/invalid-object-id/remove").apply {
                    assertEquals(HttpStatusCode.NotAcceptable, response.status())
                }
            }
        }
    }

    @Test
    fun `proceed to pay success`() {
        withTestApplication({ module(testing = true, koinModules = listOf(testModule)) }) {
            runWithTestUser {
                handleRequest(HttpMethod.Post, "/checkout/pay") {
                    addHeader(HttpHeaders.ContentType, formUrlEncoded)
                    setBody(
                        listOf(
                            "city" to "city",
                            "state" to "state",
                            "country" to "country"
                        ).formUrlEncode()
                    )
                }.apply {
                    runBlocking {
                        val order = adminRepository.getActiveOrder(TEST_CREATED_ORDER)!!
                        assertEquals(PLACED, order.status)
                        assertEquals(TEST_USER_EMAIL, order.userEmail)
                        assertTrue { order.objects.size > 0 }
                        val objs = order.objects

                        val user = accountRepository.getUser(TEST_USER_EMAIL)
                        assertTrue { user.address.city == "city" }
                        assertTrue { user.address.state == "state" }
                        assertTrue { user.address.country == "country" }
                        assertTrue { user.objects.filter { it.status == TRACKING }.containsAll(objs) }

                        assertEquals(HttpStatusCode.OK, response.status())
                    }
                }
            }
        }
    }
}