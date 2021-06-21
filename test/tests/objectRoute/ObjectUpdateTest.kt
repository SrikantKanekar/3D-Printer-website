package tests.objectRoute

import com.example.features.`object`.data.ObjectRepository
import com.example.features.objects.domain.ObjectsCookie
import com.example.module
import data.Constants
import data.Constants.TEST_CREATED_OBJECT
import data.Constants.TEST_USER_EMAIL
import data.Constants.TEST_USER_OBJECT
import di.testModules
import io.ktor.http.*
import io.ktor.server.testing.*
import io.ktor.sessions.*
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject
import tests.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ObjectUpdateTest : KoinTest {

    private val objectRepository by inject<ObjectRepository>()

    @Test
    fun `get update object route before login`() {
        withTestApplication({ module(testing = true, koinModules = testModules) }) {
            cookiesSession {
                `create object before user login`()
                handleRequest(HttpMethod.Get, "/object/$TEST_CREATED_OBJECT").apply {
                    assertEquals(HttpStatusCode.OK, response.status())
                }
                handleRequest(HttpMethod.Get, "/object/invalid-object-id").apply {
                    assertEquals(HttpStatusCode.NotFound, response.status())
                }
            }
        }
    }

    @Test
    fun `get update object route after login`() {
        withTestApplication({ module(testing = true, koinModules = testModules) }) {
            runWithTestUser {
                handleRequest(HttpMethod.Get, "/object/$TEST_USER_OBJECT").apply {
                    assertEquals(HttpStatusCode.OK, response.status())
                }
                handleRequest(HttpMethod.Get, "/object/invalid-object-id").apply {
                    assertEquals(HttpStatusCode.NotFound, response.status())
                }
            }
        }
    }

//    @Test
//    fun `update basic settings before login success`() {
//        withTestApplication({ module(testing = true, koinModules = testModules) }) {
//            cookiesSession {
//                `create object before user login`()
//                handleRequest(HttpMethod.Post, "/object/$TEST_CREATED_OBJECT/basic") {
//                    addHeader(HttpHeaders.ContentType, formUrlEncoded)
//                    setBody(listOf("layer_height" to "0.3").formUrlEncode())
//                }.apply {
//                    runBlocking {
//                        val cookie = response.call.sessions.get<ObjectsCookie>()!!
//                        val obj = cookie.objects.find { it.id == TEST_CREATED_OBJECT }!!
//                        assertEquals(0.3F, obj.intermediateSetting.layerHeight)
//                    }
//                }
//            }
//        }
//    }

    @Test
    fun `update basic settings before login invalid ID`() {
        withTestApplication({ module(testing = true, koinModules = testModules) }) {
            handleRequest(HttpMethod.Post, "/object/invalid-object-id/basic") {
                addHeader(HttpHeaders.ContentType, formUrlEncoded)
                setBody(listOf("size" to "100").formUrlEncode())
            }.apply {
                assertEquals("false", response.content)
            }
        }
    }

//    @Test
//    fun `update basic settings after login success`() {
//        withTestApplication({ module(testing = true, koinModules = testModules) }) {
//            runWithTestUser {
//                handleRequest(HttpMethod.Post, "/object/$TEST_USER_OBJECT/basic") {
//                    addHeader(HttpHeaders.ContentType, formUrlEncoded)
//                    setBody(listOf("layer_height" to "0.1").formUrlEncode())
//                }.apply {
//                    runBlocking {
//                        val obj = objectRepository.getUserObject(TEST_USER_EMAIL, TEST_USER_OBJECT)!!
//                        assertEquals(0.1F, obj.intermediateSetting.layerHeight)
//                    }
//                }
//            }
//        }
//    }

    @Test
    fun `update basic settings after login invalid ID`() {
        withTestApplication({ module(testing = true, koinModules = testModules) }) {
            runWithTestUser {
                handleRequest(HttpMethod.Post, "/object/invalid-object-id/basic") {
                    addHeader(HttpHeaders.ContentType, formUrlEncoded)
                    setBody(listOf("size" to "100").formUrlEncode())
                }.apply {
                    assertEquals("false", response.content)
                }
            }
        }
    }

//    @Test
//    fun `update advanced settings before login success`() {
//        withTestApplication({ module(testing = true, koinModules = testModules) }) {
//            cookiesSession {
//                `create object before user login`()
//                handleRequest(HttpMethod.Post, "/object/$TEST_CREATED_OBJECT/advanced") {
//                    addHeader(HttpHeaders.ContentType, formUrlEncoded)
//                    setBody(listOf("weight" to "100").formUrlEncode())
//                }.apply {
//                    runBlocking {
//                        val cookie = response.call.sessions.get<ObjectsCookie>()!!
//                        val obj = cookie.objects.find { it.id == TEST_CREATED_OBJECT }!!
//                        assertEquals(100, obj.advancedSetting.weight)
//                    }
//                }
//            }
//        }
//    }

    @Test
    fun `update advanced settings before login invalid ID`() {
        withTestApplication({ module(testing = true, koinModules = testModules) }) {
            handleRequest(HttpMethod.Post, "/object/invalid-object-id/advanced") {
                addHeader(HttpHeaders.ContentType, formUrlEncoded)
                setBody(listOf("weight" to "100").formUrlEncode())
            }.apply {
                assertEquals("false", response.content)
            }
        }
    }

//    @Test
//    fun `update advanced settings after login success`() {
//        withTestApplication({ module(testing = true, koinModules = testModules) }) {
//            runWithTestUser {
//                handleRequest(HttpMethod.Post, "/object/$TEST_USER_OBJECT/advanced") {
//                    addHeader(HttpHeaders.ContentType, formUrlEncoded)
//                    setBody(listOf("weight" to "100").formUrlEncode())
//                }.apply {
//                    runBlocking {
//                        val obj = objectRepository.getUserObject(TEST_USER_EMAIL, TEST_USER_OBJECT)!!
//                        assertEquals(100, obj.advancedSetting.weight)
//                    }
//                }
//            }
//        }
//    }

    @Test
    fun `update advanced settings after login invalid ID`() {
        withTestApplication({ module(testing = true, koinModules = testModules) }) {
            runWithTestUser {
                handleRequest(HttpMethod.Post, "/object/invalid-object-id/advanced") {
                    addHeader(HttpHeaders.ContentType, formUrlEncoded)
                    setBody(listOf("weight" to "100").formUrlEncode())
                }.apply {
                    assertEquals("false", response.content)
                }
            }
        }
    }
}