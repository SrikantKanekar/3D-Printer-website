package features.wishlist

import com.example.features.account.domain.User
import com.example.features.cart.data.CartDataSource
import com.example.features.wishlist.data.WishlistDataSource

class FakeWishlistDataSourceImpl(
    private val userData: HashMap<String, User>
): WishlistDataSource {

}