package di

import com.example.di.configModule
import com.example.di.repositoryModule

val testModules = listOf(
    fakeDatabaseModule,
    repositoryModule,
    configModule
)