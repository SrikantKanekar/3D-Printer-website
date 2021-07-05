package tests.objectsRoute

import io.ktor.http.*
import org.junit.Test
import org.koin.test.KoinTest
import tests.handleGetRequest
import tests.runTest
import tests.runWithLoggedUser
import kotlin.test.assertEquals

class ObjectsRouteTest : KoinTest {

    @Test
    fun `should return ok if user not logged`() {
        runTest {
            handleGetRequest("/objects") {
                assertEquals(HttpStatusCode.OK, response.status())
            }
        }
    }

    @Test
    fun `should return ok if user logged`() {
        runTest {
            runWithLoggedUser {
                handleGetRequest("/objects") {
                    assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }
}