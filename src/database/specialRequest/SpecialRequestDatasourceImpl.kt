package com.example.database.request

import com.example.model.SpecialRequest
import com.example.util.DatabaseException
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.eq

class SpecialRequestDatasourceImpl(
    private val requests: CoroutineCollection<SpecialRequest>
) : SpecialRequestDatasource {

    override suspend fun add(request: SpecialRequest) {
        val exists = requests.findOneById(request._id)
        val done = if (exists == null) {
            requests.insertOne(request).wasAcknowledged()
        } else {
            requests.updateOneById(request._id, request).wasAcknowledged()
        }
        if (!done) throw DatabaseException(
            "error inserting SpecialRequest of ${request.userEmail}"
        )
    }

    override suspend fun update(request: SpecialRequest) {
        val updated = requests.updateOneById(request._id, request).wasAcknowledged()
        if (!updated) throw DatabaseException(
            "error updating SpecialRequest of ${request.userEmail}"
        )
    }

    override suspend fun getAllActive(): List<SpecialRequest> {
        return requests.find(SpecialRequest::fulfilled eq false).toList().reversed()
    }

    override suspend fun get(id: String): SpecialRequest? {
        return requests.findOneById(id)
    }
}