package com.example.features.wishlist.data

import com.example.features.order.domain.Order

interface WishlistDataSource {

    suspend fun getUserWishlist(email: String): ArrayList<String>

    suspend fun getWishlistOrderList(orderIds: ArrayList<String>): ArrayList<Order>

    suspend fun deleteWishlist(orderId: String): Boolean

    suspend fun deleteUserWishlist(email: String, orderId: String): Boolean

    suspend fun addToCart(email: String, orderId: String): Boolean

    suspend fun getAllWishlistOrders(): ArrayList<Order>
}