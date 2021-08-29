package tests.accountRouteTests

import com.example.model.Address
import com.example.util.ValidationException
import data.TestConstants.TEST_USER_EMAIL
import fakeDataSource.TestRepository
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject
import tests.handlePostRequest
import tests.runServer
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class UpdateAddress : KoinTest {

    private val validAddress = Address(
        firstname = "firstname1",
        lastname = "lastname1",
        phoneNumber = "1111111111",
        address = "address1",
        city = "city1",
        state = "state1",
        country = "country1",
        pinCode = 111111
    )

    @Test
    fun `should return unauthorised if user is not logged`() {
        runServer {
            handlePostRequest(
                uri = "/account/address",
                body = validAddress
            ) {
                assertEquals(HttpStatusCode.Unauthorized, response.status())
            }
        }
    }

    @Test
    fun `should fail if input is invalid`() {
        assertFailsWith<ValidationException> {
            Address().validate()
        }
    }

    @Test
    fun `should return updated address`() {
        runServer {
            handlePostRequest(
                uri = "/account/address",
                body = validAddress,
                logged = true
            ) {
                runBlocking {
                    val testRepository by inject<TestRepository>()
                    val address = testRepository.getUser(TEST_USER_EMAIL).address
                    assertTrue { address.firstname == "firstname1" }
                    assertTrue { address.lastname == "lastname1" }
                    assertTrue { address.phoneNumber == "1111111111L" }
                    assertTrue { address.address == "address1" }
                    assertTrue { address.city == "city1" }
                    assertTrue { address.state == "state1" }
                    assertTrue { address.country == "country1" }
                    assertTrue { address.pinCode == 111111 }

                    val responseAddress = Json.decodeFromString<Address>(response.content!!)
                    assertEquals(address, responseAddress)
                }
            }
        }
    }
}