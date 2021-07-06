package tests.objectRouteTests

import com.example.features.`object`.domain.ObjectStatus.CART
import data.TestConstants.TEST_INVALID_ID
import data.TestConstants.TEST_SLICED_OBJECT
import data.TestConstants.TEST_UNSLICED_OBJECT
import data.TestConstants.TEST_USER_EMAIL
import fakeDataSource.TestRepository
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject
import tests.handlePostRequest
import tests.runServer
import tests.runWithLoggedUser
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class ObjectCart : KoinTest {

    @Test
    fun `should redirect if user not logged`() {
        runServer {
            handlePostRequest(
                "/object/add-to-cart",
                listOf("id" to TEST_SLICED_OBJECT)
            ) {
                assertNotEquals("true", response.content)
            }
        }
    }

    @Test
    fun `should return false for invalid object ID`() {
        runServer {
            runWithLoggedUser {
                handlePostRequest(
                    "/object/add-to-cart",
                    listOf("id" to TEST_INVALID_ID)
                ) {
                    runBlocking {
                        val testRepository by inject<TestRepository>()
                        val obj = testRepository.getUser(TEST_USER_EMAIL)
                            .objects
                            .filter { it.status == CART }
                            .find { it.id == TEST_INVALID_ID }
                        assertNull(obj)

                        assertEquals("false", response.content)
                    }
                }
            }
        }
    }

    @Test
    fun `should return false for unsliced object`() {
        runServer {
            runWithLoggedUser {
                handlePostRequest(
                    "/object/add-to-cart",
                    listOf("id" to TEST_UNSLICED_OBJECT)
                ) {
                    runBlocking {
                        val testRepository by inject<TestRepository>()
                        val obj = testRepository.getUser(TEST_USER_EMAIL)
                            .objects
                            .filter { it.status == CART }
                            .find { it.id == TEST_UNSLICED_OBJECT }
                        assertNull(obj)

                        assertEquals("false", response.content)
                    }
                }
            }
        }
    }

    @Test
    fun `should return true for sliced object`() {
        runServer {
            runWithLoggedUser {
                handlePostRequest(
                    "/object/add-to-cart",
                    listOf("id" to TEST_SLICED_OBJECT)
                ) {
                    runBlocking {
                        val testRepository by inject<TestRepository>()
                        val obj = testRepository.getUser(TEST_USER_EMAIL)
                            .objects
                            .filter { it.status == CART }
                            .find { it.id == TEST_SLICED_OBJECT }
                        assertNotNull(obj)

                        assertEquals("true", response.content)
                    }
                }
            }
        }
    }
}