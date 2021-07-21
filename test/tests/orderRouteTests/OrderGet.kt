package tests.orderRouteTests

import com.example.features.order.response.OrderObjectResponse
import data.TestConstants.TEST_INVALID_ID
import data.TestConstants.TEST_PENDING_OBJECT
import data.TestConstants.TEST_PLACED_ORDER
import data.TestConstants.TEST_UNKNOWN_USER_ORDER
import io.ktor.http.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.junit.Test
import org.koin.test.KoinTest
import tests.handleGetRequest
import tests.runServer
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class OrderGet : KoinTest {

    @Test
    fun `should return Unauthorised if not logged`() {
        runServer {
            handleGetRequest("/orders/$TEST_PLACED_ORDER") {
                assertEquals(HttpStatusCode.Unauthorized, response.status())
            }
        }
    }

    @Test
    fun `should return Not Found for invalid order Id`() {
        runServer {
            handleGetRequest(
                uri = "/orders/$TEST_INVALID_ID",
                logged = true
            ) {
                assertEquals(HttpStatusCode.NotFound, response.status())
            }
        }
    }

    @Test
    fun `should return Not Found for invalid order Id and admin user`() {
        runServer {
            handleGetRequest(
                uri = "/orders/$TEST_INVALID_ID",
                admin = true
            ) {
                assertEquals(HttpStatusCode.NotFound, response.status())
            }
        }
    }

    @Test
    fun `should return not found if order doesn't belong to user`() {
        runServer {
            handleGetRequest(
                uri = "/orders/$TEST_UNKNOWN_USER_ORDER",
                logged = true
            ) {
                assertEquals(HttpStatusCode.NotFound, response.status())
            }
        }
    }

    @Test
    fun `should return order and objects for logged user`() {
        runServer {
            handleGetRequest(
                uri = "/orders/$TEST_PLACED_ORDER",
                logged = true
            ) {
                val res = Json.decodeFromString<OrderObjectResponse>(response.content!!)
                val order = res.order
                val objects = res.objects

                assertTrue(order.objectIds.contains(TEST_PENDING_OBJECT))
                assertEquals(
                    TEST_PENDING_OBJECT,
                    objects.find { it.id == TEST_PENDING_OBJECT }?.id
                )
            }
        }
    }

    @Test
    fun `should return order and objects for admin if success`() {
        runServer {
            handleGetRequest(
                uri = "/orders/$TEST_PLACED_ORDER",
                admin = true
            ) {
                val res = Json.decodeFromString<OrderObjectResponse>(response.content!!)
                val order = res.order
                val objects = res.objects

                assertTrue(order.objectIds.contains(TEST_PENDING_OBJECT))
                assertEquals(
                    TEST_PENDING_OBJECT,
                    objects.find { it.id == TEST_PENDING_OBJECT }?.id
                )
            }
        }
    }
}