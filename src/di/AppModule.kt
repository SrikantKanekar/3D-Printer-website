package com.example.di

import com.example.database.COLLECTION_ORDER
import com.example.database.COLLECTION_USER
import com.example.database.order.OrderDataSource
import com.example.database.order.OrderDataSourceImpl
import com.example.database.orders
import com.example.database.user.UserDataSource
import com.example.database.user.UserDataSourceImpl
import com.example.database.users
import com.example.features.`object`.data.ObjectRepository
import com.example.features.account.data.AccountRepository
import com.example.features.admin.data.AdminRepository
import com.example.features.auth.data.AuthRepository
import com.example.features.cart.data.CartRepository
import com.example.features.checkout.data.CheckoutRepository
import com.example.features.history.data.HistoryRepository
import com.example.features.userObject.data.UserObjectRepository
import com.example.features.tracker.data.TrackerRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = module {

    single(named(COLLECTION_USER)) { users }
    single(named(COLLECTION_ORDER)) { orders }

    single<UserDataSource> { UserDataSourceImpl(get(named(COLLECTION_USER))) }
    single<OrderDataSource> { OrderDataSourceImpl(get(named(COLLECTION_ORDER))) }

    single { AccountRepository(get()) }
    single { AdminRepository(get()) }
    single { AuthRepository(get()) }
    single { CartRepository(get()) }
    single { CheckoutRepository(get(), get()) }
    single { HistoryRepository(get()) }
    single { UserObjectRepository(get()) }
    single { ObjectRepository(get()) }
    single { TrackerRepository(get()) }
}