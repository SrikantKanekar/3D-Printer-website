package features.`object`

import com.example.features.account.data.AccountRepository
import com.example.features.`object`.data.OrderRepository
import com.example.features.wishlist.domain.ObjectsCookie
import data.Constants.TEST_CREATED_ORDER
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

val uploadFile = PartData.FileItem(
    { byteArrayOf(1, 2, 3).inputStream().asInput() },
    { },
    headersOf(
        HttpHeaders.ContentDisposition,
        ContentDisposition.Inline
            .withParameter(ContentDisposition.Parameters.FileName, TEST_FILE_UPLOAD_NAME)
            .toString()
    )
)

fun createFileAndConfirm(id: String) {
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

fun TestApplicationEngine.`create order without login`(orderRepository: OrderRepository) {
    handleRequest(HttpMethod.Post, "/order/create") {
        val boundary = "***bbb***"
        addHeader(
            HttpHeaders.ContentType,
            ContentType.MultiPart.FormData.withParameter("boundary", boundary).toString()
        )
        setBody(boundary, listOf(uploadFile))
    }.apply {
        runBlocking {
            val cookie = response.call.sessions.get<ObjectsCookie>()!!
            assertNotNull(cookie.objects.find { it.id == TEST_CREATED_ORDER })

            val order = orderRepository.getOrder(TEST_CREATED_ORDER)!!
            assertEquals(TEST_FILE_UPLOAD_NAME, order.fileName)

            assertEquals(HttpStatusCode.Found, response.status())
        }
    }
}

// must be called inside runWithTestUser
fun TestApplicationEngine.`create order after login`(
    accountRepository: AccountRepository,
    orderRepository: OrderRepository
) {
    handleRequest(HttpMethod.Post, "/order/create") {
        val boundary = "***bbb***"
        addHeader(
            HttpHeaders.ContentType,
            ContentType.MultiPart.FormData.withParameter("boundary", boundary).toString()
        )
        setBody(boundary, listOf(uploadFile))
    }.apply {
        runBlocking {
            val wishlist = accountRepository.getUser(TEST_USER_EMAIL).wishlist
            assertTrue { wishlist.contains(TEST_CREATED_ORDER) }

            val order = orderRepository.getOrder(TEST_CREATED_ORDER)!!
            assertEquals(TEST_FILE_UPLOAD_NAME, order.fileName)

            assertEquals(HttpStatusCode.Found, response.status())
        }
    }
}