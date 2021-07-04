package tests

import com.example.features.`object`.domain.ObjectStatus.NONE
import com.example.features.account.data.AccountRepository
import com.example.features.admin.domain.AdminPrincipal
import com.example.features.auth.domain.Constants.EMAIL_PASSWORD_INCORRECT
import com.example.features.auth.domain.UserPrincipal
import com.example.features.objects.domain.ObjectsCookie
import data.TestConstants.TEST_CREATED_OBJECT
import data.TestConstants.TEST_FILE_UPLOAD_NAME
import data.TestConstants.TEST_UPLOAD_FILE_CONTENT
import data.TestConstants.TEST_USER_EMAIL
import data.TestConstants.TEST_USER_PASSWORD
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.testing.*
import io.ktor.sessions.*
import io.ktor.utils.io.streams.*
import kotlinx.coroutines.runBlocking
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull

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

val formUrlEncoded = ContentType.Application.FormUrlEncoded.toString()

val multiPart = ContentType.MultiPart.FormData
    .withParameter("boundary", "boundary")
    .toString()

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
            assertEquals(HttpStatusCode.OK, response.status())
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
            assertEquals(HttpStatusCode.OK, response.status())
        }
    }
}