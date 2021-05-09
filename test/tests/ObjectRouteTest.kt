package tests

import com.example.features.account.data.AccountRepository
import com.example.features.`object`.data.ObjectRepository
import com.example.features.userObject.domain.ObjectsCookie
import com.example.module
import data.Constants.TEST_CREATED_OBJECT
import data.Constants.TEST_FILE_UPDATED_NAME
import data.Constants.TEST_USER_EMAIL
import data.Constants.TEST_USER_OBJECT
import data.Constants.TEST_UPDATED_FILE_CONTENT
import data.Constants.TEST_UPLOAD_FILE_CONTENT
import di.testModule
import io.ktor.http.*
import io.ktor.server.testing.*
import io.ktor.sessions.*
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.*

class ObjectRouteTest : KoinTest {

    private val objectRepository by inject<ObjectRepository>()
    private val accountRepository by inject<AccountRepository>()

    @Test
    fun `get create object route with or without login`() {
        withTestApplication({ module(testing = true, koinModules = listOf(testModule)) }) {
            handleRequest(HttpMethod.Get, "/object").apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }
            runWithTestUser {
                handleRequest(HttpMethod.Get, "/object").apply {
                    assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }

    @Test
    fun `create object without login check cookie`() {
        withTestApplication({ module(testing = true, koinModules = listOf(testModule)) }) {
            `create object before user login`()
            assertFileNotNullAndDelete(TEST_CREATED_OBJECT)
        }
    }

    @Test
    fun `create object after user login success`() {
        withTestApplication({ module(testing = true, koinModules = listOf(testModule)) }) {
            runWithTestUser {
                `create object after user login`(accountRepository)
                assertTrue(readFileContent(TEST_CREATED_OBJECT).contentEquals(TEST_UPLOAD_FILE_CONTENT))
                assertFileNotNullAndDelete(TEST_CREATED_OBJECT)
            }
        }
    }

    @Test
    fun `get update object route before login`() {
        withTestApplication({ module(testing = true, koinModules = listOf(testModule)) }) {
            cookiesSession {
                `create object before user login`()
                handleRequest(HttpMethod.Get, "/object/$TEST_CREATED_OBJECT").apply {
                    assertEquals(HttpStatusCode.OK, response.status())
                    assertTrue(readFileContent(TEST_CREATED_OBJECT).contentEquals(TEST_UPLOAD_FILE_CONTENT))
                    assertFileNotNullAndDelete(TEST_CREATED_OBJECT)
                }
                handleRequest(HttpMethod.Get, "/object/invalid-object-id").apply {
                    assertEquals(HttpStatusCode.NotAcceptable, response.status())
                }
            }
        }
    }

    @Test
    fun `get update object route after login`() {
        withTestApplication({ module(testing = true, koinModules = listOf(testModule)) }) {
            runWithTestUser {
                handleRequest(HttpMethod.Get, "/object/$TEST_USER_OBJECT").apply {
                    assertEquals(HttpStatusCode.OK, response.status())
                }
                handleRequest(HttpMethod.Get, "/object/invalid-object-id").apply {
                    assertEquals(HttpStatusCode.NotAcceptable, response.status())
                }
            }
        }
    }

    @Test
    fun `update object file after login success`() {
        withTestApplication({ module(testing = true, koinModules = listOf(testModule)) }) {
            runWithTestUser {
                `create object after user login`(accountRepository)
                handleRequest(HttpMethod.Post, "/object/$TEST_CREATED_OBJECT/file") {
                    addHeader(HttpHeaders.ContentType, multiPart)
                    setBody("boundary", listOf(testUpdateFile))
                }.apply {
                    runBlocking {
                        val obj = objectRepository.getUserObject(TEST_USER_EMAIL, TEST_CREATED_OBJECT)!!
                        assertEquals(TEST_FILE_UPDATED_NAME, obj.fileName)
                        assertTrue(readFileContent(TEST_CREATED_OBJECT).contentEquals(TEST_UPDATED_FILE_CONTENT))
                        assertFileNotNullAndDelete(TEST_CREATED_OBJECT)
                    }
                }
            }
        }
    }

    @Test
    fun `update object file after login invalid ID`() {
        withTestApplication({ module(testing = true, koinModules = listOf(testModule)) }) {
            runWithTestUser {
                handleRequest(HttpMethod.Post, "/object/invalid-object-id/file") {
                    addHeader(HttpHeaders.ContentType, multiPart)
                    setBody("boundary", listOf(testUpdateFile))
                }.apply {
                    assertEquals(HttpStatusCode.NotAcceptable, response.status())
                }
            }
        }
    }

    @Test
    fun `update object file before login success`() {
        withTestApplication({ module(testing = true, koinModules = listOf(testModule)) }) {
            cookiesSession {
                `create object before user login`()
                handleRequest(HttpMethod.Post, "/object/$TEST_CREATED_OBJECT/file") {
                    addHeader(HttpHeaders.ContentType, multiPart)
                    setBody("boundary", listOf(testUpdateFile))
                }.apply {
                    runBlocking {
                        val cookie = response.call.sessions.get<ObjectsCookie>()!!
                        val obj = cookie.objects.find { it.id == TEST_CREATED_OBJECT }!!
                        assertEquals(TEST_FILE_UPDATED_NAME, obj.fileName)
                        assertTrue(readFileContent(TEST_CREATED_OBJECT).contentEquals(TEST_UPDATED_FILE_CONTENT))
                        assertFileNotNullAndDelete(TEST_CREATED_OBJECT)
                    }
                }
            }
        }
    }

    @Test
    fun `update object file before login invalid ID`() {
        withTestApplication({ module(testing = true, koinModules = listOf(testModule)) }) {
            handleRequest(HttpMethod.Post, "/object/invalid-object-id/file") {
                addHeader(HttpHeaders.ContentType, multiPart)
                setBody("boundary", listOf(testUpdateFile))
            }.apply {
                assertEquals(HttpStatusCode.NotAcceptable, response.status())
            }
        }
    }

    @Test
    fun `update basic settings before login success`() {
        withTestApplication({ module(testing = true, koinModules = listOf(testModule)) }) {
            cookiesSession {
                `create object before user login`()
                handleRequest(HttpMethod.Post, "/object/$TEST_CREATED_OBJECT/basic") {
                    addHeader(HttpHeaders.ContentType, formUrlEncoded)
                    setBody(listOf("size" to "100").formUrlEncode())
                }.apply {
                    runBlocking {
                        val cookie = response.call.sessions.get<ObjectsCookie>()!!
                        val obj = cookie.objects.find { it.id == TEST_CREATED_OBJECT }!!
                        assertEquals(100, obj.basicSettings.size)
                    }
                }
            }
        }
    }

    @Test
    fun `update basic settings before login invalid ID`() {
        withTestApplication({ module(testing = true, koinModules = listOf(testModule)) }) {
            handleRequest(HttpMethod.Post, "/object/invalid-object-id/basic") {
                addHeader(HttpHeaders.ContentType, formUrlEncoded)
                setBody(listOf("size" to "100").formUrlEncode())
            }.apply {
                assertEquals(HttpStatusCode.NotAcceptable, response.status())
            }
        }
    }

    @Test
    fun `update basic settings after login success`() {
        withTestApplication({ module(testing = true, koinModules = listOf(testModule)) }) {
            runWithTestUser {
                handleRequest(HttpMethod.Post, "/object/$TEST_USER_OBJECT/basic") {
                    addHeader(HttpHeaders.ContentType, formUrlEncoded)
                    setBody(listOf("size" to "100").formUrlEncode())
                }.apply {
                    runBlocking {
                        val obj = objectRepository.getUserObject(TEST_USER_EMAIL, TEST_USER_OBJECT)!!
                        assertEquals(100, obj.basicSettings.size)
                    }
                }
            }
        }
    }

    @Test
    fun `update basic settings after login invalid ID`() {
        withTestApplication({ module(testing = true, koinModules = listOf(testModule)) }) {
            runWithTestUser {
                handleRequest(HttpMethod.Post, "/object/invalid-object-id/basic") {
                    addHeader(HttpHeaders.ContentType, formUrlEncoded)
                    setBody(listOf("size" to "100").formUrlEncode())
                }.apply {
                    assertEquals(HttpStatusCode.NotAcceptable, response.status())
                }
            }
        }
    }

    @Test
    fun `update advanced settings before login success`() {
        withTestApplication({ module(testing = true, koinModules = listOf(testModule)) }) {
            cookiesSession {
                `create object before user login`()
                handleRequest(HttpMethod.Post, "/object/$TEST_CREATED_OBJECT/advanced") {
                    addHeader(HttpHeaders.ContentType, formUrlEncoded)
                    setBody(listOf("weight" to "100").formUrlEncode())
                }.apply {
                    runBlocking {
                        val cookie = response.call.sessions.get<ObjectsCookie>()!!
                        val obj = cookie.objects.find { it.id == TEST_CREATED_OBJECT }!!
                        assertEquals(100, obj.advancedSettings.weight)
                    }
                }
            }
        }
    }

    @Test
    fun `update advanced settings before login invalid ID`() {
        withTestApplication({ module(testing = true, koinModules = listOf(testModule)) }) {
            handleRequest(HttpMethod.Post, "/object/invalid-object-id/advanced") {
                addHeader(HttpHeaders.ContentType, formUrlEncoded)
                setBody(listOf("weight" to "100").formUrlEncode())
            }.apply {
                assertEquals(HttpStatusCode.NotAcceptable, response.status())
            }
        }
    }

    @Test
    fun `update advanced settings after login success`() {
        withTestApplication({ module(testing = true, koinModules = listOf(testModule)) }) {
            runWithTestUser {
                handleRequest(HttpMethod.Post, "/object/$TEST_USER_OBJECT/advanced") {
                    addHeader(HttpHeaders.ContentType, formUrlEncoded)
                    setBody(listOf("weight" to "100").formUrlEncode())
                }.apply {
                    runBlocking {
                        val obj = objectRepository.getUserObject(TEST_USER_EMAIL, TEST_USER_OBJECT)!!
                        assertEquals(100, obj.advancedSettings.weight)
                    }
                }
            }
        }
    }

    @Test
    fun `update advanced settings after login invalid ID`() {
        withTestApplication({ module(testing = true, koinModules = listOf(testModule)) }) {
            runWithTestUser {
                handleRequest(HttpMethod.Post, "/object/invalid-object-id/advanced") {
                    addHeader(HttpHeaders.ContentType, formUrlEncoded)
                    setBody(listOf("weight" to "100").formUrlEncode())
                }.apply {
                    assertEquals(HttpStatusCode.NotAcceptable, response.status())
                }
            }
        }
    }
}
