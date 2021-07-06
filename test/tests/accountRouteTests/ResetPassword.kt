package tests.accountRouteTests

import com.example.features.account.domain.AccountConstants.INCORRECT_PASSWORD
import com.example.features.account.domain.AccountConstants.PASSWORD_DO_NOT_MATCH
import com.example.features.auth.domain.checkHashForPassword
import data.TestConstants.TEST_USER_EMAIL
import data.TestConstants.TEST_USER_PASSWORD
import fakeDataSource.TestRepository
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject
import tests.handlePostRequest
import tests.runServer
import tests.runWithLoggedUser
import tests.userLogin
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertTrue

class ResetPassword : KoinTest {

    @Test
    fun `should return error if passwords don't match`() {
        runServer {
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
        runServer {
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
        runServer {
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
}