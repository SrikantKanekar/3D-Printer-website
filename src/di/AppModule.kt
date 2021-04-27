package com.example.di

import com.example.database.users
import com.example.feautures.account.data.AccountDataSource
import com.example.feautures.account.data.AccountDataSourceImpl
import com.example.feautures.account.data.AccountRepository
import com.example.feautures.auth.data.AuthDataSource
import com.example.feautures.auth.data.AuthDataSourceImpl
import com.example.feautures.auth.data.AuthRepository
import org.koin.dsl.module

val authModule = module {

    single { users }

    single<AuthDataSource> { AuthDataSourceImpl(get()) }
    single { AuthRepository(get()) }

    single<AccountDataSource> { AccountDataSourceImpl(get()) }
    single { AccountRepository(get()) }
}