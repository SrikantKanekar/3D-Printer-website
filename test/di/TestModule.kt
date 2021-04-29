package di

import com.example.feautures.account.data.AccountDataSource
import com.example.feautures.account.data.AccountRepository
import com.example.feautures.auth.data.AuthDataSource
import com.example.feautures.auth.data.AuthRepository
import com.example.feautures.order.data.OrderDataSource
import com.example.feautures.order.data.OrderRepository
import data.DataFactory
import feautures.account.FakeAccountDataSourceImpl
import feautures.auth.FakeAuthDataSourceImpl
import feautures.order.FakeOrderDataSourceImpl
import org.koin.core.qualifier.named
import org.koin.dsl.module

val testAuthModule = module {
    single(named("users")) { DataFactory().produceHashMapOfUsers() }
    single(named("currentOrders")) { DataFactory().produceHashMapOfOrders() }

    single<AuthDataSource> { FakeAuthDataSourceImpl(get(named("users"))) }
    single { AuthRepository(get()) }

    single<AccountDataSource> { FakeAccountDataSourceImpl(get(named("users"))) }
    single { AccountRepository(get()) }

    single<OrderDataSource> {
        FakeOrderDataSourceImpl(
            orderData = get(named("currentOrders")),
            userData = get(named("users"))
        )
    }
    single { OrderRepository(get()) }
}