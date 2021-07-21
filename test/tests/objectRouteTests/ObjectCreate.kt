package tests.objectRouteTests

import com.example.features.`object`.requests.ObjectCreateRequest
import com.example.model.Object
import data.TestConstants.TEST_CREATED_OBJECT
import data.TestConstants.TEST_USER_EMAIL
import fakeDataSource.TestRepository
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject
import tests.`create object before user login`
import tests.handlePostRequest
import tests.runServer
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class ObjectCreate : KoinTest {

    @Test
    fun `should return object if user is not logged`() {
        runServer {
            `create object before user login`()
        }
    }

    @Test
    fun `should return object if user is logged`() {
        runServer {
            handlePostRequest(
                "/objects",
                ObjectCreateRequest(
                    id = TEST_CREATED_OBJECT,
                    name = "name",
                    fileUrl = "file_url",
                    imageUrl = "image_url",
                    fileExtension = "stl"
                ),
                logged = true
            ) {
                runBlocking {
                    val testRepository by inject<TestRepository>()
                    val obj = testRepository
                        .getUser(TEST_USER_EMAIL)
                        .objects
                        .find { it.id == TEST_CREATED_OBJECT }
                    assertNotNull(obj)

                    val responseObj = Json.decodeFromString<Object>(response.content!!)
                    assertEquals(TEST_CREATED_OBJECT, obj.id)
                    assertEquals("name", responseObj.name)

                    assertEquals(HttpStatusCode.Created, response.status())
                }
            }
        }
    }
}
