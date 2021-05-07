package fakeDataSource

import com.example.database.user.UserDataSource
import com.example.features.`object`.domain.Object
import com.example.features.account.domain.User
import data.Constants.TEST_CREATED_OBJECT

class FakeUserDataSourceImpl(
    private val users: HashMap<String, User>
): UserDataSource {

    override suspend fun insertUser(user: User): Boolean {
        users[user.email] = user
        return true
    }

    override suspend fun getUserOrNull(email: String): User? {
        return users[email]
    }

    override suspend fun getUser(email: String): User {
        return users[email]!!
    }

    override suspend fun updateUser(user: User): Boolean {
        users[user.email] = user
        return true
    }

    override suspend fun createNewObject(fileName: String): Object {
        return Object(id = TEST_CREATED_OBJECT, fileName = fileName)
    }
}