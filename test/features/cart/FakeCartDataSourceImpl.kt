package features.cart

import com.example.features.account.domain.User
import com.example.features.cart.data.CartDataSource

class FakeCartDataSourceImpl(
    private val userData: HashMap<String, User>
): CartDataSource {

}