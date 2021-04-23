package com.example

import com.example.data.database.Constants.TEST_USER_NAME
import com.example.data.database.Constants.TEST_USER_PASSWORD
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest {

    @Test
    fun testRoot() {
        withTestApplication({ module(testing = true) }) {
            handleRequest(HttpMethod.Get, "/hello-world").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals("HELLO WORLD!", response.content)
            }
        }
    }

    @Test
    fun loginTest() {
        withTestApplication({ module(testing = true) }) {
            loginTestAccount()
        }
    }

    @Test
    fun meData_noAuth_Unauthorized() {
        withTestApplication({ module(testing = true) }) {
            handleRequest(HttpMethod.Get, "/user_info").apply {
                assertEquals(HttpStatusCode.Unauthorized, response.status())
            }
        }
    }

    @Test
    fun meData_hasAuth_ReturnsOkAndCurrentUserData() {
        withTestApplication({ module(testing = true) }) {
            runWithTestUser {
                handleRequest(HttpMethod.Get, "/user_info").apply {
                    assertEquals(HttpStatusCode.OK, response.status())

                    val expectedContent = """{"name":"test_user_name"}"""
                    assertEquals(expectedContent, response.content)
                }
            }
        }
    }

    private fun TestApplicationEngine.runWithTestUser(test: TestApplicationEngine.() -> Unit) =
        cookiesSession {
            loginTestAccount()
            test()
        }

    private fun TestApplicationEngine.loginTestAccount() {
        handleRequest(HttpMethod.Post, "/login") {
            addHeader(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded.toString())
            setBody(
                listOf(
                    "Username" to TEST_USER_NAME,
                    "Password" to TEST_USER_PASSWORD
                ).formUrlEncode()
            )
        }.apply {
            assertEquals(HttpStatusCode.OK, response.status())
            assertEquals("Your are now logged in!", response.content)
        }
    }
}
