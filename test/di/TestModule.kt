package com.example.di

import com.example.data.DataFactory
import com.example.feautures.auth.FakeAuthDataSourceImpl
import com.example.feautures.auth.data.AuthDataSource
import com.example.feautures.auth.data.AuthRepository
import org.koin.dsl.module

val testAuthModule = module {
    single { AuthRepository(get()) }
    single<AuthDataSource> { FakeAuthDataSourceImpl(get()) }
    single { DataFactory().produceHashMapOfUsers() }
}