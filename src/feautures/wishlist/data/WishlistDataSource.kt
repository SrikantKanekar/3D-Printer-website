package com.example.feautures.wishlist.data

interface WishlistDataSource {

    suspend fun getAllOrders()
}