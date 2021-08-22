package fakeDataSource

import com.example.database.request.SpecialRequestDatasource
import com.example.model.SpecialRequest

class FakeRequestDatasourceImpl(
    private val requests: HashMap<String, SpecialRequest>
) : SpecialRequestDatasource {

    override suspend fun add(request: SpecialRequest) {
        requests[request._id] = request
    }

    override suspend fun update(request: SpecialRequest) {
        requests[request._id] = request
    }

    override suspend fun getAllActive(): List<SpecialRequest> {
        return requests.values.toList().reversed()
    }

    override suspend fun get(id: String): SpecialRequest? {
        return requests[id]
    }
}