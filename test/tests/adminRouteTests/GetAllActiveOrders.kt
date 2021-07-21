package tests.adminRouteTests

import com.example.model.Order
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

class GetAllActiveOrders : KoinTest {

    @Test
    fun `should return Unauthorised if not logged`() {
        runServer {
            handleGetRequest("/admin") {
                assertEquals(HttpStatusCode.Unauthorized, response.status())
            }
        }
    }

    @Test
    fun `should return Forbidden if not admin`() {
        runServer {
            handleGetRequest(
                uri = "/admin",
                logged = true
            ) {
                assertEquals(HttpStatusCode.Unauthorized, response.status())
            }
        }
    }

    @Test
    fun `should return active orders if admin user`() {
        runServer {
            handleGetRequest(
                uri = "/admin",
                admin = true
            ) {
                runBlocking {
                    val testRepository by inject<TestRepository>()
                    val orders = testRepository.getAllActiveOrders()
                    val responseOrders = Json.decodeFromString<List<Order>>(response.content!!)

                    assertEquals(orders, responseOrders)
                    assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }
}