package fakeDataSource

import com.example.database.user.UserDataSource
import com.example.features.`object`.requests.ObjectCreateRequest
import com.example.model.Object
import com.example.model.User
import data.TestConstants.TEST_CREATED_OBJECT

class FakeUserDataSourceImpl(
    private val users: HashMap<String, User>
) : UserDataSource {

    override suspend fun insertUser(user: User) {
        users[user.email] = user
    }

    override suspend fun getUserOrNull(email: String): User? {
        return users[email]
    }

    override suspend fun getUser(email: String): User {
        return users[email]!!
    }

    override suspend fun updateUser(user: User) {
        users[user.email] = user
    }

    override suspend fun createObject(body: ObjectCreateRequest): Object {
        return Object(
            id = TEST_CREATED_OBJECT,
            name = body.name,
            fileUrl = body.fileUrl,
            fileExtension = body.fileExtension,
            imageUrl = body.imageUrl
        )
    }
}