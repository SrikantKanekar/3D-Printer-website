package tests.authRouteTests

import com.auth0.jwt.JWT
import com.example.features.auth.domain.RegisterRequest
import com.example.util.ValidationException
import com.example.util.constants.Auth.ADMIN_CLAIM
import com.example.util.constants.Auth.EMAIL_ALREADY_TAKEN
import com.example.util.constants.Auth.EMAIL_CLAIM
import com.example.util.constants.Auth.PASSWORDS_DO_NOT_MATCH
import com.example.util.constants.Auth.USERNAME_CLAIM
import data.TestConstants.TEST_USER_EMAIL
import data.TestConstants.TEST_USER_PASSWORD
import data.TestConstants.TEST_USER_USERNAME
import fakeDataSource.TestRepository
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject
import tests.accountRouteTests.LONG_USERNAME
import tests.handlePostRequest
import tests.runServer
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

const val NEW_EMAIL = "NEW_EMAIL"
const val NEW_USERNAME = "NEW_USERNAME"

class Register : KoinTest {

    @Test
    fun `should fail if input is invalid`() {
        assertFailsWith<ValidationException> {
            RegisterRequest("1", "valid", "valid", "valid")
        }
        assertFailsWith<ValidationException> {
            RegisterRequest(LONG_USERNAME, "valid", "valid", "valid")
        }
        assertFailsWith<ValidationException> {
            RegisterRequest("valid", "valid", "111", "valid")
        }
    }

    @Test
    fun `should return error if passwords don't match`() {
        runServer {
            handlePostRequest(
                "/auth/register",
                RegisterRequest(
                    NEW_USERNAME,
                    NEW_EMAIL,
                    "1111",
                    "2222"
                )
            ) {
                assertEquals(PASSWORDS_DO_NOT_MATCH, response.content)
                assertEquals(HttpStatusCode.BadRequest, response.status())
            }
        }
    }

    @Test
    fun `should return error if email already exist`() {
        runServer {
            handlePostRequest(
                "/auth/register",
                RegisterRequest(
                    TEST_USER_USERNAME,
                    TEST_USER_EMAIL,
                    TEST_USER_PASSWORD,
                    TEST_USER_PASSWORD
                )
            ) {
                assertEquals(EMAIL_ALREADY_TAKEN, response.content)
                assertEquals(HttpStatusCode.BadRequest, response.status())
            }
        }
    }

    @Test
    fun `should create user and return token upon success`() {
        runServer {
            handlePostRequest(
                "/auth/register",
                RegisterRequest(
                    NEW_USERNAME,
                    NEW_EMAIL,
                    "1111",
                    "1111"
                )
            ) {
                runBlocking {
                    val testRepository by inject<TestRepository>()
                    assertTrue(testRepository.doesUserExist(NEW_EMAIL))

                    val token = response.content!!
                    val decodedJWT = JWT.decode(token)
                    assertEquals(NEW_EMAIL, decodedJWT.getClaim(EMAIL_CLAIM).asString())
                    assertEquals(NEW_USERNAME, decodedJWT.getClaim(USERNAME_CLAIM).asString())
                    assertEquals(false, decodedJWT.getClaim(ADMIN_CLAIM).asBoolean())
                }
            }
        }
    }
}