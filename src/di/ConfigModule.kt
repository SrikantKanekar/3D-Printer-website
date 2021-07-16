package com.example.di

import com.example.config.AppConfig
import org.koin.dsl.module

val configModule = module{
    single { AppConfig() }
}