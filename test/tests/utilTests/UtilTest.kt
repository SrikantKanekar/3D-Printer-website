package tests.utilTests

import io.ktor.http.*
import org.junit.Test
import tests.handleGetRequest
import tests.runServer
import kotlin.test.assertEquals

class UtilTest {

    @Test
    fun `should return ok for home page`() {
        runServer {
            handleGetRequest(uri = "/", logged = true) {
                assertEquals(HttpStatusCode.OK, response.status())
            }
        }
    }
}