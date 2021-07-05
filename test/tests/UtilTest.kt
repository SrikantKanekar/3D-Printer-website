package tests

import io.ktor.http.*
import org.junit.Test
import kotlin.test.assertEquals

class UtilTest {

    @Test
    fun `should return ok for home page`() {
        runTest {
            handleGetRequest("/") {
                assertEquals(HttpStatusCode.OK, response.status())
            }
        }
    }
}