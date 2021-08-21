package tests.authRouteTests

import com.auth0.jwt.JWT
import com.example.features.auth.domain.LoginRequest
import com.example.util.ValidationException
import com.example.util.constants.Auth.ADMIN_CLAIM
import com.example.util.constants.Auth.EMAIL_CLAIM
import com.example.util.constants.Auth.EMAIL_PASSWORD_INCORRECT
import com.example.util.constants.Auth.USERNAME_CLAIM
import data.TestConstants.TEST_INVALID_ID
import data.TestConstants.TEST_USER_EMAIL
import data.TestConstants.TEST_USER_PASSWORD
import data.TestConstants.TEST_USER_USERNAME
import io.ktor.http.*
import org.junit.Test
import org.koin.test.KoinTest
import tests.handlePostRequest
import tests.runServer
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotEquals

class Login : KoinTest {

    @Test
    fun `should fail if input is invalid`() {
        assertFailsWith<ValidationException> {
            LoginRequest("111111", "111")
        }
    }

    @Test
    fun `should return error for invalid login credentials`() {
        runServer {
            handlePostRequest(
                "/auth/login",
                LoginRequest(TEST_INVALID_ID, TEST_INVALID_ID)
            ) {
                assertEquals(EMAIL_PASSWORD_INCORRECT, response.content)
                assertEquals(HttpStatusCode.BadRequest, response.status())
            }
        }
    }

    @Test
    fun `should return valid token after login`() {
        runServer {
            handlePostRequest(
                "/auth/login",
                LoginRequest(TEST_USER_EMAIL, TEST_USER_PASSWORD)
            ) {
                assertNotEquals(EMAIL_PASSWORD_INCORRECT, response.content)

                val token = response.content!!
                val decodedJWT = JWT.decode(token)
                assertEquals(TEST_USER_EMAIL, decodedJWT.getClaim(EMAIL_CLAIM).asString())
                assertEquals(TEST_USER_USERNAME, decodedJWT.getClaim(USERNAME_CLAIM).asString())
                assertEquals(false, decodedJWT.getClaim(ADMIN_CLAIM).asBoolean())
            }
        }
    }
}