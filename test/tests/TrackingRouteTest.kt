package tests

import com.example.module
import di.testModule
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.Test
import kotlin.test.assertEquals

class TrackingRouteTest {

    @Test
    fun `get tracking route test`() {
        withTestApplication({ module(testing = true, koinModules = listOf(testModule)) }) {
            handleRequest(HttpMethod.Get, "/tracking").apply {
                assertEquals(HttpStatusCode.Unauthorized, response.status())
            }
            runWithTestUser {
                handleRequest(HttpMethod.Get, "/tracking").apply {
                    assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }
}