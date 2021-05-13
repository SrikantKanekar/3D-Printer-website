package tests.objectRoute

import com.example.features.account.data.AccountRepository
import com.example.module
import data.Constants.TEST_CREATED_OBJECT
import data.Constants.TEST_UPLOAD_FILE_CONTENT
import di.testModules
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject
import tests.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue

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
            assertFileNotNullAndDelete(TEST_CREATED_OBJECT)
        }
    }

    @Test
    fun `create object after user login Test`() {
        withTestApplication({ module(testing = true, koinModules = testModules) }) {
            runWithTestUser {
                `create object after user login`(accountRepository)
                assertTrue(readFileContent(TEST_CREATED_OBJECT).contentEquals(TEST_UPLOAD_FILE_CONTENT))
                assertFileNotNullAndDelete(TEST_CREATED_OBJECT)
            }
        }
    }
}
