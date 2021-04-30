package com.example.di

import com.example.database.*
import com.example.features.account.data.AccountDataSource
import com.example.features.account.data.AccountDataSourceImpl
import com.example.features.account.data.AccountRepository
import com.example.features.auth.data.AuthDataSource
import com.example.features.auth.data.AuthDataSourceImpl
import com.example.features.auth.data.AuthRepository
import com.example.features.order.data.OrderDataSource
import com.example.features.order.data.OrderDataSourceImpl
import com.example.features.order.data.OrderRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module

val authModule = module {

    single(named(COLLECTION_USER)) { users }
    single(named(COLLECTION_WISHLIST)) { wishlistOrders }

    single<AuthDataSource> { AuthDataSourceImpl(get(named(COLLECTION_USER))) }
    single { AuthRepository(get()) }

    single<AccountDataSource> { AccountDataSourceImpl(get(named(COLLECTION_USER))) }
    single { AccountRepository(get()) }

    single<OrderDataSource> {
        OrderDataSourceImpl(
            wishlistOrders = get(named(COLLECTION_WISHLIST)),
            users = get(named(COLLECTION_USER))
        )
    }
    single { OrderRepository(get()) }
}