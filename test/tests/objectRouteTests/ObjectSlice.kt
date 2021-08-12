package tests.objectRouteTests

import com.example.model.ObjectsCookie
import com.example.model.SlicingDetails
import data.TestConstants.TEST_CART_OBJECT
import data.TestConstants.TEST_CREATED_OBJECT
import data.TestConstants.TEST_INVALID_ID
import data.TestConstants.TEST_SLICED_OBJECT
import data.TestConstants.TEST_UNSLICED_OBJECT
import data.TestConstants.TEST_USER_EMAIL
import fakeDataSource.TestRepository
import io.ktor.http.*
import io.ktor.server.testing.*
import io.ktor.sessions.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject
import tests.`create object before user login`
import tests.handlePutRequest
import tests.runServer
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class ObjectSlice : KoinTest {

    private val slicingDetails = SlicingDetails(
        printTime = "1hr",
        materialWeight = 1F,
        materialCost = 1F,
        powerCost = 1F,
        labourCost = 1F,
        price = 3
    )

    @Test
    fun `should return NotFound for invalid Id if not logged`() {
        runServer {
            handlePutRequest(
                uri = "objects/slice/$TEST_INVALID_ID",
                body = slicingDetails
            ) {
                assertEquals(HttpStatusCode.NotFound, response.status())
            }
        }
    }

    @Test
    fun `should return NotFound for invalid Id if logged`() {
        runServer {
            handlePutRequest(
                uri = "objects/slice/$TEST_INVALID_ID",
                body = slicingDetails,
                logged = true
            ) {
                assertEquals(HttpStatusCode.NotFound, response.status())
            }
        }
    }

    @Test
    fun `should return slicing details if not logged`() {
        runServer {
            cookiesSession {
                `create object before user login`()
                handlePutRequest(
                    uri = "objects/slice/$TEST_CREATED_OBJECT",
                    body = slicingDetails,
                ) {
                    val cookie = response.call.sessions.get<ObjectsCookie>()!!
                    val obj = cookie.objects.find { it.id == TEST_CREATED_OBJECT }
                    assertEquals(obj?.slicingDetails, slicingDetails)
                    assertFalse(obj!!.setting.updated)
                }
            }
        }
    }

    @Test
    fun `should return NotFound if already sliced if not logged`() {
        runServer {
            cookiesSession {
                `create object before user login`()
                handlePutRequest(
                    uri = "objects/slice/$TEST_CREATED_OBJECT",
                    body = slicingDetails
                ) {
                    assertEquals(HttpStatusCode.OK, response.status())
                }
                handlePutRequest(
                    uri = "objects/slice/$TEST_CREATED_OBJECT",
                    body = slicingDetails
                ) {
                    assertEquals(HttpStatusCode.NotFound, response.status())
                }
            }
        }
    }

    @Test
    fun `should return NotFound if already sliced if logged`() {
        runServer {
            handlePutRequest(
                uri = "objects/slice/$TEST_SLICED_OBJECT",
                body = slicingDetails,
                logged = true
            ) {
                assertEquals(HttpStatusCode.NotFound, response.status())
            }
        }
    }

    @Test
    fun `should return NotFound for cart object if logged`() {
        runServer {
            handlePutRequest(
                uri = "objects/slice/$TEST_CART_OBJECT",
                body = slicingDetails,
                logged = true
            ) {
                assertEquals(HttpStatusCode.NotFound, response.status())
            }
        }
    }

    @Test
    fun `should return slicing details if logged`() {
        runServer {
            handlePutRequest(
                uri = "objects/slice/$TEST_UNSLICED_OBJECT",
                body = slicingDetails,
                logged = true
            ) {
                runBlocking {
                    val testRepository by inject<TestRepository>()
                    val obj = testRepository.getUserObjectById(TEST_USER_EMAIL, TEST_UNSLICED_OBJECT)!!
                    val res = Json.decodeFromString<SlicingDetails>(response.content!!)
                    assertEquals(res, obj.slicingDetails)
                    assertFalse(obj.setting.updated)
                }
            }
        }
    }
}
