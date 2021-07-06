package tests.objectsRouteTests

import io.ktor.http.*
import org.junit.Test
import org.koin.test.KoinTest
import tests.handleGetRequest
import tests.runServer
import tests.runWithLoggedUser
import kotlin.test.assertEquals

class ObjectsRoute : KoinTest {

    @Test
    fun `should return ok if user not logged`() {
        runServer {
            handleGetRequest("/objects") {
                assertEquals(HttpStatusCode.OK, response.status())
            }
        }
    }

    @Test
    fun `should return ok if user logged`() {
        runServer {
            runWithLoggedUser {
                handleGetRequest("/objects") {
                    assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }
}