package tests.authRouteTests

import com.example.features.auth.requests.ResetPasswordRequest
import com.example.util.ValidationException
import com.example.util.checkPassword
import com.example.util.constants.Auth.INCORRECT_PASSWORD
import com.example.util.constants.Auth.PASSWORD_DO_NOT_MATCH
import data.TestConstants.TEST_INVALID_ID
import data.TestConstants.TEST_USER_EMAIL
import data.TestConstants.TEST_USER_PASSWORD
import fakeDataSource.TestRepository
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject
import tests.handlePutRequest
import tests.runServer
import tests.userLogin
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class ResetPassword : KoinTest {

    @Test
    fun `should return Unauthorised if not logged`() {
        runServer {
            handlePutRequest(
                "/auth/reset-password",
                ResetPasswordRequest(
                    TEST_USER_PASSWORD,
                    "1111",
                    "2222"
                )
            ) {
                assertEquals(HttpStatusCode.Unauthorized, response.status())
            }
        }
    }

    @Test
    fun `should fail if input is invalid`() {
        assertFailsWith<ValidationException> {
            ResetPasswordRequest(
                TEST_USER_PASSWORD,
                "111",
                "111"
            )
        }
    }

    @Test
    fun `should return error if passwords don't match`() {
        runServer {
            handlePutRequest(
                "/auth/reset-password",
                ResetPasswordRequest(
                    TEST_USER_PASSWORD,
                    "1111",
                    "2222"
                ),
                logged = true
            ) {
                assertEquals(PASSWORD_DO_NOT_MATCH, response.content)
                assertEquals(HttpStatusCode.BadRequest, response.status())
            }
        }
    }

    @Test
    fun `should return error if old password is incorrect`() {
        runServer {
            handlePutRequest(
                "/auth/reset-password",
                ResetPasswordRequest(
                    TEST_INVALID_ID,
                    "1111",
                    "1111"
                ),
                logged = true
            ) {
                assertEquals(INCORRECT_PASSWORD, response.content)
                assertEquals(HttpStatusCode.BadRequest, response.status())
            }
        }
    }

    @Test
    fun `should update password if reset is successful`() {
        runServer {
            handlePutRequest(
                "/auth/reset-password",
                ResetPasswordRequest(
                    TEST_USER_PASSWORD,
                    "1111",
                    "1111"
                ),
                logged = true
            ) {
                runBlocking {
                    val testRepository by inject<TestRepository>()
                    val user = testRepository.getUser(TEST_USER_EMAIL)
                    assertTrue(checkPassword("1111", user.password))

                    assertEquals(HttpStatusCode.NoContent, response.status())
                }
            }
            assertFails { userLogin() }
        }
    }
}