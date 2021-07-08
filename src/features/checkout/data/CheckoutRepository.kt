package com.example.features.checkout.data

import com.example.database.order.OrderDataSource
import com.example.database.user.UserDataSource
import com.example.features.`object`.domain.Object
import com.example.features.`object`.domain.ObjectStatus.CART
import com.example.features.`object`.domain.ObjectStatus.TRACKING
import com.example.features.checkout.domain.Address
import com.example.features.notification.domain.NotificationManager.sendNotification
import com.example.features.notification.domain.NotificationType.PLACED
import com.example.features.notification.domain.generateNotification

class CheckoutRepository(
    private val userDataSource: UserDataSource,
    private val orderDataSource: OrderDataSource,
) {
    suspend fun getCartObjects(email: String): ArrayList<Object> {
        return ArrayList(userDataSource.getUser(email).objects.filter { it.status == CART })
    }

    suspend fun getUserAddress(email: String): Address {
        return userDataSource.getUser(email).address
    }

    suspend fun updateUserAddress(email: String, address: Address): Boolean {
        val user = userDataSource.getUser(email)
        user.address = address
        return userDataSource.updateUser(user)
    }

    suspend fun placeOrder(email: String): Boolean {
        val user = userDataSource.getUser(email)
        val order = orderDataSource.creteNewOrder(userEmail = email)

        var price = 0
        user.objects
            .filter { it.status == CART }
            .forEach { obj ->
                order.objectIds.add(obj.id)
                obj.status = TRACKING
                price += obj.slicingDetails.totalPrice!! * obj.quantity
            }
        order.price = price

        var updated = false
        if (order.objectIds.size > 0){
            val ordered = orderDataSource.insertOrder(order)
            if (ordered) {
                user.orderIds.add(order.id)

                val notification = generateNotification(PLACED, user, order)
                sendNotification(notification, user.email)
                user.notification.add(notification)

                updated = userDataSource.updateUser(user)
            }
        }
        return updated
    }
}