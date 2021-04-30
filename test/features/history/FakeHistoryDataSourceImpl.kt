package features.history

import com.example.features.account.domain.User
import com.example.features.cart.data.CartDataSource
import com.example.features.history.data.HistoryDataSource

class FakeHistoryDataSourceImpl(
    private val userData: HashMap<String, User>
): HistoryDataSource {

}