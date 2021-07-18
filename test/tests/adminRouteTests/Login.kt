package tests.adminRouteTests

import com.example.features.admin.domain.AdminPrincipal
import com.example.features.auth.domain.AuthConstants.EMAIL_PASSWORD_INCORRECT
import io.ktor.http.*
import io.ktor.sessions.*
import org.junit.Test
import org.koin.test.KoinTest
import tests.*
import kotlin.test.assertEquals
import kotlin.test.assertNull

class Login : KoinTest {

    @Test
    fun `should return Ok if not logged`() {
        runServer {
            handleGetRequest("/admin/login") {
                assertEquals(HttpStatusCode.OK, response.status())
            }
        }
    }

    @Test
    fun `should return ok if not admin user`() {
        runServer {
            runWithLoggedUser {
                handleGetRequest("/admin/login") {
                    assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }

    @Test
    fun `should redirect if admin user`() {
        runServer {
            runWithAdminUser {
                handleGetRequest("/admin/login") {
                    assertEquals(HttpStatusCode.Found, response.status())
                }
            }
        }
    }

    @Test
    fun `should return error if invalid credentials`() {
        runServer {
            handlePostRequest(
                "/admin/login",
                mapOf(
                    "name" to "1111",
                    "Password" to "1111"
                )
            ) {
                val adminPrincipal = response.call.sessions.get<AdminPrincipal>()
                assertNull(adminPrincipal)

                assertEquals(EMAIL_PASSWORD_INCORRECT, response.content)
            }
        }
    }

    @Test
    fun `should have adminPrincipal id success`() {
        runServer {
            adminLogin()
        }
    }
}