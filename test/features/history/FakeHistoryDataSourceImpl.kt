package features.history

import com.example.features.account.domain.User
import com.example.features.history.data.HistoryDataSource
import com.example.features.order.domain.Order

class FakeHistoryDataSourceImpl(
    private val userData: HashMap<String, User>
): HistoryDataSource {

    override suspend fun getUserHistoryOrders(email: String): ArrayList<Order> {
        TODO("Not yet implemented")
    }
}