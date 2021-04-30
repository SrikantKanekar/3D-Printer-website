package di

import com.example.database.COLLECTION_USER
import com.example.database.COLLECTION_WISHLIST
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
    single(named(COLLECTION_USER)) { DataFactory().produceHashMapOfUsers() }
    single(named(COLLECTION_WISHLIST)) { DataFactory().produceHashMapOfOrders() }

    single<AuthDataSource> { FakeAuthDataSourceImpl(get(named(COLLECTION_USER))) }
    single { AuthRepository(get()) }

    single<AccountDataSource> { FakeAccountDataSourceImpl(get(named(COLLECTION_USER))) }
    single { AccountRepository(get()) }

    single<OrderDataSource> {
        FakeOrderDataSourceImpl(
            wishlistOrders = get(named(COLLECTION_WISHLIST)),
            userData = get(named(COLLECTION_USER))
        )
    }
    single { OrderRepository(get()) }
}