package tests

import com.example.features.admin.domain.AdminPrincipal
import com.example.features.auth.domain.AuthConstants.EMAIL_PASSWORD_INCORRECT
import com.example.features.auth.domain.UserPrincipal
import com.example.features.objects.domain.ObjectsCookie
import com.example.module
import data.TestConstants.TEST_CREATED_OBJECT
import data.TestConstants.TEST_USER_EMAIL
import data.TestConstants.TEST_USER_PASSWORD
import di.testModules
import io.ktor.config.*
import io.ktor.http.*
import io.ktor.server.testing.*
import io.ktor.sessions.*
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull

fun MapApplicationConfig.configForTesting() {
    put("ktor.jwt.secret", "123456")
    put("ktor.jwt.issuer", "3D printing api")
    put("ktor.jwt.audience", "printer audience")
    put("ktor.jwt.realm", "3D printing api")
}

fun runServer(test: TestApplicationEngine.() -> Unit) {
    withTestApplication({
        (environment.config as MapApplicationConfig).apply {
            configForTesting()
        }
        module(testing = true, koinModules = testModules)
    }) {
        test()
    }
}

fun TestApplicationEngine.handleGetRequest(
    uri: String,
    assert: TestApplicationCall.() -> Unit
) {
    handleRequest(HttpMethod.Get, uri).apply {
        assert()
    }
}

fun TestApplicationEngine.handlePostRequest(
    uri: String,
    body: List<Pair<String, String?>> = listOf(),
    assert: TestApplicationCall.() -> Unit
) {
    handleRequest(HttpMethod.Post, uri) {
        addHeader(
            HttpHeaders.ContentType,
            ContentType.Application.FormUrlEncoded.toString()
        )
        setBody(body.formUrlEncode())
    }.apply {
        assert()
    }
}

fun TestApplicationEngine.userLogin() {
    handlePostRequest(
        "/auth/login",
        listOf(
            "Email" to TEST_USER_EMAIL,
            "Password" to TEST_USER_PASSWORD
        )
    ) {
        val userPrincipal = response.call.sessions.get<UserPrincipal>()!!
        assertEquals(TEST_USER_EMAIL, userPrincipal.email)
        assertNotEquals(EMAIL_PASSWORD_INCORRECT, response.content)
    }
}

fun TestApplicationEngine.adminLogin() {
    handlePostRequest(
        "/admin/login",
        listOf(
            "name" to "admin",
            "Password" to "password"
        )
    ) {
        val adminPrincipal = response.call.sessions.get<AdminPrincipal>()!!
        assertEquals("admin", adminPrincipal.name)
        assertNotEquals(EMAIL_PASSWORD_INCORRECT, response.content)
    }
}

fun TestApplicationEngine.runWithLoggedUser(test: TestApplicationEngine.() -> Unit) {
    cookiesSession {
        userLogin()
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
        val cookie = response.call.sessions.get<ObjectsCookie>()!!
        val cookieObject = cookie.objects.find { it.id == TEST_CREATED_OBJECT }
        assertNotNull(cookieObject)

        assertEquals(TEST_CREATED_OBJECT, response.content)
    }
}