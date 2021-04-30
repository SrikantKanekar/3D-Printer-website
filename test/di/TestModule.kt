package di

import com.example.features.account.data.AccountDataSource
import com.example.features.account.data.AccountRepository
import com.example.features.auth.data.AuthDataSource
import com.example.features.auth.data.AuthRepository
import com.example.features.order.data.OrderDataSource
import com.example.features.order.data.OrderRepository
import data.DataFactory
import features.account.FakeAccountDataSourceImpl
import features.auth.FakeAuthDataSourceImpl
import features.order.FakeOrderDataSourceImpl
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