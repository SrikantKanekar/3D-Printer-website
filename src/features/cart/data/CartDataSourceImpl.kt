package com.example.features.cart.data

import com.example.features.account.domain.User
import com.example.features.order.domain.Order
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.eq

class CartDataSourceImpl(
    private val users: CoroutineCollection<User>,
    private val wishlistOrders: CoroutineCollection<Order>,
    private val cartOrders: CoroutineCollection<Order>
) : CartDataSource {

    override suspend fun getUserCartOrders(email: String): ArrayList<Order> {
        val user = users.findOne(User::email eq email)!!
        return ArrayList(
            user.cartOrders.map {
                cartOrders.findOneById(it)!!
            }
        )
    }

    override suspend fun removeCartOrder(email: String, orderId: String): Boolean {
        val user = users.findOne(User::email eq email)!!
        val removed = user.cartOrders.remove(orderId)
        if (removed) {
            user.wishlist.add(orderId)
            val userResult = users.updateOne(User::email eq email, user).wasAcknowledged()

            val order = cartOrders.findOneById(orderId)!!
            cartOrders.deleteOneById(orderId)
            val orderResult = wishlistOrders.insertOne(order).wasAcknowledged()

            return userResult and orderResult
        }
        return false
    }

    override suspend fun getAllCartOrders(): ArrayList<Order> {
        return ArrayList(cartOrders.find().toList())
    }
}