package com.example.features.checkout.data

import com.example.features.account.domain.User
import com.example.features.checkout.domain.Address
import com.example.features.order.domain.Order
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.eq

class CheckoutDataSourceImpl(
    private val users: CoroutineCollection<User>,
    private val wishlistOrders: CoroutineCollection<Order>,
    private val cartOrders: CoroutineCollection<Order>
): CheckoutDataSource {

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

    override suspend fun updateUserAddress(email: String, address: Address): Boolean {
        val user = users.findOne(User::email eq email)!!.copy(address = address)
        return users.updateOne(User::email eq email, user).wasAcknowledged()
    }
}