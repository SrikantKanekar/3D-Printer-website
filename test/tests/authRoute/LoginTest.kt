package tests.authRoute

import com.example.features.auth.domain.AuthConstants.EMAIL_PASSWORD_INCORRECT
import data.TestConstants.TEST_CREATED_OBJECT
import data.TestConstants.TEST_USER_EMAIL
import fakeDataSource.TestRepository
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject
import tests.*
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class LoginTest : KoinTest {

    @Test
    fun `should return ok if user is not logged`() {
        runTest {
            handleGetRequest("/auth/login") {
                assertEquals(HttpStatusCode.OK, response.status())
            }
        }
    }

    @Test
    fun `should redirect if user is logged`() {
        runTest {
            runWithLoggedUser {
                handleGetRequest("/auth/login") {
                    assertEquals(HttpStatusCode.Found, response.status())
                }
            }
        }
    }

    @Test
    fun `should return error for invalid credentials`() {
        runTest {
            handlePostRequest(
                "/auth/login",
                listOf(
                    "email" to "INVALID_EMAIL",
                    "password" to "INVALID_PASSWORD"
                )
            ) {
                assertEquals(EMAIL_PASSWORD_INCORRECT, response.content)
            }
        }
    }

    @Test
    fun `objects cookie sync success`() {
        runTest {
            cookiesSession {
                `create object before user login`()
                userLogin()
                runBlocking {
                    val testRepository by inject<TestRepository>()
                    val obj = testRepository.getUser(TEST_USER_EMAIL)
                        .objects
                        .find { it.id == TEST_CREATED_OBJECT }
                    assertNotNull(obj)
                }
            }
        }
    }
}