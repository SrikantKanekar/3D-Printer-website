package features.auth

import com.example.features.auth.data.AuthRepository
import com.example.features.auth.domain.Constants
import com.example.features.auth.domain.Constants.EMAIL_PASSWORD_INCORRECT
import com.example.features.auth.domain.UserIdPrincipal
import com.example.module
import data.Constants.TEST_USER_EMAIL
import data.Constants.TEST_USER_PASSWORD
import data.Constants.TEST_USER_USERNAME
import di.testAuthModule
import io.ktor.http.*
import io.ktor.server.testing.*
import io.ktor.sessions.*
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class AuthRouteTest : KoinTest {

    private val authRepository by inject<AuthRepository>()

    @Test
    fun `get login route test`() {
        withTestApplication({ module(testing = true, koinModules = listOf(testAuthModule)) }) {
            handleRequest(HttpMethod.Get, "/auth/login").apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }
        }
    }

    @Test
    fun `login success with valid credentials`() {
        withTestApplication({ module(testing = true, koinModules = listOf(testAuthModule)) }) {
            testUserLogin()
        }
    }

    @Test
    fun `login failure with invalid credentials`() {
        withTestApplication({ module(testing = true, koinModules = listOf(testAuthModule)) }) {
            handleRequest(HttpMethod.Post, "/auth/login") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded.toString())
                setBody(
                    listOf(
                        "Email" to "INVALID_EMAIL",
                        "Password" to "INVALID_PASSWORD"
                    ).formUrlEncode()
                )
            }.apply {
                assertEquals(EMAIL_PASSWORD_INCORRECT, response.content)
            }
        }
    }

    @Test
    fun `get register route test`() {
        withTestApplication({ module(testing = true, koinModules = listOf(testAuthModule)) }) {
            handleRequest(HttpMethod.Get, "/auth/register").apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }
        }
    }

    @Test
    fun `register success`() {
        withTestApplication({ module(testing = true, koinModules = listOf(testAuthModule)) }) {
            handleRequest(HttpMethod.Post, "/auth/register") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded.toString())
                setBody(
                    listOf(
                        "Email" to "NEW_EMAIL",
                        "Password" to "NEW_PASSWORD",
                        "username" to "NEW_USERNAME"
                    ).formUrlEncode()
                )
            }.apply {
                runBlocking {
                    assertEquals(HttpStatusCode.OK, response.status())
                    assertTrue(authRepository.doesUserExist("NEW_EMAIL"))
                    val userIdPrincipal = response.call.sessions.get<UserIdPrincipal>()
                    assertEquals("NEW_EMAIL", userIdPrincipal?.email)
                }
            }
        }
    }

    @Test
    fun `register failure email already exist`() {
        withTestApplication({ module(testing = true, koinModules = listOf(testAuthModule)) }) {
            handleRequest(HttpMethod.Post, "/auth/register") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded.toString())
                setBody(
                    listOf(
                        "Email" to TEST_USER_EMAIL,
                        "Password" to TEST_USER_PASSWORD,
                        "username" to TEST_USER_USERNAME
                    ).formUrlEncode()
                )
            }.apply {
                runBlocking {
                    assertTrue(authRepository.doesUserExist(TEST_USER_EMAIL))
                    assertEquals(Constants.EMAIL_ALREADY_TAKEN, response.content)
                }
            }
        }
    }
}

// Login with a registered user
fun TestApplicationEngine.testUserLogin() {
    handleRequest(HttpMethod.Post, "/auth/login") {
        addHeader(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded.toString())
        setBody(
            listOf(
                "Email" to TEST_USER_EMAIL,
                "Password" to TEST_USER_PASSWORD
            ).formUrlEncode()
        )
    }.apply {
        assertEquals(HttpStatusCode.OK, response.status())
        assertNotEquals(EMAIL_PASSWORD_INCORRECT, response.content)
        val userIdPrincipal = response.call.sessions.get<UserIdPrincipal>()
        assertEquals(TEST_USER_EMAIL, userIdPrincipal?.email)
    }
}

fun TestApplicationEngine.runWithTestUser(test: TestApplicationEngine.() -> Unit) {
    cookiesSession {
        testUserLogin()
        test()
    }
}