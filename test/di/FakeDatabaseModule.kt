package di

import com.example.database.COLLECTION_ORDER
import com.example.database.COLLECTION_USER
import com.example.database.order.OrderDataSource
import com.example.database.user.UserDataSource
import data.orderDataFactory
import data.userDataFactory
import fakeDataSource.FakeOrderDataSourceImpl
import fakeDataSource.FakeUserDataSourceImpl
import fakeDataSource.TestRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module

val fakeDatabaseModule = module {
    single(named(COLLECTION_USER)) { userDataFactory() }
    single(named(COLLECTION_ORDER)) { orderDataFactory() }

    single<UserDataSource> { FakeUserDataSourceImpl(get(named(COLLECTION_USER))) }
    single<OrderDataSource> { FakeOrderDataSourceImpl(get(named(COLLECTION_ORDER))) }

    single { TestRepository(get(), get()) }
}