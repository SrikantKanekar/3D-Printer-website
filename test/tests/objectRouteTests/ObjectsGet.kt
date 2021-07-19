package tests.objectRouteTests

import com.example.model.Object
import data.TestConstants.TEST_CREATED_OBJECT
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
import kotlin.test.assertNotNull

class ObjectsGet : KoinTest {

    @Test
    fun `should return object list before login`() {
        runServer {
            cookiesSession {
                `create object before user login`()
                handleGetRequest("/objects") {
                    val objects = Json.decodeFromString<List<Object>>(response.content!!)
                    assertNotNull(objects.find { it.id == TEST_CREATED_OBJECT })

                    assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }

    @Test
    fun `should return object list after login`() {
        runServer {
            handleGetRequest(
                uri = "/objects",
                logged = true
            ) {
                val objects = Json.decodeFromString<List<Object>>(response.content!!)
                assertNotNull(objects.find { it.id == TEST_UNSLICED_OBJECT })

                assertEquals(HttpStatusCode.OK, response.status())
            }
        }
    }
}