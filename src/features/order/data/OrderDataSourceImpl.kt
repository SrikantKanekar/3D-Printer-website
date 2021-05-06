package com.example.features.order.data

import com.example.features.account.domain.User
import com.example.features.order.domain.AdvancedSettings
import com.example.features.order.domain.BasicSettings
import com.example.features.order.domain.Object
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.eq
import org.litote.kmongo.setValue

class OrderDataSourceImpl(
    private val users: CoroutineCollection<User>,
    private val wishlistOrders: CoroutineCollection<Object>,
    private val cartOrders: CoroutineCollection<Object>
) : OrderDataSource {

    override suspend fun createOrder(fileName: String): Object {
        val order = Object(fileName = fileName)
        wishlistOrders.insertOne(order)
        return order
    }

    override suspend fun addOrderToUserWishlist(email: String, orderId: String): Boolean {
        val user = users.findOne(User::email eq email)!!
        user.wishlist.add(orderId)
        return users.updateOne(User::email eq user.email, user).wasAcknowledged()
    }

    private suspend fun getOrderCollection(orderId: String): CoroutineCollection<Object>? {
        val wishlist = wishlistOrders.findOneById(orderId)
        return if (wishlist == null) {
            val cart = cartOrders.findOneById(orderId)
            if (cart != null) {
                cartOrders
            } else null
        } else wishlistOrders
    }

    override suspend fun getOrder(id: String): Object? {
        return getOrderCollection(id)?.findOne(Object::id eq id)
    }

    override suspend fun updateFileName(id: String, fileName: String): Boolean {
        return getOrderCollection(id)
            ?.updateOne(Object::id eq id, setValue(Object::fileName, fileName))
            ?.wasAcknowledged() ?: false
    }

    override suspend fun updateBasicSetting(id: String, basicSettings: BasicSettings): Boolean {
        return getOrderCollection(id)
            ?.updateOne(Object::id eq id, setValue(Object::basicSettings, basicSettings))
            ?.wasAcknowledged() ?: false
    }

    override suspend fun updateAdvancedSetting(id: String, advancedSettings: AdvancedSettings): Boolean {
        return getOrderCollection(id)
            ?.updateOne(Object::id eq id, setValue(Object::advancedSettings, advancedSettings))
            ?.wasAcknowledged() ?: false
    }
}