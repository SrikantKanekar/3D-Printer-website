package di

import com.example.feautures.account.data.AccountDataSource
import com.example.feautures.account.data.AccountRepository
import data.DataFactory
import feautures.auth.FakeAuthDataSourceImpl
import com.example.feautures.auth.data.AuthDataSource
import com.example.feautures.auth.data.AuthRepository
import feautures.account.FakeAccountDataSourceImpl
import org.koin.dsl.module

val testAuthModule = module {
    single { DataFactory().produceHashMapOfUsers() }

    single<AuthDataSource> { FakeAuthDataSourceImpl(get()) }
    single { AuthRepository(get()) }

    single<AccountDataSource> { FakeAccountDataSourceImpl(get()) }
    single { AccountRepository(get()) }
}