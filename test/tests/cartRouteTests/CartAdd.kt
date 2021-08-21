package tests.cartRouteTests

import com.example.util.enums.ObjectStatus.CART
import data.TestConstants.TEST_INVALID_ID
import data.TestConstants.TEST_SLICED_OBJECT
import data.TestConstants.TEST_USER_EMAIL
import fakeDataSource.TestRepository
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject
import tests.handlePostRequest
import tests.runServer
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class CartAdd : KoinTest {

    @Test
    fun `should return unauthorised if user not logged`() {
        runServer {
            handlePostRequest(
                uri = "/cart/$TEST_SLICED_OBJECT",
                body = Unit
            ) {
                assertEquals(HttpStatusCode.Unauthorized, response.status())
            }
        }
    }

    @Test
    fun `should return method not allowed for invalid object ID`() {
        runServer {
            handlePostRequest(
                uri = "/cart/$TEST_INVALID_ID",
                body = Unit,
                logged = true
            ) {
                assertEquals(HttpStatusCode.MethodNotAllowed, response.status())
            }
        }
    }

    @Test
    fun `should return no content for sliced object`() {
        runServer {
            handlePostRequest(
                uri = "/cart/$TEST_SLICED_OBJECT",
                body = Unit,
                logged = true
            ) {
                runBlocking {
                    val testRepository by inject<TestRepository>()
                    val obj = testRepository.getUser(TEST_USER_EMAIL)
                        .objects
                        .filter { it.status == CART }
                        .find { it.id == TEST_SLICED_OBJECT }
                    assertNotNull(obj)

                    assertEquals(HttpStatusCode.NoContent, response.status())
                }
            }
        }
    }
}