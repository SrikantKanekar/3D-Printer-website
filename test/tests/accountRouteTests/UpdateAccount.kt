package tests.accountRouteTests

import com.example.features.account.domain.requests.UpdateAccountRequest
import com.example.util.ValidationException
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
import kotlin.test.assertFailsWith

const val UPDATED_USERNAME = "UPDATED_USERNAME"
const val SHORT_USERNAME = "A"
const val LONG_USERNAME = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"

class UpdateAccount : KoinTest {

    @Test
    fun `should return unauthorised if user is not logged`() {
        runServer {
            handlePostRequest(
                uri = "/account",
                body = UpdateAccountRequest(UPDATED_USERNAME)
            ) {
                assertEquals(HttpStatusCode.Unauthorized, response.status())
            }
        }
    }

    @Test
    fun `should fail if input is invalid`() {
        assertFailsWith<ValidationException> {
            UpdateAccountRequest(SHORT_USERNAME)
        }
        assertFailsWith<ValidationException> {
            UpdateAccountRequest(LONG_USERNAME)
        }
    }

    @Test
    fun `should return updated username`() {
        runServer {
            handlePostRequest(
                uri = "/account",
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