package features.checkout

import com.example.features.account.domain.User
import com.example.features.checkout.data.CheckoutDataSource

class FakeCheckoutDataSourceImpl(
    private val userData: HashMap<String, User>
): CheckoutDataSource {

}