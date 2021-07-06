package tests.authRouteTests

import com.example.features.auth.domain.AuthConstants.EMAIL_ALREADY_TAKEN
import com.example.features.auth.domain.AuthConstants.PASSWORDS_DO_NOT_MATCH
import com.example.features.auth.domain.UserPrincipal
import data.TestConstants.TEST_USER_EMAIL
import data.TestConstants.TEST_USER_PASSWORD
import data.TestConstants.TEST_USER_USERNAME
import fakeDataSource.TestRepository
import io.ktor.http.*
import io.ktor.sessions.*
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject
import tests.handleGetRequest
import tests.handlePostRequest
import tests.runServer
import tests.runWithLoggedUser
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class Register : KoinTest {

    @Test
    fun `should return ok if user is not logged`() {
        runServer {
            handleGetRequest("/auth/register") {
                assertEquals(HttpStatusCode.OK, response.status())
            }
        }
    }

    @Test
    fun `should redirect if user is logged`() {
        runServer {
            runWithLoggedUser {
                handleGetRequest("/auth/register") {
                    assertEquals(HttpStatusCode.Found, response.status())
                }
            }
        }
    }

    @Test
    fun `should return error if passwords don't match`() {
        runServer {
            handlePostRequest(
                "/auth/register",
                listOf(
                    "username" to "NEW_USERNAME",
                    "email" to "NEW_EMAIL",
                    "password1" to "1111",
                    "password2" to "2222"
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
                listOf(
                    "username" to TEST_USER_USERNAME,
                    "email" to TEST_USER_EMAIL,
                    "password1" to TEST_USER_PASSWORD,
                    "password2" to TEST_USER_PASSWORD
                )
            ) {
                assertEquals(EMAIL_ALREADY_TAKEN, response.content)
            }
        }
    }

    @Test
    fun `should return registered email upon success`() {
        runServer {
            handlePostRequest(
                "/auth/register",
                listOf(
                    "username" to "NEW_USERNAME",
                    "email" to "NEW_EMAIL",
                    "password1" to "NEW_PASSWORD",
                    "password2" to "NEW_PASSWORD"
                )
            ) {
                runBlocking {
                    val testRepository by inject<TestRepository>()
                    assertTrue(testRepository.doesUserExist("NEW_EMAIL"))

                    val userPrincipal = response.call.sessions.get<UserPrincipal>()!!
                    assertEquals("NEW_EMAIL", userPrincipal.email)
                }
            }
        }
    }
}