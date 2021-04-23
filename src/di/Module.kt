package com.example.di

import com.example.repository.HelloRepository
import com.example.service.HelloService
import com.example.service.HelloServiceImpl
import org.koin.dsl.module

val helloAppModule = module {
    single<HelloService> { HelloServiceImpl(get()) }
    single { HelloRepository() }
}