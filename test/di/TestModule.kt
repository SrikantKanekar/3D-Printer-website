package di

import com.example.database.COLLECTION_ORDER
import com.example.database.COLLECTION_USER
import com.example.database.order.OrderDataSource
import com.example.database.user.UserDataSource
import com.example.features.`object`.data.ObjectRepository
import com.example.features.account.data.AccountRepository
import com.example.features.admin.data.AdminRepository
import com.example.features.auth.data.AuthRepository
import com.example.features.cart.data.CartRepository
import com.example.features.checkout.data.CheckoutRepository
import com.example.features.history.data.HistoryRepository
import com.example.features.order.data.OrderRepository
import com.example.features.userObject.data.UserObjectRepository
import com.example.features.tracking.data.TrackingRepository
import data.orderDataFactory
import data.userDataFactory
import fakeDataSource.FakeOrderDataSourceImpl
import fakeDataSource.FakeUserDataSourceImpl
import org.koin.core.qualifier.named
import org.koin.dsl.module

val testModule = module {

    single(named(COLLECTION_USER)) { userDataFactory() }
    single(named(COLLECTION_ORDER)) { orderDataFactory() }

    single<UserDataSource> { FakeUserDataSourceImpl(get(named(COLLECTION_USER))) }
    single<OrderDataSource> { FakeOrderDataSourceImpl(get(named(COLLECTION_ORDER))) }

    single { AccountRepository(get()) }
    single { AdminRepository(get(), get()) }
    single { AuthRepository(get()) }
    single { CartRepository(get()) }
    single { CheckoutRepository(get(), get()) }
    single { HistoryRepository(get(), get()) }
    single { UserObjectRepository(get()) }
    single { ObjectRepository(get()) }
    single { OrderRepository(get(), get()) }
    single { TrackingRepository(get(), get()) }
}