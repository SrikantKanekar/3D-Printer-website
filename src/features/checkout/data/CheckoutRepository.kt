package com.example.features.checkout.data

import com.example.config.AppConfig
import com.example.database.order.OrderDataSource
import com.example.database.user.UserDataSource
import com.example.features.checkout.requests.RazorpayRequest
import com.example.model.Address
import com.example.model.Object
import com.example.model.Order
import com.example.model.User
import com.example.util.enums.ObjectStatus.CART
import com.example.util.enums.Quality.*
import com.razorpay.RazorpayClient
import com.razorpay.Utils
import org.json.JSONObject

class CheckoutRepository(
    private val userDataSource: UserDataSource,
    private val orderDataSource: OrderDataSource,
) {
    suspend fun getCartObjects(email: String): List<Object> {
        return userDataSource.getUser(email).objects.filter { it.status == CART }
    }

    suspend fun getUserAddress(email: String): Address {
        return userDataSource.getUser(email).address
    }

    suspend fun isCartEmpty(email: String): Boolean {
        val objects = userDataSource.getUser(email).objects.filter { it.status == CART }
        return objects.isNotEmpty()
    }

    suspend fun getUser(email: String): User {
        return userDataSource.getUser(email)
    }

    suspend fun updateUser(user: User) {
        userDataSource.updateUser(user)
    }

    suspend fun generateNewOrder(id: String, email: String): Order {
        return orderDataSource.generateNewOrder(id = id, userEmail = email)
    }

    fun getOrderPrice(user: User): Int {
        return user.objects
            .filter { it.status == CART }
            .sumOf { getObjectPrice(it)!! * it.quantity }
    }

    private fun getObjectPrice(obj: Object): Int? {
        val quality = obj.setting.quality
        return when(quality){
            SUPER -> obj.slicing._super.price
            DYNAMIC -> obj.slicing.dynamic.price
            STANDARD -> obj.slicing.standard.price
            LOW -> obj.slicing.low.price
            CUSTOM -> obj.slicing.custom.price
        }
    }

    suspend fun insertOrder(order: Order) {
        orderDataSource.insertOrder(order)
    }

    suspend fun updateOrder(order: Order) {
        orderDataSource.updateOrder(order)
    }

    suspend fun getOrderById(id: String): Order? {
        return orderDataSource.getOrderById(id)
    }

    fun createRazorpayOrder(price: Int, appConfig: AppConfig): String {
        val razorpay = RazorpayClient(appConfig.razorpayConfig.key, appConfig.razorpayConfig.secret)
        val orderRequest = JSONObject()
        orderRequest.put("amount", price * 100)
        orderRequest.put("currency", "INR")
        val razorpayOrder = razorpay.Orders.create(orderRequest)
        return razorpayOrder.toJson().get("id").toString()
    }

    fun verifySignature(razorpay: RazorpayRequest, appConfig: AppConfig): Boolean {
        val options = JSONObject()
        options.put("razorpay_order_id", razorpay.id)
        options.put("razorpay_payment_id", razorpay.payment_id)
        options.put("razorpay_signature", razorpay.signature)
        return Utils.verifyPaymentSignature(options, appConfig.razorpayConfig.secret)
    }
}