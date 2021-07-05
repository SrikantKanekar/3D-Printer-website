package tests.accountRoute

import data.TestConstants.TEST_USER_EMAIL
import data.TestConstants.UPDATED_USERNAME
import fakeDataSource.TestRepository
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject
import tests.handleGetRequest
import tests.handlePostRequest
import tests.runTest
import tests.runWithLoggedUser
import kotlin.test.assertEquals

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