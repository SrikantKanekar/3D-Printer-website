package tests.objectsRoute

import com.example.features.`object`.domain.ObjectStatus.CART
import data.TestConstants.TEST_INVALID_ID
import data.TestConstants.TEST_SLICED_OBJECT
import data.TestConstants.TEST_UNSLICED_OBJECT
import data.TestConstants.TEST_USER_EMAIL
import fakeDataSource.TestRepository
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject
import tests.handlePostRequest
import tests.runTest
import tests.runWithLoggedUser
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class ObjectsCartTest : KoinTest {

    @Test
    fun `should redirect if user not logged`() {
        runTest {
            handlePostRequest(
                "/objects/add-to-cart",
                listOf("id" to TEST_SLICED_OBJECT)
            ) {
                assertEquals(HttpStatusCode.OK, response.status())
            }
        }
    }

    @Test
    fun `should return false for invalid object ID`() {
        runTest {
            runWithLoggedUser {
                handlePostRequest(
                    "/objects/add-to-cart",
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
    fun `should return false for object without slicing`() {
        runTest {
            runWithLoggedUser {
                handlePostRequest(
                    "/objects/add-to-cart",
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
    fun `should return true for object after slicing`() {
        runTest {
            runWithLoggedUser {
                handlePostRequest(
                    "/objects/add-to-cart",
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