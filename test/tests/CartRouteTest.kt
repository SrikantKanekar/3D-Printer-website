package tests

import com.example.features.`object`.domain.ObjectStatus.NONE
import com.example.features.account.data.AccountRepository
import com.example.module
import data.TestConstants.TEST_CART_OBJECT1
import data.TestConstants.TEST_USER_EMAIL
import di.testModules
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class CartRouteTest : KoinTest {

    private val accountRepository by inject<AccountRepository>()

    @Test
    fun `get cart route test`() {
        withTestApplication({ module(testing = true, koinModules = testModules) }) {
            handleRequest(HttpMethod.Get, "/cart").apply {
                assertEquals(HttpStatusCode.Unauthorized, response.status())
            }
            runWithLoggedUser {
                handleRequest(HttpMethod.Get, "/cart").apply {
                    assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }

    @Test
    fun `remove from cart success`() {
        withTestApplication({ module(testing = true, koinModules = testModules) }) {
            runWithLoggedUser {
                handleRequest(HttpMethod.Post, "/cart/remove"){
                    addHeader(HttpHeaders.ContentType, formUrlEncoded)
                    setBody(listOf("id" to TEST_CART_OBJECT1).formUrlEncode())
                }.apply {
                    runBlocking {
                        val obj = accountRepository.getUser(TEST_USER_EMAIL).objects
                            .filter { it.status == NONE }
                            .find { it.id == TEST_CART_OBJECT1 }
                        assertNotNull(obj)
                        assertEquals("true", response.content)
                    }
                }
            }
        }
    }
}