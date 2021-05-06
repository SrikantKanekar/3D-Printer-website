package features.tracker

import com.example.features.account.domain.User
import com.example.features.order.domain.Order
import com.example.features.tracker.data.TrackerDataSource

class FakeTrackerDataSourceImpl(
    private val userData: HashMap<String, User>
): TrackerDataSource {

    override suspend fun getUserTrackingOrders(email: String): ArrayList<Order> {
        TODO("Not yet implemented")
    }
}