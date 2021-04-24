package com.example.feautures.auth

import com.example.data.Constants.TEST_USER_EMAIL
import com.example.data.Constants.TEST_USER_PASSWORD
import com.example.di.testAuthModule
import com.example.module
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.Test
import kotlin.test.assertEquals

class AuthRouteTest {

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
                assertEquals(HttpStatusCode.NotAcceptable, response.status())
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
                        "Password" to "NEW_PASSWORD"
                    ).formUrlEncode()
                )
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
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
                        "Password" to TEST_USER_PASSWORD
                    ).formUrlEncode()
                )
            }.apply {
                assertEquals(HttpStatusCode.Conflict, response.status())
            }
        }
    }

    @Test
    fun `access account data without login`() {
        withTestApplication({ module(testing = true, koinModules = listOf(testAuthModule)) }) {
            handleRequest(HttpMethod.Get, "/user_info").apply {
                assertEquals(HttpStatusCode.Unauthorized, response.status())
            }
        }
    }

    @Test
    fun `access account data after login`() {
        withTestApplication({ module(testing = true, koinModules = listOf(testAuthModule)) }) {
            runWithTestUser {
                handleRequest(HttpMethod.Get, "/user_info").apply {
                    assertEquals(HttpStatusCode.OK, response.status())
                    assertEquals("""{"email":"$TEST_USER_EMAIL"}""", response.content)
                }
            }
        }
    }
}

// Login with a registered user
private fun TestApplicationEngine.testUserLogin() {
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
    }
}

private fun TestApplicationEngine.runWithTestUser(test: TestApplicationEngine.() -> Unit) =
    cookiesSession {
        testUserLogin()
        test()
    }