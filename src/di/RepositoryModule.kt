package com.example.di

import com.example.features.`object`.data.ObjectRepository
import com.example.features.account.data.AccountRepository
import com.example.features.admin.data.AdminRepository
import com.example.features.auth.data.AuthRepository
import com.example.features.cart.data.CartRepository
import com.example.features.checkout.data.CheckoutRepository
import com.example.features.notification.data.NotificationRepository
import com.example.features.order.data.OrderRepository
import com.example.features.orders.data.OrdersRepository
import com.example.features.userObject.data.UserObjectRepository
import org.koin.dsl.module

val repositoryModule = module {

    single { AccountRepository(get()) }
    single { AdminRepository(get(), get(), get()) }
    single { AuthRepository(get()) }
    single { CartRepository(get()) }
    single { CheckoutRepository(get(), get(), get()) }
    single { NotificationRepository(get()) }
    single { UserObjectRepository(get()) }
    single { ObjectRepository(get()) }
    single { OrderRepository(get(), get(), get()) }
    single { OrdersRepository(get()) }
}