package tests.objectRouteTests

import com.example.util.enums.ObjectStatus.CART
import com.example.util.enums.Quality.STANDARD
import com.example.util.enums.Quality.SUPER
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
import tests.handlePutRequest
import tests.runServer
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class ObjectQuality : KoinTest {

    @Test
    fun `should return MethodNotAllowed for invalid object Id`() {
        runServer {
            handlePutRequest(
                uri = "objects/quality/$TEST_INVALID_ID",
                body = STANDARD,
                logged = true
            ) {
                assertEquals(HttpStatusCode.MethodNotAllowed, response.status())
            }
        }
    }

    @Test
    fun `should return MethodNotAllowed for tracking object`() {
        runServer {
            handlePutRequest(
                uri = "objects/quality/$TEST_TRACKING_OBJECT",
                body = SUPER,
                logged = true
            ) {
                runBlocking {
                    val testRepository by inject<TestRepository>()
                    val user = testRepository.getUser(TEST_USER_EMAIL)
                    val obj = user.objects.find { it.id == TEST_TRACKING_OBJECT }
                    assertNotEquals(obj?.quality, SUPER)

                    assertEquals(HttpStatusCode.MethodNotAllowed, response.status())
                }
            }
        }
    }

    @Test
    fun `should return quality if success`() {
        runServer {
            handlePutRequest(
                uri = "objects/quality/$TEST_CART_OBJECT",
                body = SUPER,
                logged = true
            ) {
                runBlocking {
                    val testRepository by inject<TestRepository>()
                    val user = testRepository.getUser(TEST_USER_EMAIL)
                    val obj = user.objects
                        .filter { it.status == CART }
                        .find { it.id == TEST_CART_OBJECT }
                    assertEquals(SUPER, obj?.quality)

                    assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }
}