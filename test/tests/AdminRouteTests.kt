package tests

import com.example.features.account.data.AccountRepository
import com.example.module
import di.testModule
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.assertEquals

class AdminRouteTests : KoinTest {

    val accountRepository by inject<AccountRepository>()

    @Test
    fun `get admin login route test`() {
        withTestApplication({ module(testing = true, koinModules = listOf(testModule)) }) {
            handleRequest(HttpMethod.Get, "/admin/login").apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }
        }
    }

    @Test
    fun `post admin login route test`() {
        withTestApplication({ module(testing = true, koinModules = listOf(testModule)) }) {
            adminLogin()
        }
    }

    @Test
    fun `get admin route test`() {
        withTestApplication({ module(testing = true, koinModules = listOf(testModule)) }) {
            handleRequest(HttpMethod.Get, "/admin").apply {
                assertEquals(HttpStatusCode.Unauthorized, response.status())
            }
            runWithAdminUser {
                handleRequest(HttpMethod.Get, "/admin").apply {
                    assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }

    @Test
    fun `update order status`() {
        withTestApplication({ module(testing = true, koinModules = listOf(testModule)) }) {
            runWithAdminUser {
                handleRequest(HttpMethod.Post, "/admin/update/order-status") {
                    addHeader(HttpHeaders.ContentType, formUrlEncoded)
                    setBody(
                        listOf(
                            "Email" to "INVALID_EMAIL",
                            "Password" to "INVALID_PASSWORD"
                        ).formUrlEncode()
                    )
                }.apply {
                    //assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }
}