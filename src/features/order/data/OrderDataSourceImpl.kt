package com.example.features.order.data

import com.example.features.account.domain.User
import com.example.features.order.domain.AdvancedSettings
import com.example.features.order.domain.BasicSettings
import com.example.features.order.domain.Order
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.eq
import org.litote.kmongo.setValue

class OrderDataSourceImpl(
    private val wishlistOrders: CoroutineCollection<Order>,
    private val users: CoroutineCollection<User>
) : OrderDataSource {

    override suspend fun createOrder(order: Order): Boolean {
        return wishlistOrders.insertOne(order).wasAcknowledged()
    }

    override suspend fun addOrderToUserWishlist(email: String, orderId: String): Boolean {
        val user = users.findOne(User::email eq email)!!
        user.wishlist.add(orderId)
        return users.updateOne(User::email eq user.email, user).wasAcknowledged()
    }

    override suspend fun getOrder(id: String): Order? {
        return wishlistOrders.findOne(Order::id eq id)
    }

    override suspend fun updateFileName(id: String, fileName: String): Boolean {
        return wishlistOrders
            .updateOne(Order::id eq id, setValue(Order::fileName, fileName))
            .wasAcknowledged()
    }

    override suspend fun updateBasicSetting(id: String, basicSettings: BasicSettings): Boolean {
        return wishlistOrders
            .updateOne(Order::id eq id, setValue(Order::basicSettings, basicSettings))
            .wasAcknowledged()
    }

    override suspend fun updateAdvancedSetting(id: String, advancedSettings: AdvancedSettings): Boolean {
        return wishlistOrders
            .updateOne(Order::id eq id, setValue(Order::advancedSettings, advancedSettings))
            .wasAcknowledged()
    }
}