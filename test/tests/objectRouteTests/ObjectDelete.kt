package tests.objectRouteTests

import data.TestConstants.TEST_CART_OBJECT
import data.TestConstants.TEST_INVALID_ID
import data.TestConstants.TEST_SLICED_OBJECT
import data.TestConstants.TEST_USER_EMAIL
import fakeDataSource.TestRepository
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject
import tests.handleDeleteRequest
import tests.runServer
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class ObjectDelete : KoinTest {

    @Test
    fun `should return NotFound if invalid Id before login`() {
        runServer {
            handleDeleteRequest(
                uri = "/objects/$TEST_INVALID_ID"
            ) {
                assertEquals(HttpStatusCode.NotFound, response.status())
            }
        }
    }

    @Test
    fun `should return NotFound if invalid Id after login`() {
        runServer {
            handleDeleteRequest(
                uri = "/objects/$TEST_INVALID_ID",
                logged = true
            ) {
                assertEquals(HttpStatusCode.NotFound, response.status())
            }
        }
    }

    @Test
    fun `should return NotFound if cart object is deleted`() {
        runServer {
            handleDeleteRequest(
                "/objects/$TEST_CART_OBJECT",
                logged = true
            ) {
                runBlocking {
                    val testRepository by inject<TestRepository>()
                    val obj = testRepository.getUserObjectById(TEST_USER_EMAIL, TEST_CART_OBJECT)
                    assertNotNull(obj)

                    assertEquals(HttpStatusCode.NotFound, response.status())
                }
            }
        }
    }

    @Test
    fun `should return NoContent if object deleted after login`() {
        runServer {
            handleDeleteRequest(
                "/objects/$TEST_SLICED_OBJECT",
                logged = true
            ) {
                runBlocking {
                    val testRepository by inject<TestRepository>()
                    val obj = testRepository.getUserObjectById(TEST_USER_EMAIL, TEST_SLICED_OBJECT)
                    assertNull(obj)

                    assertEquals(HttpStatusCode.NoContent, response.status())
                }
            }
        }
    }
}