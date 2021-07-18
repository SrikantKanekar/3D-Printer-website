package tests.accountRouteTests

import com.example.features.account.domain.requests.UpdateAccountRequest
import data.TestConstants.TEST_USER_EMAIL
import fakeDataSource.TestRepository
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject
import tests.handlePostRequest
import tests.runServer
import kotlin.test.assertEquals
import kotlin.test.assertFails

const val UPDATED_USERNAME = "UPDATED_USERNAME"
const val SHORT_USERNAME = "AB"
const val LONG_USERNAME = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"

class UpdateAccount : KoinTest {

    @Test
    fun `should return unauthorised if user is not logged`() {
        runServer {
            handlePostRequest(
                uri = "/account/update",
                body = UpdateAccountRequest(UPDATED_USERNAME)
            ) {
                assertEquals(HttpStatusCode.Unauthorized, response.status())
            }
        }
    }

    @Test
    fun `should fail if input is invalid`() {
        assertFails { UpdateAccountRequest(SHORT_USERNAME) }
        assertFails { UpdateAccountRequest(LONG_USERNAME) }
    }

    @Test
    fun `should return updated username`() {
        runServer {
            handlePostRequest(
                uri = "/account/update",
                body = UpdateAccountRequest(UPDATED_USERNAME),
                logged = true
            ) {
                runBlocking {
                    val testRepository by inject<TestRepository>()
                    val user = testRepository.getUser(TEST_USER_EMAIL)
                    assertEquals(UPDATED_USERNAME, user.username)
                }
            }
        }
    }
}