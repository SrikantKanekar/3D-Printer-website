package tests

import com.example.module
import di.testModules
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.Test
import kotlin.test.assertEquals

class OrdersRouteTest {

    @Test
    fun `get orders route test`() {
        withTestApplication({ module(testing = true, koinModules = testModules) }) {
            handleRequest(HttpMethod.Get, "/orders").apply {
                assertEquals(HttpStatusCode.Unauthorized, response.status())
            }
            runWithLoggedUser {
                handleRequest(HttpMethod.Get, "/orders").apply {
                    assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }
}