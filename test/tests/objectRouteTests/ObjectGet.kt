package tests.objectRouteTests

import com.example.model.Object
import data.TestConstants.TEST_CREATED_OBJECT
import data.TestConstants.TEST_INVALID_ID
import data.TestConstants.TEST_UNSLICED_OBJECT
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.junit.Test
import org.koin.test.KoinTest
import tests.`create object before user login`
import tests.handleGetRequest
import tests.runServer
import kotlin.test.assertEquals

class ObjectGet : KoinTest {

    @Test
    fun `should return Not found if invalid Id before login`() {
        runServer {
            handleGetRequest("/objects/$TEST_INVALID_ID") {
                assertEquals(HttpStatusCode.NotFound, response.status())
            }
        }
    }

    @Test
    fun `should return Not found if invalid Id after login`() {
        runServer {
            handleGetRequest(
                uri = "/objects/$TEST_INVALID_ID",
                logged = true
            ) {
                assertEquals(HttpStatusCode.NotFound, response.status())
            }
        }
    }

    @Test
    fun `should return object if success before login`() {
        runServer {
            cookiesSession {
                `create object before user login`()
                handleGetRequest("/objects/$TEST_CREATED_OBJECT") {
                    val obj = Json.decodeFromString<Object>(response.content!!)
                    assertEquals(TEST_CREATED_OBJECT, obj.id)

                    assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }

    @Test
    fun `should return object if success after login`() {
        runServer {
            handleGetRequest(
                uri = "/objects/$TEST_UNSLICED_OBJECT",
                logged = true
            ) {
                val obj = Json.decodeFromString<Object>(response.content!!)
                assertEquals(TEST_UNSLICED_OBJECT, obj.id)

                assertEquals(HttpStatusCode.OK, response.status())
            }
        }
    }
}