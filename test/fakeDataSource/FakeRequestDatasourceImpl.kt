package fakeDataSource

import com.example.database.request.RequestDatasource
import com.example.model.Request

class FakeRequestDatasourceImpl(
    private val requests: HashMap<String, Request>
) : RequestDatasource {

    override suspend fun add(request: Request) {
        requests[request._id] = request
    }

    override suspend fun update(request: Request) {
        requests[request._id] = request
    }

    override suspend fun getAllPending(): List<Request> {
        return requests.values.toList().reversed()
    }

    override suspend fun get(id: String): Request? {
        return requests[id]
    }
}