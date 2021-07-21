package com.example.features.checkout.data

import com.example.database.order.OrderDataSource
import com.example.database.user.UserDataSource
import com.example.features.notification.data.NotificationManager.sendNotification
import com.example.features.notification.data.generateNotification
import com.example.model.Address
import com.example.model.Object
import com.example.model.Order
import com.example.util.enums.NotificationType.PLACED
import com.example.util.enums.ObjectStatus.CART
import com.example.util.enums.ObjectStatus.TRACKING

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

    suspend fun placeOrder(email: String): Order {
        val user = userDataSource.getUser(email)
        val order = orderDataSource.generateNewOrder(userEmail = email)

        var price = 0
        user.objects
            .filter { it.status == CART }
            .forEach { obj ->
                order.objectIds.add(obj.id)
                obj.status = TRACKING
                price += obj.slicingDetails.totalPrice!! * obj.quantity
            }
        order.price = price

        orderDataSource.insertOrder(order)
        user.orderIds.add(order.id)

        val notification = generateNotification(PLACED, user, order)
        sendNotification(notification, user.email)
        user.notification.add(notification)

        userDataSource.updateUser(user)
        return order
    }
}