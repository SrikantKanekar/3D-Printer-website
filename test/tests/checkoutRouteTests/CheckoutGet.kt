package tests.checkoutRouteTests

import com.example.features.checkout.response.CheckoutResponse
import com.example.util.enums.ObjectStatus.CART
import data.TestConstants.TEST_USER_EMAIL
import fakeDataSource.TestRepository
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject
import tests.handleGetRequest
import tests.runServer
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CheckoutGet : KoinTest {

    @Test
    fun `should return Unauthorised if not logged`() {
        runServer {
            handleGetRequest("/checkout") {
                assertEquals(HttpStatusCode.Unauthorized, response.status())
            }
        }
    }

    @Test
    fun `should return objects and address if logged`() {
        runServer {
            handleGetRequest("/checkout", logged = true) {
                runBlocking {
                    val testRepository by inject<TestRepository>()
                    val user = testRepository.getUser(TEST_USER_EMAIL)
                    val res = Json.decodeFromString<CheckoutResponse>(response.content!!)

                    user.objects
                        .filter { it.status == CART }
                        .forEach { assertTrue { res.objects.contains(it) } }
                    assertEquals(user.address, res.address)

                    assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }
}