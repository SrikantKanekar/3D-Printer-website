package com.example.features.wishlist.data

import com.example.features.order.domain.Order

class WishlistRepository(
    private val wishlistDataSource: WishlistDataSource
) {

    suspend fun getUserWishlist(email: String): ArrayList<String> {
        return wishlistDataSource.getUserWishlist(email)
    }

    suspend fun getWishlistOrderList(orderIds: ArrayList<String>): ArrayList<Order> {
        return wishlistDataSource.getWishlistOrderList(orderIds)
    }

    suspend fun deleteWishlist(orderId: String): Boolean {
        return wishlistDataSource.deleteWishlist(orderId)
    }

    suspend fun deleteUserWishlist(email: String, orderId: String): Boolean {
        return wishlistDataSource.deleteUserWishlist(email, orderId)
    }

    suspend fun addToCart(email: String, orderId: String): Boolean {
        return wishlistDataSource.addToCart(email, orderId)
    }

    suspend fun getAllWishlistOrders(): ArrayList<Order>{
        return wishlistDataSource.getAllWishlistOrders()
    }
}