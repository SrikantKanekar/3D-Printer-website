package fakeDataSource

import com.example.database.user.UserDataSource
import com.example.model.Object
import com.example.model.User
import data.TestConstants.TEST_CREATED_OBJECT

class FakeUserDataSourceImpl(
    private val users: HashMap<String, User>
) : UserDataSource {

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

    override suspend fun createObject(id: String, name: String, fileUrl: String, imageUrl: String, fileExtension: String): Object {
        return Object(id = TEST_CREATED_OBJECT, name = name, fileUrl = fileUrl, fileExtension = fileExtension, imageUrl = imageUrl)
    }
}