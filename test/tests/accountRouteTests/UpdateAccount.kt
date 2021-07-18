package tests.accountRouteTests

import data.TestConstants
import fakeDataSource.TestRepository
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject
import tests.handlePostRequest
import tests.runServer
import tests.runWithLoggedUser
import kotlin.test.assertEquals

class UpdateAccount: KoinTest {

    @Test
    fun `should return updated username`() {
        runServer {
            runWithLoggedUser {
                handlePostRequest(
                    "/account/update",
                    mapOf("username" to "username1")
                ) {
                    runBlocking {
                        val testRepository by inject<TestRepository>()
                        val user = testRepository.getUser(TestConstants.TEST_USER_EMAIL)
                        assertEquals("username1", user.username)
                    }
                }
            }
        }
    }
}