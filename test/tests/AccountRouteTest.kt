package tests

import com.example.features.account.domain.Constants.INCORRECT_PASSWORD
import com.example.features.account.domain.Constants.PASSWORD_DO_NOT_MATCH
import com.example.features.auth.domain.checkHashForPassword
import data.TestConstants.TEST_USER_EMAIL
import data.TestConstants.TEST_USER_PASSWORD
import data.TestConstants.UPDATED_USERNAME
import fakeDataSource.TestRepository
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertTrue

class AccountRouteTest : KoinTest {

    @Test
    fun `should return unauthorised if user is not logged`() {
        runTest {
            handleGetRequest("/account") {
                assertEquals(HttpStatusCode.Unauthorized, response.status())
            }
        }
    }

    @Test
    fun `should return ok if user is logged`() {
        runTest {
            runWithLoggedUser {
                handleGetRequest("/account") {
                    assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }

    @Test
    fun `should return updated username`() {
        runTest {
            runWithLoggedUser {
                handlePostRequest(
                    "/account/update",
                    listOf("username" to UPDATED_USERNAME)
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

    @Test
    fun `should return error if passwords don't match`() {
        runTest {
            runWithLoggedUser {
                handlePostRequest(
                    "/account/reset-password",
                    listOf(
                        "old_password" to TEST_USER_PASSWORD,
                        "new_password" to "1111",
                        "confirm_password" to "2222",
                    )
                ) {
                    assertEquals(PASSWORD_DO_NOT_MATCH, response.content)
                }
            }
        }
    }

    @Test
    fun `should return error if old password is incorrect`() {
        runTest {
            runWithLoggedUser {
                handlePostRequest(
                    "/account/reset-password",
                    listOf(
                        "old_password" to "Invalid password",
                        "new_password" to "1111",
                        "confirm_password" to "1111"
                    )
                ) {
                    assertEquals(INCORRECT_PASSWORD, response.content)
                }
            }
        }
    }

    @Test
    fun `should return updated password if reset is successful`() {
        runTest {
            runWithLoggedUser {
                handlePostRequest(
                    "/account/reset-password",
                    listOf(
                        "old_password" to TEST_USER_PASSWORD,
                        "new_password" to "1111",
                        "confirm_password" to "1111"
                    )
                ) {
                    runBlocking {
                        val testRepository by inject<TestRepository>()
                        val user = testRepository.getUser(TEST_USER_EMAIL)
                        assertTrue(checkHashForPassword("1111", user.password))
                    }
                }
                assertFails { userLogin() }
            }
        }
    }

    @Test
    fun `should redirect if logout is successful`() {
        runTest {
            runWithLoggedUser {
                handleGetRequest("/account/logout") {
                    assertEquals(HttpStatusCode.Found, response.status())
                }
            }
        }
    }
}