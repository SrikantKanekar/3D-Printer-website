package tests.authRouteTests

import com.auth0.jwt.JWT
import com.example.features.auth.domain.RegisterRequest
import com.example.util.ValidationException
import com.example.util.constants.Auth.ADMIN_CLAIM
import com.example.util.constants.Auth.EMAIL_ALREADY_TAKEN
import com.example.util.constants.Auth.EMAIL_CLAIM
import com.example.util.constants.Auth.PASSWORDS_DO_NOT_MATCH
import com.example.util.constants.Auth.USERNAME_CLAIM
import data.TestConstants
import data.TestConstants.TEST_USER_EMAIL
import data.TestConstants.TEST_USER_PASSWORD
import data.TestConstants.TEST_USER_USERNAME
import fakeDataSource.TestRepository
import io.ktor.server.testing.*
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject
import tests.`create object before user login`
import tests.accountRouteTests.LONG_USERNAME
import tests.handlePostRequest
import tests.runServer
import tests.userLogin
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

const val NEW_EMAIL = "NEW_EMAIL"
const val NEW_USERNAME = "NEW_USERNAME"

class Register : KoinTest {

    @Test
    fun `should fail if input is invalid`() {
        assertFailsWith<ValidationException> {
            RegisterRequest("11", "valid", "valid", "valid")
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

    @Test
    fun `should sync cookie objects after login`() {
        runServer {
            cookiesSession {
                `create object before user login`()
                userLogin()
                runBlocking {
                    val testRepository by inject<TestRepository>()
                    val obj = testRepository.getUser(TEST_USER_EMAIL)
                        .objects
                        .find { it.id == TestConstants.TEST_CREATED_OBJECT }
                    assertNotNull(obj)
                }
            }
        }
    }
}