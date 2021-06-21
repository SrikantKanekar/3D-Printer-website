package tests.objectRoute

import com.example.features.account.data.AccountRepository
import com.example.module
import di.testModules
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject
import tests.`create object after user login`
import tests.`create object before user login`
import tests.runWithTestUser
import kotlin.test.assertEquals

class ObjectCreateTest : KoinTest {

    private val accountRepository by inject<AccountRepository>()

    @Test
    fun `get create object route Test`() {
        withTestApplication({ module(testing = true, koinModules = testModules) }) {
            handleRequest(HttpMethod.Get, "/object/create").apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }
            runWithTestUser {
                handleRequest(HttpMethod.Get, "/object/create").apply {
                    assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }

    @Test
    fun `create object before login Test`() {
        withTestApplication({ module(testing = true, koinModules = testModules) }) {
            `create object before user login`()
        }
    }

    @Test
    fun `create object after user login Test`() {
        withTestApplication({ module(testing = true, koinModules = testModules) }) {
            runWithTestUser {
                `create object after user login`(accountRepository)
            }
        }
    }
}
