package tests.objectRouteTests

import com.example.features.objects.domain.ObjectsCookie
import data.TestConstants.TEST_CART_OBJECT1
import data.TestConstants.TEST_CREATED_OBJECT
import data.TestConstants.TEST_INVALID_ID
import data.TestConstants.TEST_SLICED_OBJECT
import data.TestConstants.TEST_USER_EMAIL
import fakeDataSource.TestRepository
import io.ktor.server.testing.*
import io.ktor.sessions.*
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject
import tests.`create object before user login`
import tests.handlePostRequest
import tests.runServer
import tests.runWithLoggedUser
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class ObjectDelete : KoinTest {

    @Test
    fun `should return false if invalid Id before login`() {
        runServer {
            handlePostRequest(
                "/object/delete",
                listOf("id" to TEST_INVALID_ID)
            ) {
                assertEquals("false", response.content)
            }
        }
    }

    @Test
    fun `should return false if invalid Id after login`() {
        runServer {
            runWithLoggedUser {
                handlePostRequest(
                    "/object/delete",
                    listOf("id" to TEST_INVALID_ID)
                ) {
                    assertEquals("false", response.content)
                }
            }
        }
    }

    @Test
    fun `should return true if object deleted before login`() {
        runServer {
            cookiesSession {
                `create object before user login`()
                handlePostRequest(
                    "/object/delete",
                    listOf("id" to TEST_CREATED_OBJECT)
                ) {
                    val cookie = response.call.sessions.get<ObjectsCookie>()!!
                    assertNull(cookie.objects.find { it.id == TEST_CREATED_OBJECT })
                    assertEquals("true", response.content)
                }
            }
        }
    }

    @Test
    fun `should return true if object deleted after login`() {
        runServer {
            runWithLoggedUser {
                handlePostRequest(
                    "/object/delete",
                    listOf("id" to TEST_SLICED_OBJECT)
                ) {
                    runBlocking {
                        val testRepository by inject<TestRepository>()
                        val obj = testRepository.getUserObject(TEST_USER_EMAIL, TEST_SLICED_OBJECT)
                        assertNull(obj)

                        assertEquals("true", response.content)
                    }
                }
            }
        }
    }

    @Test
    fun `should return false if cart object is deleted`() {
        runServer {
            runWithLoggedUser {
                handlePostRequest(
                    "/object/delete",
                    listOf("id" to TEST_CART_OBJECT1)
                ) {
                    runBlocking {
                        val testRepository by inject<TestRepository>()
                        val obj = testRepository.getUserObject(TEST_USER_EMAIL, TEST_CART_OBJECT1)
                        assertNotNull(obj)

                        assertEquals("false", response.content)
                    }
                }
            }
        }
    }
}