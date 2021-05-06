package com.example.di

import com.example.database.*
import com.example.database.user.UserDataSource
import com.example.database.user.UserDataSourceImpl
import com.example.features.account.data.AccountRepository
import com.example.features.admin.data.AdminDataSource
import com.example.features.admin.data.AdminDataSourceImpl
import com.example.features.admin.data.AdminRepository
import com.example.features.auth.data.AuthRepository
import com.example.features.cart.data.CartDataSource
import com.example.features.cart.data.CartDataSourceImpl
import com.example.features.cart.data.CartRepository
import com.example.features.checkout.data.CheckoutDataSource
import com.example.features.checkout.data.CheckoutDataSourceImpl
import com.example.features.checkout.data.CheckoutRepository
import com.example.features.history.data.HistoryDataSource
import com.example.features.history.data.HistoryDataSourceImpl
import com.example.features.history.data.HistoryRepository
import com.example.features.order.data.OrderDataSource
import com.example.features.order.data.OrderDataSourceImpl
import com.example.features.order.data.OrderRepository
import com.example.features.tracker.data.TrackerDataSource
import com.example.features.tracker.data.TrackerDataSourceImpl
import com.example.features.tracker.data.TrackerRepository
import com.example.features.wishlist.data.WishlistDataSource
import com.example.features.wishlist.data.WishlistDataSourceImpl
import com.example.features.wishlist.data.WishlistRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = module {

    single(named(COLLECTION_USER)) { users }

    single<UserDataSource> { UserDataSourceImpl(get(named(COLLECTION_USER))) }

    single { AuthRepository(get()) }
    single { AccountRepository(get()) }

    /////////////////////////////////

    single(named(COLLECTION_WISHLIST)) { wishlistOrders }
    single(named(COLLECTION_CART)) { cartOrders }
    single(named(COLLECTION_PROCESSING)) { processingOrders }
    single(named(COLLECTION_HISTORY)) { historyOrders }

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
            cartOrders = get(named(COLLECTION_CART)),
            processingOrders = get(named(COLLECTION_PROCESSING))
        )
    }
    single { CheckoutRepository(get()) }

    // Tracking
    single<TrackerDataSource> {
        TrackerDataSourceImpl(
            users = get(named(COLLECTION_USER)),
            processingOrders = get(named(COLLECTION_PROCESSING))
        )
    }
    single { TrackerRepository(get()) }

    // History
    single<HistoryDataSource> {
        HistoryDataSourceImpl(
            users = get(named(COLLECTION_USER)),
            historyOrders = get(named(COLLECTION_HISTORY))
        )
    }
    single { HistoryRepository(get()) }

    // Admin
    single<AdminDataSource> {
        AdminDataSourceImpl(
            processingOrders = get(named(COLLECTION_PROCESSING)),
            historyOrders = get(named(COLLECTION_HISTORY))
        )
    }
    single { AdminRepository(get()) }
}