package tests.orderRouteTests

import com.example.model.Order
import data.TestConstants.TEST_USER_EMAIL
import fakeDataSource.TestRepository
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject
import tests.handleGetRequest
import tests.runServer
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class OrdersGet : KoinTest {

    @Test
    fun `should return Unauthorised if not logged`() {
        runServer {
            handleGetRequest("/orders") {
                assertEquals(HttpStatusCode.Unauthorized, response.status())
            }
        }
    }

    @Test
    fun `should return orders if logged`() {
        runServer {
            handleGetRequest(
                uri = "/orders",
                logged = true
            ) {
                runBlocking {
                    val testRepository by inject<TestRepository>()
                    val user = testRepository.getUser(TEST_USER_EMAIL)

                    val responseOrders = Json.decodeFromString<List<Order>>(response.content!!)
                    val responseOrderIds = responseOrders.map { it._id }

                    assertTrue { responseOrderIds.containsAll(user.orderIds) }

                    assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }
}