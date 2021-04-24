package com.example.di

import com.example.database.users
import com.example.feautures.auth.data.AuthDataSource
import com.example.feautures.auth.data.AuthDataSourceImpl
import com.example.feautures.auth.data.AuthRepository
import org.koin.dsl.module

val authModule = module {
    single { AuthRepository(get()) }
    single<AuthDataSource> { AuthDataSourceImpl(get()) }
    single { users }
}