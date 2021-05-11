package tests

import com.example.module
import di.testModules
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.Test
import kotlin.test.assertEquals

class HistoryRouteTest {

    @Test
    fun `get history route test`() {
        withTestApplication({ module(testing = true, koinModules = testModules) }) {
            handleRequest(HttpMethod.Get, "/history").apply {
                assertEquals(HttpStatusCode.Unauthorized, response.status())
            }
            runWithTestUser {
                handleRequest(HttpMethod.Get, "/history").apply {
                    assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }
}