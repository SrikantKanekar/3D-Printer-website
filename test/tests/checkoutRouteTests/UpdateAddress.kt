package tests.checkoutRouteTests

import data.TestConstants.TEST_USER_EMAIL
import fakeDataSource.TestRepository
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject
import tests.handlePostRequest
import tests.runServer
import tests.runWithLoggedUser
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class UpdateAddress : KoinTest {

    @Test
    fun `should return Unauthorised if not logged`() {
        runServer {
            handlePostRequest(
                "/checkout/address",
                mapOf(
                    "firstname" to "firstname1",
                    "lastname" to "lastname1",
                    "phoneNumber" to "1111111111",
                    "address" to "address1",
                    "city" to "city1",
                    "state" to "state1",
                    "country" to "country1",
                    "pinCode" to "111111"
                )
            ) {
                assertEquals(HttpStatusCode.Unauthorized, response.status())
            }
        }
    }

    @Test
    fun `should return true if success`() {
        runServer {
            runWithLoggedUser {
                handlePostRequest(
                    "/checkout/address",
                    mapOf(
                        "firstname" to "firstname1",
                        "lastname" to "lastname1",
                        "phoneNumber" to "1111111111",
                        "address" to "address1",
                        "city" to "city1",
                        "state" to "state1",
                        "country" to "country1",
                        "pinCode" to "111111"
                    )
                ) {
                    runBlocking {
                        val testRepository by inject<TestRepository>()

                        val address = testRepository.getUser(TEST_USER_EMAIL).address

                        assertTrue { address.firstname == "firstname1" }
                        assertTrue { address.lastname == "lastname1" }
                        assertTrue { address.phoneNumber == 1111111111L }
                        assertTrue { address.address == "address1" }
                        assertTrue { address.city == "city1" }
                        assertTrue { address.state == "state1" }
                        assertTrue { address.country == "country1" }
                        assertTrue { address.pinCode == 111111 }

                        assertEquals("true", response.content)
                    }
                }
            }
        }
    }
}