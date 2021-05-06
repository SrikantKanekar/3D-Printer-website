package features.tracker

import com.example.features.account.domain.User
import com.example.features.order.domain.Object
import com.example.features.tracker.data.TrackerDataSource

class FakeTrackerDataSourceImpl(
    private val userData: HashMap<String, User>
): TrackerDataSource {

    override suspend fun getUserTrackingOrders(email: String): ArrayList<Object> {
        TODO("Not yet implemented")
    }
}