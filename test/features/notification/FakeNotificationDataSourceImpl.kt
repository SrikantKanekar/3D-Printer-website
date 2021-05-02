package features.notification

import com.example.features.account.domain.User
import com.example.features.notification.data.NotificationDataSource

class FakeNotificationDataSourceImpl(
    private val userData: HashMap<String, User>
): NotificationDataSource {

}