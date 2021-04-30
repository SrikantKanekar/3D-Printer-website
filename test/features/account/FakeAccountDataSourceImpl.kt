package features.account

import com.example.features.account.data.AccountDataSource
import com.example.features.account.domain.User

class FakeAccountDataSourceImpl(
    private val userData: HashMap<String, User>
): AccountDataSource {

    override suspend fun get(email: String): User? {
        return userData[email]
    }

    override suspend fun update(user: User): Boolean {
        userData[user.email] = user
        return true
    }
}