package di

import com.example.database.COLLECTION_CART
import com.example.database.COLLECTION_USER
import com.example.database.COLLECTION_WISHLIST
import com.example.features.account.data.AccountDataSource
import com.example.features.account.data.AccountRepository
import com.example.features.auth.data.AuthDataSource
import com.example.features.auth.data.AuthRepository
import com.example.features.cart.data.CartDataSource
import com.example.features.cart.data.CartRepository
import com.example.features.checkout.data.CheckoutDataSource
import com.example.features.checkout.data.CheckoutDataSourceImpl
import com.example.features.checkout.data.CheckoutRepository
import com.example.features.order.data.OrderDataSource
import com.example.features.order.data.OrderRepository
import com.example.features.wishlist.data.WishlistDataSource
import com.example.features.wishlist.data.WishlistRepository
import data.DataFactory
import features.account.FakeAccountDataSourceImpl
import features.auth.FakeAuthDataSourceImpl
import features.cart.FakeCartDataSourceImpl
import features.checkout.FakeCheckoutDataSourceImpl
import features.order.FakeOrderDataSourceImpl
import features.wishlist.FakeWishlistDataSourceImpl
import org.koin.core.qualifier.named
import org.koin.dsl.module

val testAuthModule = module {
    single(named(COLLECTION_USER)) { DataFactory().users() }
    single(named(COLLECTION_WISHLIST)) { DataFactory().wishlistOrders() }
    single(named(COLLECTION_CART)) { DataFactory().cartOrders() }

    single<AuthDataSource> { FakeAuthDataSourceImpl(get(named(COLLECTION_USER))) }
    single { AuthRepository(get()) }

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

    // Cart
    single<CheckoutDataSource> {
        FakeCheckoutDataSourceImpl(
            userData = get(named(COLLECTION_USER)),
            wishlistOrders = get(named(COLLECTION_WISHLIST)),
            cartOrders = get(named(COLLECTION_CART))
        )
    }
    single { CheckoutRepository(get()) }
}