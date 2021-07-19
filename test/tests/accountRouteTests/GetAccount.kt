package tests.accountRouteTests

import com.example.model.UserPrincipal
import data.TestConstants.TEST_USER_EMAIL
import data.TestConstants.TEST_USER_USERNAME
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.junit.Test
import org.koin.test.KoinTest
import tests.handleGetRequest
import tests.runServer
import kotlin.test.assertEquals

class GetAccount : KoinTest {

    @Test
    fun `should return unauthorised if user is not logged`() {
        runServer {
            handleGetRequest(uri = "/account") {
                assertEquals(HttpStatusCode.Unauthorized, response.status())
            }
        }
    }

    @Test
    fun `should return principal if user is logged`() {
        runServer {
            handleGetRequest(uri = "/account", logged = true) {
                runBlocking {
                    val principal = Json.decodeFromString<UserPrincipal>(response.content!!)
                    assertEquals(TEST_USER_EMAIL, principal.email)
                    assertEquals(TEST_USER_USERNAME, principal.username)
                    assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }
}