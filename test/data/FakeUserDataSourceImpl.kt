package data

import com.example.database.user.UserDataSource
import com.example.features.account.domain.User

class FakeUserDataSourceImpl(
    private val users: HashMap<String, User>
): UserDataSource {

    override suspend fun insertUser(user: User): Boolean {
        users[user.email] = user
        return true
    }

    override suspend fun getUser(email: String): User? {
        return users[email]
    }

    override suspend fun updateUser(user: User): Boolean {
        users[user.email] = user
        return true
    }

    override suspend fun doesUserExist(email: String): Boolean {
        return users.containsKey(email)
    }

    override suspend fun getUserHashedPassword(email: String): String? {
        return users[email]?.password
    }
}