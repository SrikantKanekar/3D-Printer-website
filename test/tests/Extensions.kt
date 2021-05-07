package tests

import com.example.features.account.data.AccountRepository
import com.example.features.`object`.data.ObjectRepository
import com.example.features.myObjects.domain.ObjectsCookie
import data.Constants.TEST_CREATED_OBJECT
import data.Constants.TEST_FILE_UPLOAD_NAME
import data.Constants.TEST_USER_EMAIL
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.testing.*
import io.ktor.sessions.*
import io.ktor.utils.io.streams.*
import kotlinx.coroutines.runBlocking
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

val testUploadFile = PartData.FileItem(
    { byteArrayOf(1, 2, 3).inputStream().asInput() },
    { },
    headersOf(
        HttpHeaders.ContentDisposition,
        ContentDisposition.Inline
            .withParameter(ContentDisposition.Parameters.FileName, TEST_FILE_UPLOAD_NAME)
            .toString()
    )
)

fun insertNewFileAndConfirm(id: String) {
    val file = File("uploads/$id")
    file.createNewFile()
    assertTrue { file.exists() }
}

fun confirmAndDeleteFile(id: String) {
    val file = File("uploads/$id")
    assertTrue { file.exists() }
    file.delete()
    assertFalse { file.exists() }
}

fun TestApplicationEngine.`create object without user login`(objectRepository: ObjectRepository) {
    handleRequest(HttpMethod.Post, "/order/create") {
        val boundary = "***bbb***"
        addHeader(
            HttpHeaders.ContentType,
            ContentType.MultiPart.FormData.withParameter("boundary", boundary).toString()
        )
        setBody(boundary, listOf(testUploadFile))
    }.apply {
        runBlocking {
            val cookie = response.call.sessions.get<ObjectsCookie>()!!
            assertNotNull(cookie.objects.find { it.id == TEST_CREATED_OBJECT })

            val order = objectRepository.getUserObject(TEST_USER_EMAIL, TEST_CREATED_OBJECT)!!
            assertEquals(TEST_FILE_UPLOAD_NAME, order.fileName)

            assertEquals(HttpStatusCode.Found, response.status())
        }
    }
}

// must be called inside runWithTestUser
fun TestApplicationEngine.`create object after user login`(
    accountRepository: AccountRepository,
    objectRepository: ObjectRepository
) {
    handleRequest(HttpMethod.Post, "/order/create") {
        val boundary = "***bbb***"
        addHeader(
            HttpHeaders.ContentType,
            ContentType.MultiPart.FormData.withParameter("boundary", boundary).toString()
        )
        setBody(boundary, listOf(testUploadFile))
    }.apply {
        runBlocking {
            val wishlist = accountRepository.getUser(TEST_USER_EMAIL).objects.map { it.id }
            assertTrue { wishlist.contains(TEST_CREATED_OBJECT) }

            val order = objectRepository.getUserObject(TEST_USER_EMAIL, TEST_CREATED_OBJECT)!!
            assertEquals(TEST_FILE_UPLOAD_NAME, order.fileName)

            assertEquals(HttpStatusCode.Found, response.status())
        }
    }
}