package features.tracker

import com.example.features.account.domain.User
import com.example.features.cart.data.CartDataSource
import com.example.features.tracker.data.TrackerDataSource

class FakeTrackerDataSourceImpl(
    private val userData: HashMap<String, User>
): TrackerDataSource {

}