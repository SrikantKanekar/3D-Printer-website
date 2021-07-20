package tests.accountRouteTests

import com.example.model.User
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
    fun `should return user if user is logged`() {
        runServer {
            handleGetRequest(uri = "/account", logged = true) {
                runBlocking {
                    val user = Json.decodeFromString<User>(response.content!!)
                    assertEquals(TEST_USER_EMAIL, user.email)
                    assertEquals(TEST_USER_USERNAME, user.username)
                    assertEquals("", user.password)
                    assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }
}