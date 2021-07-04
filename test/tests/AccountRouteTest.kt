package tests

import com.example.features.account.domain.Constants
import com.example.features.account.domain.Constants.INCORRECT_PASSWORD
import com.example.features.auth.domain.checkHashForPassword
import com.example.module
import data.TestConstants.TEST_USER_EMAIL
import data.TestConstants.TEST_USER_PASSWORD
import di.testModules
import fakeDataSource.TestRepository
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertFalse

class AccountRouteTest : KoinTest {

    @Test
    fun `trial test`() {
        withTestApplication({ module(testing = true, koinModules = testModules) }) {

        }
    }

    @Test
    fun `get account route test`() {
        withTestApplication({ module(testing = true, koinModules = testModules) }) {
            handleRequest(HttpMethod.Get, "/account").apply {
                assertEquals(HttpStatusCode.Unauthorized, response.status())
            }
            runWithTestUser {
                handleRequest(HttpMethod.Get, "/account").apply {
                    assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }

    @Test
    fun `update username success`() {
        withTestApplication({ module(testing = true, koinModules = testModules) }) {
            runWithTestUser {
                handleRequest(HttpMethod.Post, "/account/update") {
                    addHeader(HttpHeaders.ContentType, formUrlEncoded)
                    setBody(
                        listOf(
                            "username" to "UPDATED_USERNAME"
                        ).formUrlEncode()
                    )
                }.apply {
                    runBlocking {
                        assertEquals(HttpStatusCode.OK, response.status())

                        val testRepository by inject<TestRepository>()
                        val user = testRepository.getUser(TEST_USER_EMAIL)
                        assertEquals("UPDATED_USERNAME", user.username)
                    }
                }
            }
        }
    }

    @Test
    fun `reset password failure password don't match`() {
        withTestApplication({ module(testing = true, koinModules = testModules) }) {
            runWithTestUser {
                handleRequest(HttpMethod.Post, "/account/reset-password") {
                    addHeader(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded.toString())
                    setBody(
                        listOf(
                            "old_password" to TEST_USER_PASSWORD,
                            "new_password" to "abcd",
                            "confirm_password" to "1234",
                        ).formUrlEncode()
                    )
                }.apply {
                    assertEquals(Constants.PASSWORD_DO_NOT_MATCH, response.content)
                }
            }
        }
    }

    @Test
    fun `reset password failure old password incorrect`() {
        withTestApplication({ module(testing = true, koinModules = testModules) }) {
            runWithTestUser {
                handleRequest(HttpMethod.Post, "/account/reset-password") {
                    addHeader(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded.toString())
                    setBody(
                        listOf(
                            "old_password" to "Invalid password",
                            "new_password" to "abcd",
                            "confirm_password" to "abcd",
                        ).formUrlEncode()
                    )
                }.apply {
                    assertEquals(INCORRECT_PASSWORD, response.content)
                }
            }
        }
    }

    @Test
    fun `reset password success`() {
        withTestApplication({ module(testing = true, koinModules = testModules) }) {
            runWithTestUser {
                handleRequest(HttpMethod.Post, "/account/reset-password") {
                    addHeader(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded.toString())
                    setBody(
                        listOf(
                            "old_password" to TEST_USER_PASSWORD,
                            "new_password" to "abcd",
                            "confirm_password" to "abcd",
                        ).formUrlEncode()
                    )
                }.apply {
                    assertEquals(HttpStatusCode.OK, response.status())
                    runBlocking {
                        val testRepository by inject<TestRepository>()
                        val user = testRepository.getUser(TEST_USER_EMAIL)
                        assertFalse(checkHashForPassword(TEST_USER_PASSWORD, user.password))
                    }
                }
                assertFails { testUserLogin() }
            }
        }
    }

    @Test
    fun `logout success`() {
        withTestApplication({ module(testing = true, koinModules = testModules) }) {
            runWithTestUser {
                handleRequest(HttpMethod.Get, "/account/logout").apply {
                    assertEquals(HttpStatusCode.Found, response.status())
                }
            }
        }
    }
}