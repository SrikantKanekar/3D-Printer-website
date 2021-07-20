package tests.cartRouteTests

import com.example.util.enums.ObjectStatus.NONE
import data.TestConstants.TEST_CART_OBJECT
import data.TestConstants.TEST_INVALID_ID
import data.TestConstants.TEST_TRACKING_OBJECT
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

class CartDelete : KoinTest {

    @Test
    fun `should return Unauthorised if user not logged`() {
        runServer {
            handleDeleteRequest(
                uri = "/cart/$TEST_CART_OBJECT"
            ) {
                assertEquals(HttpStatusCode.Unauthorized, response.status())
            }
        }
    }

    @Test
    fun `should return method not allowed for invalid object Id`() {
        runServer {
            handleDeleteRequest(
                uri = "/cart/$TEST_INVALID_ID",
                logged = true
            ) {
                assertEquals(HttpStatusCode.MethodNotAllowed, response.status())
            }
        }
    }

    @Test
    fun `should return method not allowed for tracking object`() {
        runServer {
            handleDeleteRequest(
                uri = "/cart/$TEST_TRACKING_OBJECT",
                logged = true
            ) {
                runBlocking {
                    val testRepository by inject<TestRepository>()
                    val user = testRepository.getUser(TEST_USER_EMAIL)
                    val obj = user.objects
                        .filter { it.status == NONE }
                        .find { it.id == TEST_TRACKING_OBJECT }
                    assertNull(obj)

                    assertEquals(HttpStatusCode.MethodNotAllowed, response.status())
                }
            }
        }
    }

    @Test
    fun `should return NoContent if Success`() {
        runServer {
            handleDeleteRequest(
                uri = "/cart/$TEST_CART_OBJECT",
                logged = true
            ) {
                runBlocking {
                    val testRepository by inject<TestRepository>()
                    val user = testRepository.getUser(TEST_USER_EMAIL)
                    val obj = user.objects
                        .filter { it.status == NONE }
                        .find { it.id == TEST_CART_OBJECT }
                    assertNotNull(obj)

                    assertEquals(HttpStatusCode.NoContent, response.status())
                }
            }
        }
    }
}