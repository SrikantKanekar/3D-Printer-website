package tests.cartRouteTests

import com.example.model.Object
import data.TestConstants.TEST_CART_OBJECT
import io.ktor.http.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.junit.Test
import tests.handleGetRequest
import tests.runServer
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class CartGet {

    @Test
    fun `should return unauthorised if not logged`() {
        runServer {
            handleGetRequest("/cart") {
                assertEquals(HttpStatusCode.Unauthorized, response.status())
            }
        }
    }

    @Test
    fun `should return cart objects if logged`() {
        runServer {
            handleGetRequest(
                uri = "/cart",
                logged = true
            ) {
                val objects = Json.decodeFromString<List<Object>>(response.content!!)
                val cartObject = objects.find { it.id == TEST_CART_OBJECT }
                assertNotNull(cartObject)

                assertEquals(HttpStatusCode.OK, response.status())
            }
        }
    }
}