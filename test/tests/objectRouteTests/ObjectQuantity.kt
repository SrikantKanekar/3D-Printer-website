package tests.objectRouteTests

import com.example.features.`object`.requests.ObjectQuantityRequest
import com.example.util.ValidationException
import com.example.util.enums.ObjectStatus.CART
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
import tests.handlePatchRequest
import tests.runServer
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotEquals

class ObjectQuantity : KoinTest {

    @Test
    fun `should fail if input is invalid`() {
        assertFailsWith<ValidationException> {
            ObjectQuantityRequest(0)
        }
        assertFailsWith<ValidationException> {
            ObjectQuantityRequest(-1)
        }
    }

    @Test
    fun `should return not found for invalid object Id`() {
        runServer {
            handlePatchRequest(
                uri = "objects/quantity/$TEST_INVALID_ID",
                body = ObjectQuantityRequest(2),
                logged = true
            ) {
                assertEquals(HttpStatusCode.NotFound, response.status())
            }
        }
    }

    @Test
    fun `should return not found for tracking object`() {
        runServer {
            handlePatchRequest(
                uri = "objects/quantity/$TEST_TRACKING_OBJECT",
                body = ObjectQuantityRequest(2),
                logged = true
            ) {
                runBlocking {
                    val testRepository by inject<TestRepository>()
                    val user = testRepository.getUser(TEST_USER_EMAIL)
                    val obj = user.objects.find { it.id == TEST_TRACKING_OBJECT }
                    assertNotEquals(obj?.quantity, 2)

                    assertEquals(HttpStatusCode.NotFound, response.status())                }
            }
        }
    }

    @Test
    fun `should return ok if success`() {
        runServer {
            handlePatchRequest(
                uri = "objects/quantity/$TEST_CART_OBJECT",
                body = ObjectQuantityRequest(2),
                logged = true
            ) {
                runBlocking {
                    val testRepository by inject<TestRepository>()
                    val user = testRepository.getUser(TEST_USER_EMAIL)
                    val obj = user.objects
                        .filter { it.status == CART }
                        .find { it.id == TEST_CART_OBJECT }
                    assertEquals(2, obj?.quantity)

                    assertEquals(HttpStatusCode.OK, response.status())                }
            }
        }
    }
}