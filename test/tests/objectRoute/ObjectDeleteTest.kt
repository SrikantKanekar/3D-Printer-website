package tests.objectRoute

import com.example.features.objects.domain.ObjectsCookie
import com.example.module
import data.TestConstants
import di.testModules
import fakeDataSource.TestRepository
import io.ktor.http.*
import io.ktor.server.testing.*
import io.ktor.sessions.*
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject
import tests.`create object before user login`
import tests.runWithLoggedUser
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ObjectDeleteTest: KoinTest {

    @Test
    fun `delete user object before login success`() {
        withTestApplication({ module(testing = true, koinModules = testModules) }) {
            cookiesSession {
                `create object before user login`()
                handleRequest(HttpMethod.Get, "/objects/${TestConstants.TEST_CREATED_OBJECT}/delete").apply {
                    runBlocking {
                        val cookie = response.call.sessions.get<ObjectsCookie>()!!
                        assertNull(cookie.objects.find { it.id == TestConstants.TEST_CREATED_OBJECT })
                        assertEquals(HttpStatusCode.Found, response.status())
                    }
                }
            }
        }
    }

    @Test
    fun `delete user object after login success`() {
        withTestApplication({ module(testing = true, koinModules = testModules) }) {
            runWithLoggedUser {
                //`create object after user login`(accountRepository)
                handleRequest(HttpMethod.Get, "/objects/${TestConstants.TEST_CREATED_OBJECT}/delete").apply {
                    runBlocking {
                        val testRepository by inject<TestRepository>()
                        val obj = testRepository.getUserObject(TestConstants.TEST_USER_EMAIL, TestConstants.TEST_CREATED_OBJECT)
                        assertNull(obj)
                        assertEquals(HttpStatusCode.Found, response.status())
                    }
                }
            }
        }
    }

    @Test
    fun `delete user object before login invalid ID`() {
        withTestApplication({ module(testing = true, koinModules = testModules) }) {
            handleRequest(HttpMethod.Get, "/objects/invalid-object-id/delete").apply {
                assertEquals(HttpStatusCode.NotAcceptable, response.status())
            }
        }
    }

    @Test
    fun `delete user object after login invalid ID`() {
        withTestApplication({ module(testing = true, koinModules = testModules) }) {
            runWithLoggedUser {
                handleRequest(HttpMethod.Get, "/objects/invalid-object-id/delete").apply {
                    assertEquals(HttpStatusCode.NotAcceptable, response.status())
                }
            }
        }
    }

}