package di

import com.example.database.order.OrderDataSource
import com.example.database.request.SpecialRequestDatasource
import com.example.database.user.UserDataSource
import data.orderDataFactory
import data.userDataFactory
import fakeDataSource.FakeOrderDataSourceImpl
import fakeDataSource.FakeRequestDatasourceImpl
import fakeDataSource.FakeUserDataSourceImpl
import fakeDataSource.TestRepository
import org.koin.dsl.module

val fakeDatabaseModule = module {

    single<UserDataSource> { FakeUserDataSourceImpl(userDataFactory()) }
    single<OrderDataSource> { FakeOrderDataSourceImpl(orderDataFactory()) }
    single<SpecialRequestDatasource> { FakeRequestDatasourceImpl(HashMap()) }

    single { TestRepository(get(), get()) }
}