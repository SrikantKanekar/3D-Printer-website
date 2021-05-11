package tests

import com.example.module
import data.Constants.TEST_NOTIFICATION
import di.testModules
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.Test
import kotlin.test.assertEquals

class NotificationRouteTest {

    @Test
    fun `get all notifications route test`() {
        withTestApplication({ module(testing = true, koinModules = testModules) }) {
            handleRequest(HttpMethod.Get, "/notification").apply {
                assertEquals(HttpStatusCode.Unauthorized, response.status())
            }
            runWithTestUser {
                handleRequest(HttpMethod.Get, "/notification").apply {
                    assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }

    @Test
    fun `get notification success`() {
        withTestApplication({ module(testing = true, koinModules = testModules) }) {
            runWithTestUser {
                handleRequest(HttpMethod.Get, "/notification/$TEST_NOTIFICATION").apply {
                    assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }

    @Test
    fun `get notification invalid ID`() {
        withTestApplication({ module(testing = true, koinModules = testModules) }) {
            runWithTestUser {
                handleRequest(HttpMethod.Get, "/notification/invalid-id").apply {
                    assertEquals(HttpStatusCode.NotFound, response.status())
                }
            }
        }
    }
}