package tests.objectRoute

import data.TestConstants
import data.TestConstants.TEST_CREATED_OBJECT
import fakeDataSource.TestRepository
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject
import tests.*
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class ObjectCreate : KoinTest {

    @Test
    fun `should return ok if user is not logged`() {
        runTest {
            handleGetRequest("/object/create") {
                assertEquals(HttpStatusCode.OK, response.status())
            }
        }
    }

    @Test
    fun `should return ok if user is logged`() {
        runTest {
            runWithLoggedUser {
                handleGetRequest("/object/create") {
                    assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }

    @Test
    fun `should return object Id if user is not logged`() {
        runTest {
            `create object before user login`()
        }
    }

    @Test
    fun `should return object Id if user is logged`() {
        runTest {
            runWithLoggedUser {
                handlePostRequest(
                    "/object/create",
                    listOf(
                        "id" to TEST_CREATED_OBJECT,
                        "name" to "name",
                        "file_url" to "file_url",
                        "image_url" to "image_url",
                        "file_extension" to "stl",
                    )
                ) {
                    runBlocking {
                        val testRepository by inject<TestRepository>()
                        val obj = testRepository
                            .getUser(TestConstants.TEST_USER_EMAIL)
                            .objects
                            .find { it.id == TEST_CREATED_OBJECT }
                        assertNotNull(obj)

                        assertEquals(TEST_CREATED_OBJECT, response.content)
                    }
                }
            }
        }
    }
}
