package feautures.account

import com.example.feautures.account.data.AccountRepository
import com.example.module
import com.example.util.checkHashForPassword
import data.Constants
import data.Constants.TEST_USER_EMAIL
import data.Constants.TEST_USER_PASSWORD
import data.Constants.UPDATED_USERNAME
import di.testAuthModule
import feautures.auth.runWithTestUser
import feautures.auth.testUserLogin
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertFalse

class AccountRouteTest: KoinTest {

    private val accountRepository by inject<AccountRepository>()

    @Test
    fun `access account data without login`() {
        withTestApplication({ module(testing = true, koinModules = listOf(testAuthModule)) }) {
            handleRequest(HttpMethod.Get, "/account").apply {
                assertEquals(HttpStatusCode.Unauthorized, response.status())
            }
        }
    }

    @Test
    fun `access account data after login`() {
        withTestApplication({ module(testing = true, koinModules = listOf(testAuthModule)) }) {
            runWithTestUser {
                handleRequest(HttpMethod.Get, "/account").apply {
                    assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }

    @Test
    fun `update username success`() {
        withTestApplication({ module(testing = true, koinModules = listOf(testAuthModule)) }) {
            runWithTestUser {
                handleRequest(HttpMethod.Post, "/account/update") {
                    addHeader(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded.toString())
                    setBody(
                        listOf(
                            "username" to UPDATED_USERNAME
                        ).formUrlEncode()
                    )
                }.apply {
                    runBlocking {
                        assertEquals(HttpStatusCode.OK, response.status())
                        assertEquals(UPDATED_USERNAME, accountRepository.getUser(TEST_USER_EMAIL)?.username)
                    }
                }
            }
        }
    }

    @Test
    fun `reset password failure password don't match`(){
        withTestApplication({ module(testing = true, koinModules = listOf(testAuthModule)) }) {
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
                    runBlocking {
                        assertEquals(HttpStatusCode.NotAcceptable, response.status())
                        assertFalse(
                            checkHashForPassword(
                                "abcd",
                                accountRepository.getUser(TEST_USER_EMAIL)!!.password
                            )
                        )
                    }
                }
            }
        }
    }

    @Test
    fun `reset password failure old password incorrect`(){
        withTestApplication({ module(testing = true, koinModules = listOf(testAuthModule)) }) {
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
                    runBlocking {
                        assertEquals(HttpStatusCode.NotAcceptable, response.status())
                        assertFalse(
                            checkHashForPassword(
                                "Invalid password",
                                accountRepository.getUser(TEST_USER_EMAIL)!!.password
                            )
                        )
                    }
                }
            }
        }
    }

    @Test
    fun `reset password success`(){
        withTestApplication({ module(testing = true, koinModules = listOf(testAuthModule)) }) {
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
                    runBlocking {
                        assertEquals(HttpStatusCode.OK, response.status())
                        assertFalse(
                            checkHashForPassword(
                                TEST_USER_PASSWORD,
                                accountRepository.getUser(TEST_USER_EMAIL)!!.password
                            )
                        )
                    }
                }
                assertFails { testUserLogin() }
            }
        }
    }

    @Test
    fun `logout success`() {
        withTestApplication({ module(testing = true, koinModules = listOf(testAuthModule)) }) {
            runWithTestUser {
                handleRequest(HttpMethod.Get, "/account/logout").apply {
                    assertEquals(HttpStatusCode.Found, response.status())
                }
            }
        }
    }
}