package tests

import com.example.features.account.data.AccountRepository
import com.example.features.`object`.domain.ObjectStatus.*
import com.example.features.admin.domain.AdminPrincipal
import com.example.features.auth.domain.Constants.EMAIL_PASSWORD_INCORRECT
import com.example.features.auth.domain.UserPrincipal
import com.example.features.userObject.domain.ObjectsCookie
import data.Constants.TEST_UPLOAD_FILE_CONTENT
import data.Constants.TEST_CREATED_OBJECT
import data.Constants.TEST_FILE_UPDATED_NAME
import data.Constants.TEST_FILE_UPLOAD_NAME
import data.Constants.TEST_UPDATED_FILE_CONTENT
import data.Constants.TEST_USER_EMAIL
import data.Constants.TEST_USER_PASSWORD
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.testing.*
import io.ktor.sessions.*
import io.ktor.utils.io.streams.*
import kotlinx.coroutines.runBlocking
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.test.*

val testUploadFile = PartData.FileItem(
    { TEST_UPLOAD_FILE_CONTENT.inputStream().asInput() },
    { },
    headersOf(
        HttpHeaders.ContentDisposition,
        ContentDisposition.Inline
            .withParameter(ContentDisposition.Parameters.FileName, TEST_FILE_UPLOAD_NAME)
            .toString()
    )
)

val testUpdateFile = PartData.FileItem(
    { TEST_UPDATED_FILE_CONTENT.inputStream().asInput() },
    { },
    headersOf(
        HttpHeaders.ContentDisposition,
        ContentDisposition.Inline
            .withParameter(ContentDisposition.Parameters.FileName, TEST_FILE_UPDATED_NAME)
            .toString()
    )
)

val formUrlEncoded = ContentType.Application.FormUrlEncoded.toString()

val multiPart = ContentType.MultiPart.FormData
    .withParameter("boundary", "boundary")
    .toString()

fun readFileContent(id: String): ByteArray {
    return Files.readAllBytes(Paths.get("uploads/$id"))
}

fun assertFileNotNullAndDelete(id: String) {
    val file = File("uploads/$id")
    assertTrue { file.exists() }
    file.delete()
    assertFalse { file.exists() }
}

fun assertFileNull(id: String) {
    val file = File("uploads/$id")
    assertFalse { file.exists() }
}

fun TestApplicationEngine.testUserLogin() {
    handleRequest(HttpMethod.Post, "/auth/login") {
        addHeader(HttpHeaders.ContentType, formUrlEncoded)
        setBody(
            listOf(
                "Email" to TEST_USER_EMAIL,
                "Password" to TEST_USER_PASSWORD
            ).formUrlEncode()
        )
    }.apply {
        val userPrincipal = response.call.sessions.get<UserPrincipal>()!!
        assertEquals(TEST_USER_EMAIL, userPrincipal.email)

        assertNotEquals(EMAIL_PASSWORD_INCORRECT, response.content)
        assertEquals(HttpStatusCode.OK, response.status())
    }
}

fun TestApplicationEngine.adminLogin() {
    handleRequest(HttpMethod.Post, "/admin/login") {
        addHeader(HttpHeaders.ContentType, formUrlEncoded)
        setBody(
            listOf(
                "name" to "admin",
                "Password" to "password"
            ).formUrlEncode()
        )
    }.apply {
        val adminPrincipal = response.call.sessions.get<AdminPrincipal>()!!
        assertEquals("admin", adminPrincipal.name)
        assertEquals(HttpStatusCode.OK, response.status())
    }
}

fun TestApplicationEngine.runWithTestUser(test: TestApplicationEngine.() -> Unit) {
    cookiesSession {
        testUserLogin()
        test()
    }
}

fun TestApplicationEngine.runWithAdminUser(test: TestApplicationEngine.() -> Unit) {
    cookiesSession {
        adminLogin()
        test()
    }
}

fun TestApplicationEngine.`create object before user login`() {
    handleRequest(HttpMethod.Post, "/object/create") {
        addHeader(HttpHeaders.ContentType, multiPart)
        setBody("boundary", listOf(testUploadFile))
    }.apply {
        runBlocking {
            val cookie = response.call.sessions.get<ObjectsCookie>()!!
            assertNotNull(cookie.objects.find { it.id == TEST_CREATED_OBJECT })
            assertEquals(HttpStatusCode.Found, response.status())
        }
    }
}

// must be called inside runWithTestUser
fun TestApplicationEngine.`create object after user login`(
    accountRepository: AccountRepository
) {
    handleRequest(HttpMethod.Post, "/object/create") {
        addHeader(HttpHeaders.ContentType, multiPart)
        setBody("boundary", listOf(testUploadFile))
    }.apply {
        runBlocking {
            val obj = accountRepository.getUser(TEST_USER_EMAIL).objects
                .filter { it.status == NONE }
                .find { it.id == TEST_CREATED_OBJECT }
            assertNotNull(obj)
            assertEquals(HttpStatusCode.Found, response.status())
        }
    }
}