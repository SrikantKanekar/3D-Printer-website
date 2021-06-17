package tests

import com.example.features.account.data.AccountRepository
import com.example.features.auth.data.AuthRepository
import com.example.features.auth.domain.Constants.EMAIL_ALREADY_TAKEN
import com.example.features.auth.domain.Constants.EMAIL_PASSWORD_INCORRECT
import com.example.features.auth.domain.Constants.PASSWORDS_DO_NOT_MATCH
import com.example.features.auth.domain.UserPrincipal
import com.example.module
import data.Constants.TEST_CREATED_OBJECT
import data.Constants.TEST_FILE_UPLOAD_NAME
import data.Constants.TEST_USER_EMAIL
import data.Constants.TEST_USER_PASSWORD
import data.Constants.TEST_USER_USERNAME
import di.testModules
import io.ktor.http.*
import io.ktor.server.testing.*
import io.ktor.sessions.*
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AuthRouteTest : KoinTest {

    private val authRepository by inject<AuthRepository>()
    private val accountRepository by inject<AccountRepository>()

    @Test
    fun `get login route test`() {
        withTestApplication({ module(testing = true, koinModules = testModules) }) {
            handleRequest(HttpMethod.Get, "/auth/login").apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }
        }
    }

    @Test
    fun `login success with valid credentials`() {
        withTestApplication({ module(testing = true, koinModules = testModules) }) {
            testUserLogin()
        }
    }

    @Test
    fun `login failure with invalid credentials`() {
        withTestApplication({ module(testing = true, koinModules = testModules) }) {
            handleRequest(HttpMethod.Post, "/auth/login") {
                addHeader(HttpHeaders.ContentType, formUrlEncoded)
                setBody(
                    listOf(
                        "email" to "INVALID_EMAIL",
                        "password" to "INVALID_PASSWORD"
                    ).formUrlEncode()
                )
            }.apply {
                assertEquals(EMAIL_PASSWORD_INCORRECT, response.content)
            }
        }
    }

    @Test
    fun `objects cookie sync success`() {
        withTestApplication({ module(testing = true, koinModules = testModules) }) {
            cookiesSession {
                `create object before user login`()
                testUserLogin()
                runBlocking {
                    val obj = accountRepository.getUser(TEST_USER_EMAIL)
                        .objects
                        .find { it.id == TEST_CREATED_OBJECT }!!
                    assertEquals(TEST_FILE_UPLOAD_NAME, obj.name)
                    assertFileNotNullAndDelete(obj.id)
                }
            }
        }
    }

    @Test
    fun `get register route test`() {
        withTestApplication({ module(testing = true, koinModules = testModules) }) {
            handleRequest(HttpMethod.Get, "/auth/register").apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }
        }
    }

    @Test
    fun `register success`() {
        withTestApplication({ module(testing = true, koinModules = testModules) }) {
            handleRequest(HttpMethod.Post, "/auth/register") {
                addHeader(HttpHeaders.ContentType, formUrlEncoded)
                setBody(
                    listOf(
                        "username" to "NEW_USERNAME",
                        "email" to "NEW_EMAIL",
                        "password1" to "NEW_PASSWORD",
                        "password2" to "NEW_PASSWORD"
                    ).formUrlEncode()
                )
            }.apply {
                runBlocking {
                    assertTrue(authRepository.doesUserExist("NEW_EMAIL"))

                    val userPrincipal = response.call.sessions.get<UserPrincipal>()!!
                    assertEquals("NEW_EMAIL", userPrincipal.email)

                    assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }

    @Test
    fun `register failure email already exist`() {
        withTestApplication({ module(testing = true, koinModules = testModules) }) {
            handleRequest(HttpMethod.Post, "/auth/register") {
                addHeader(HttpHeaders.ContentType, formUrlEncoded)
                setBody(
                    listOf(
                        "username" to TEST_USER_USERNAME,
                        "email" to TEST_USER_EMAIL,
                        "password1" to TEST_USER_PASSWORD,
                        "password2" to TEST_USER_PASSWORD
                    ).formUrlEncode()
                )
            }.apply {
                runBlocking {
                    assertTrue(authRepository.doesUserExist(TEST_USER_EMAIL))
                    assertEquals(EMAIL_ALREADY_TAKEN, response.content)
                    assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }

    @Test
    fun `register failure passwords don't match`() {
        withTestApplication({ module(testing = true, koinModules = testModules) }) {
            handleRequest(HttpMethod.Post, "/auth/register") {
                addHeader(HttpHeaders.ContentType, formUrlEncoded)
                setBody(
                    listOf(
                        "username" to "NEW_USERNAME",
                        "email" to "NEW_EMAIL",
                        "password1" to "NEW_PASSWORD1",
                        "password2" to "NEW_PASSWORD2"
                    ).formUrlEncode()
                )
            }.apply {
                runBlocking {
                    assertFalse(authRepository.doesUserExist("NEW_EMAIL"))
                    assertEquals(PASSWORDS_DO_NOT_MATCH, response.content)
                    assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }
}