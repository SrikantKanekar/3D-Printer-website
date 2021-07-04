package tests

import com.example.features.`object`.data.ObjectRepository
import com.example.features.`object`.domain.ObjectStatus.CART
import com.example.features.account.data.AccountRepository
import com.example.features.objects.domain.ObjectsCookie
import com.example.module
import data.TestConstants.TEST_CREATED_OBJECT
import data.TestConstants.TEST_USER_EMAIL
import data.TestConstants.TEST_USER_OBJECT
import di.testModules
import io.ktor.http.*
import io.ktor.server.testing.*
import io.ktor.sessions.*
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class ObjectsRouteTest : KoinTest {

    private val objectRepository by inject<ObjectRepository>()
    private val accountRepository by inject<AccountRepository>()

    @Test
    fun `get user objects route test`() {
        withTestApplication({ module(testing = true, koinModules = testModules) }) {
            handleRequest(HttpMethod.Get, "/objects").apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }
            runWithTestUser {
                handleRequest(HttpMethod.Get, "/objects").apply {
                    assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }

    @Test
    fun `delete user object before login success`() {
        withTestApplication({ module(testing = true, koinModules = testModules) }) {
            cookiesSession {
                `create object before user login`()
                handleRequest(HttpMethod.Get, "/objects/$TEST_CREATED_OBJECT/delete").apply {
                    runBlocking {
                        val cookie = response.call.sessions.get<ObjectsCookie>()!!
                        assertNull(cookie.objects.find { it.id == TEST_CREATED_OBJECT })
                        assertEquals(HttpStatusCode.Found, response.status())
                    }
                }
            }
        }
    }

    @Test
    fun `delete user object after login success`() {
        withTestApplication({ module(testing = true, koinModules = testModules) }) {
            runWithTestUser {
                `create object after user login`(accountRepository)
                handleRequest(HttpMethod.Get, "/objects/$TEST_CREATED_OBJECT/delete").apply {
                    runBlocking {
                        val obj = objectRepository.getUserObject(TEST_USER_EMAIL, TEST_CREATED_OBJECT)
                        assertNull(obj)
                        assertEquals(HttpStatusCode.Found, response.status())
                    }
                }
            }
        }
    }

    @Test
    fun `delete user object before login invalid ID`() {
        withTestApplication({ module(testing = true, koinModules = testModules) }) {
            handleRequest(HttpMethod.Get, "/objects/invalid-object-id/delete").apply {
                assertEquals(HttpStatusCode.NotAcceptable, response.status())
            }
        }
    }

    @Test
    fun `delete user object after login invalid ID`() {
        withTestApplication({ module(testing = true, koinModules = testModules) }) {
            runWithTestUser {
                handleRequest(HttpMethod.Get, "/objects/invalid-object-id/delete").apply {
                    assertEquals(HttpStatusCode.NotAcceptable, response.status())
                }
            }
        }
    }

    @Test
    fun `add to cart before login`() {
        withTestApplication({ module(testing = true, koinModules = testModules) }) {
            handleRequest(HttpMethod.Post, "/objects/add-to-cart") {
                addHeader(HttpHeaders.ContentType, formUrlEncoded)
                setBody(listOf("id" to TEST_USER_OBJECT).formUrlEncode())
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }
        }
    }

    @Test
    fun `add to cart after login success`() {
        withTestApplication({ module(testing = true, koinModules = testModules) }) {
            runWithTestUser {
                handleRequest(HttpMethod.Post, "/objects/add-to-cart") {
                    addHeader(HttpHeaders.ContentType, formUrlEncoded)
                    setBody(listOf("id" to TEST_USER_OBJECT).formUrlEncode())
                }.apply {
                    runBlocking {
                        val obj = accountRepository.getUser(TEST_USER_EMAIL).objects
                            .filter { it.status == CART }
                            .find { it.id == TEST_USER_OBJECT }
                        assertNotNull(obj)
                    }
                }
            }
        }
    }

    @Test
    fun `add to cart after login invalid ID`() {
        withTestApplication({ module(testing = true, koinModules = testModules) }) {
            runWithTestUser {
                handleRequest(HttpMethod.Post, "/objects/add-to-cart") {
                    addHeader(HttpHeaders.ContentType, formUrlEncoded)
                    setBody(listOf("id" to TEST_USER_OBJECT).formUrlEncode())
                }.apply {
                    runBlocking {
                        val obj = objectRepository.getUserObject(TEST_USER_EMAIL, "invalid-object-id")
                        assertNull(obj)
                    }
                }
            }
        }
    }
}