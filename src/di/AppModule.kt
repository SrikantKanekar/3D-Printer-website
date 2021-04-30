package com.example.di

import com.example.database.currentOrders
import com.example.database.users
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

    single(named("users")) { users }
    single(named("currentOrders")) { currentOrders }

    single<AuthDataSource> { AuthDataSourceImpl(get(named("users"))) }
    single { AuthRepository(get()) }

    single<AccountDataSource> { AccountDataSourceImpl(get(named("users"))) }
    single { AccountRepository(get()) }

    single<OrderDataSource> {
        OrderDataSourceImpl(
            currentOrders = get(named("currentOrders")),
            users = get(named("users"))
        )
    }
    single { OrderRepository(get()) }
}