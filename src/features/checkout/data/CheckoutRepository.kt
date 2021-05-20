package com.example.features.checkout.data

import com.example.database.order.OrderDataSource
import com.example.database.user.UserDataSource
import com.example.features.checkout.domain.Address
import com.example.features.`object`.domain.Object
import com.example.features.`object`.domain.ObjectStatus.*
import com.example.features.notification.data.NotificationRepository
import com.example.features.notification.domain.NotificationType.*
import kotlin.random.Random

class CheckoutRepository(
    private val userDataSource: UserDataSource,
    private val orderDataSource: OrderDataSource,
    private val notificationRepository: NotificationRepository
) {
    suspend fun getUserCartObjects(email: String): ArrayList<Object> {
        return ArrayList(userDataSource.getUser(email).objects.filter { it.status == CART })
    }

    suspend fun getUserAddress(email: String): Address {
        return userDataSource.getUser(email).address
    }

    suspend fun removeObjectFromCart(email: String, objectId: String): Boolean {
        val user = userDataSource.getUser(email)
        user.objects
            .filter { it.status == CART }
            .find { it.id == objectId }
            ?.let { it.status = NONE } ?: return false
        return userDataSource.updateUser(user)
    }

    suspend fun updateUserAddress(email: String, address: Address): Boolean {
        val user = userDataSource.getUser(email)
        user.address = address
        return userDataSource.updateUser(user)
    }

    suspend fun checkoutSuccess(email: String): Boolean {
        val user = userDataSource.getUser(email)
        val order = orderDataSource.creteNewOrder(
            userEmail = email,
            price = Random.nextInt(10000, 20000),
            deliveryDays = Random.nextInt(5, 15),
        )
        user.objects
            .filter { it.status == CART }
            .forEach { obj ->
                order.objectIds.add(obj.id)
                obj.status = TRACKING
            }
        var updated = false
        if (order.objectIds.size > 0){
            val ordered = orderDataSource.insertOrder(order)
            if (ordered) {
                user.orderIds.add(order.id)
                notificationRepository.sendNotification(PLACED, user, order)
                updated = userDataSource.updateUser(user)
            }
        }
        return updated
    }
}