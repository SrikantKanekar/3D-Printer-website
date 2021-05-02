package com.example.di

import com.example.database.*
import com.example.features.account.data.AccountDataSource
import com.example.features.account.data.AccountDataSourceImpl
import com.example.features.account.data.AccountRepository
import com.example.features.auth.data.AuthDataSource
import com.example.features.auth.data.AuthDataSourceImpl
import com.example.features.auth.data.AuthRepository
import com.example.features.cart.data.CartDataSource
import com.example.features.cart.data.CartDataSourceImpl
import com.example.features.cart.data.CartRepository
import com.example.features.checkout.data.CheckoutDataSource
import com.example.features.checkout.data.CheckoutDataSourceImpl
import com.example.features.checkout.data.CheckoutRepository
import com.example.features.order.data.OrderDataSource
import com.example.features.order.data.OrderDataSourceImpl
import com.example.features.order.data.OrderRepository
import com.example.features.wishlist.data.WishlistDataSource
import com.example.features.wishlist.data.WishlistDataSourceImpl
import com.example.features.wishlist.data.WishlistRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module

val authModule = module {

    single(named(COLLECTION_USER)) { users }
    single(named(COLLECTION_WISHLIST)) { wishlistOrders }
    single(named(COLLECTION_CART)) { cartOrders }

    // Auth
    single<AuthDataSource> { AuthDataSourceImpl(get(named(COLLECTION_USER))) }
    single { AuthRepository(get()) }

    // Account
    single<AccountDataSource> { AccountDataSourceImpl(get(named(COLLECTION_USER))) }
    single { AccountRepository(get()) }

    // Order
    single<OrderDataSource> {
        OrderDataSourceImpl(
            users = get(named(COLLECTION_USER)),
            wishlistOrders = get(named(COLLECTION_WISHLIST)),
            cartOrders = get(named(COLLECTION_CART))
        )
    }
    single { OrderRepository(get()) }

    // Wishlist
    single<WishlistDataSource> {
        WishlistDataSourceImpl(
            users = get(named(COLLECTION_USER)),
            wishlistOrders = get(named(COLLECTION_WISHLIST)),
            cartOrders = get(named(COLLECTION_CART))
        )
    }
    single { WishlistRepository(get()) }

    // Cart
    single<CartDataSource> {
        CartDataSourceImpl(
            users = get(named(COLLECTION_USER)),
            wishlistOrders = get(named(COLLECTION_WISHLIST)),
            cartOrders = get(named(COLLECTION_CART))
        )
    }
    single { CartRepository(get()) }

    // Checkout
    single<CheckoutDataSource> {
        CheckoutDataSourceImpl(
            users = get(named(COLLECTION_USER)),
            wishlistOrders = get(named(COLLECTION_WISHLIST)),
            cartOrders = get(named(COLLECTION_CART))
        )
    }
    single { CheckoutRepository(get()) }
}