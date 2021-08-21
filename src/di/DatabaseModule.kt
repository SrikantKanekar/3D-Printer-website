package com.example.di

import com.example.database.*
import com.example.database.order.OrderDataSource
import com.example.database.order.OrderDataSourceImpl
import com.example.database.request.RequestDatasource
import com.example.database.request.RequestDatasourceImpl
import com.example.database.user.UserDataSource
import com.example.database.user.UserDataSourceImpl
import org.koin.core.qualifier.named
import org.koin.dsl.module

val databaseModule = module {

    single(named(COLLECTION_USER)) { users }
    single(named(COLLECTION_ORDER)) { orders }
    single(named(COLLECTION_REQUEST)) { requests }

    single<UserDataSource> { UserDataSourceImpl(get(named(COLLECTION_USER))) }
    single<OrderDataSource> { OrderDataSourceImpl(get(named(COLLECTION_ORDER))) }
    single<RequestDatasource> { RequestDatasourceImpl(get(named(COLLECTION_REQUEST))) }
}