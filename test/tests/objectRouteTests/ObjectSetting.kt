package tests.objectRouteTests

import com.example.model.Setting
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
import tests.handlePatchRequest
import tests.runServer
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class ObjectSetting : KoinTest {

    @Test
    fun `should return not found for invalid object Id`() {
        runServer {
            handlePatchRequest(
                uri = "objects/setting/$TEST_INVALID_ID",
                body = Setting(),
                logged = true
            ) {
                assertEquals(HttpStatusCode.NotFound, response.status())
            }
        }
    }

    @Test
    fun `should return not found for cart object`() {
        runServer {
            handlePatchRequest(
                uri = "objects/setting/$TEST_CART_OBJECT",
                body = Setting(),
                logged = true
            ) {
                assertEquals(HttpStatusCode.NotFound, response.status())
            }
        }
    }

    @Test
    fun `should return ok if success`() {
        runServer {
            handlePatchRequest(
                uri = "objects/setting/$TEST_SLICED_OBJECT",
                body = Setting(),
                logged = true
            ) {
                runBlocking {
                    val testRepository by inject<TestRepository>()
                    val user = testRepository.getUser(TEST_USER_EMAIL)
                    val obj = user.objects.find { it.id == TEST_SLICED_OBJECT }!!
                    assertFalse(obj.slicingDetails.uptoDate)

                    assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }
}