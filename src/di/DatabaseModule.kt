package com.example.di

import com.example.database.*
import com.example.database.order.OrderDataSource
import com.example.database.order.OrderDataSourceImpl
import com.example.database.request.DirectRequestDatasource
import com.example.database.request.DirectRequestDatasourceImpl
import com.example.database.request.SpecialRequestDatasource
import com.example.database.request.SpecialRequestDatasourceImpl
import com.example.database.user.UserDataSource
import com.example.database.user.UserDataSourceImpl
import org.koin.core.qualifier.named
import org.koin.dsl.module

val databaseModule = module {

    single(named(COLLECTION_USER)) { users }
    single(named(COLLECTION_ORDER)) { orders }
    single(named(COLLECTION_DIRECT_REQUEST)) { directRequests }
    single(named(COLLECTION_SPECIAL_REQUEST)) { specialRequests }

    single<UserDataSource> { UserDataSourceImpl(get(named(COLLECTION_USER))) }
    single<OrderDataSource> { OrderDataSourceImpl(get(named(COLLECTION_ORDER))) }
    single<DirectRequestDatasource> { DirectRequestDatasourceImpl(get(named(COLLECTION_DIRECT_REQUEST))) }
    single<SpecialRequestDatasource> { SpecialRequestDatasourceImpl(get(named(COLLECTION_SPECIAL_REQUEST))) }
}