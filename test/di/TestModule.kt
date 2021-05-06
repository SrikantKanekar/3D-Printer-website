package di

import com.example.database.*
import com.example.database.user.UserDataSource
import com.example.features.account.data.AccountDataSource
import com.example.features.account.data.AccountRepository
import com.example.features.admin.data.AdminDataSource
import com.example.features.admin.data.AdminRepository
import com.example.features.auth.data.AuthRepository
import com.example.features.cart.data.CartDataSource
import com.example.features.cart.data.CartRepository
import com.example.features.checkout.data.CheckoutDataSource
import com.example.features.checkout.data.CheckoutRepository
import com.example.features.history.data.HistoryDataSource
import com.example.features.history.data.HistoryRepository
import com.example.features.order.data.OrderDataSource
import com.example.features.order.data.OrderRepository
import com.example.features.tracker.data.TrackerDataSource
import com.example.features.tracker.data.TrackerRepository
import com.example.features.wishlist.data.WishlistDataSource
import com.example.features.wishlist.data.WishlistRepository
import data.DataFactory
import data.FakeUserDataSourceImpl
import features.account.FakeAccountDataSourceImpl
import features.admin.FakeAdminDataSourceImpl
import features.cart.FakeCartDataSourceImpl
import features.checkout.FakeCheckoutDataSourceImpl
import features.history.FakeHistoryDataSourceImpl
import features.order.FakeOrderDataSourceImpl
import features.tracker.FakeTrackerDataSourceImpl
import features.wishlist.FakeWishlistDataSourceImpl
import org.koin.core.qualifier.named
import org.koin.dsl.module

val testAuthModule = module {

    single(named(COLLECTION_USER)) { DataFactory().users() }

    single<UserDataSource> { FakeUserDataSourceImpl(get(named(COLLECTION_USER))) }

    single { AuthRepository(get()) }

    /////////////////////////////////////

    single(named(COLLECTION_WISHLIST)) { DataFactory().wishlistOrders() }
    single(named(COLLECTION_CART)) { DataFactory().cartOrders() }

    single<AccountDataSource> { FakeAccountDataSourceImpl(get(named(COLLECTION_USER))) }
    single { AccountRepository(get()) }

    single<OrderDataSource> {
        FakeOrderDataSourceImpl(
            userData = get(named(COLLECTION_USER)),
            wishlistOrders = get(named(COLLECTION_WISHLIST)),
            cartOrders = get(named(COLLECTION_CART))
        )
    }
    single { OrderRepository(get()) }

    single<WishlistDataSource> {
        FakeWishlistDataSourceImpl(
            userData = get(named(COLLECTION_USER)),
            wishlistOrders = get(named(COLLECTION_WISHLIST)),
            cartOrders = get(named(COLLECTION_CART))
        )
    }
    single { WishlistRepository(get()) }


    // Cart
    single<CartDataSource> {
        FakeCartDataSourceImpl(
            userData = get(named(COLLECTION_USER)),
            wishlistOrders = get(named(COLLECTION_WISHLIST)),
            cartOrders = get(named(COLLECTION_CART))
        )
    }
    single { CartRepository(get()) }

    // Checkout
    single<CheckoutDataSource> {
        FakeCheckoutDataSourceImpl(
            userData = get(named(COLLECTION_USER)),
            wishlistOrders = get(named(COLLECTION_WISHLIST)),
            cartOrders = get(named(COLLECTION_CART))
        )
    }
    single { CheckoutRepository(get()) }

    // Tracking
    single<TrackerDataSource> {
        FakeTrackerDataSourceImpl(
            userData = get(named(COLLECTION_USER))
        )
    }
    single { TrackerRepository(get()) }

    // History
    single<HistoryDataSource> {
        FakeHistoryDataSourceImpl(
            userData = get(named(COLLECTION_USER))
        )
    }
    single { HistoryRepository(get()) }

    // Admin
    single<AdminDataSource> {
        FakeAdminDataSourceImpl(

        )
    }
    single { AdminRepository(get()) }
}