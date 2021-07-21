package tests.objectRouteTests

import com.example.model.Setting
import data.TestConstants.TEST_CART_OBJECT
import data.TestConstants.TEST_INVALID_ID
import data.TestConstants.TEST_SLICED_OBJECT
import data.TestConstants.TEST_USER_EMAIL
import fakeDataSource.TestRepository
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject
import tests.handlePutRequest
import tests.runServer
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class ObjectSetting : KoinTest {

    @Test
    fun `should return MethodNotAllowed for invalid object Id`() {
        runServer {
            handlePutRequest(
                uri = "objects/setting/$TEST_INVALID_ID",
                body = Setting(),
                logged = true
            ) {
                assertEquals(HttpStatusCode.MethodNotAllowed, response.status())
            }
        }
    }

    @Test
    fun `should return MethodNotAllowed for cart object`() {
        runServer {
            handlePutRequest(
                uri = "objects/setting/$TEST_CART_OBJECT",
                body = Setting(),
                logged = true
            ) {
                assertEquals(HttpStatusCode.MethodNotAllowed, response.status())
            }
        }
    }

    @Test
    fun `should return setting if success`() {
        runServer {
            handlePutRequest(
                uri = "objects/setting/$TEST_SLICED_OBJECT",
                body = Setting(),
                logged = true
            ) {
                runBlocking {
                    val testRepository by inject<TestRepository>()
                    val user = testRepository.getUser(TEST_USER_EMAIL)
                    val obj = user.objects.find { it.id == TEST_SLICED_OBJECT }!!

                    assertFalse(obj.slicingDetails.uptoDate)

                    val res = Json.decodeFromString<Setting>(response.content!!)
                    assertEquals(res, obj.setting)
                }
            }
        }
    }
}