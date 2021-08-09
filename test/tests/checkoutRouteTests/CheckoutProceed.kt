package tests.checkoutRouteTests

import org.koin.test.KoinTest

class CheckoutProceed : KoinTest {

//    @Test
//    fun `should return Unauthorised if not logged`() {
//        runServer {
//            handlePostRequest(
//                uri = "/checkout/proceed",
//                body = CheckoutProceedRequest(true)
//            ) {
//                assertEquals(HttpStatusCode.Unauthorized, response.status())
//            }
//        }
//    }
//
//    @Test
//    fun `should return bad request if payment verification fails`() {
//        runServer {
//            handlePostRequest(
//                uri = "/checkout/proceed",
//                body = CheckoutProceedRequest(false),
//                logged = true
//            ) {
//                assertEquals(HttpStatusCode.BadRequest, response.status())
//            }
//        }
//    }
//
//    @Test
//    fun `should return created order if success`() {
//        runServer {
//            handlePostRequest(
//                uri = "/checkout/proceed",
//                body = CheckoutProceedRequest(true),
//                logged = true
//            ) {
//                runBlocking {
//                    val testRepository by inject<TestRepository>()
//
//                    val order = testRepository.getActiveOrderById(TEST_CREATED_ORDER)!!
//                    assertEquals(PLACED, order.status)
//                    assertEquals(TEST_USER_EMAIL, order.userEmail)
//                    assertEquals(TEST_CART_OBJECT, order.objectIds[0])
//                    assertEquals(4, order.price)
//
//                    val user = testRepository.getUser(TEST_USER_EMAIL)
//                    assertTrue { user.orderIds.contains(order.id) }
//                    assertTrue {
//                        user.objects
//                            .filter { it.status == TRACKING }
//                            .map { it.id }
//                            .containsAll(order.objectIds)
//                    }
//
//                    val responseOrder = Json.decodeFromString<Order>(response.content!!)
//                    assertEquals(responseOrder, order)
//
//                    assertEquals(HttpStatusCode.Created, response.status())
//                }
//            }
//        }
//    }
}