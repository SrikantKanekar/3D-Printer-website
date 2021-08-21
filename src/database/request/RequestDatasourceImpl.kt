package com.example.database.request

import com.example.model.Request
import com.example.util.DatabaseException
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.eq

class RequestDatasourceImpl(
    private val requests: CoroutineCollection<Request>
) : RequestDatasource {

    override suspend fun add(request: Request) {
        val exists = requests.findOneById(request._id)
        val done = if (exists == null) {
            requests.insertOne(request).wasAcknowledged()
        } else {
            requests.updateOneById(request._id, request).wasAcknowledged()
        }
        if (!done) throw DatabaseException(
            "error inserting request of ${request.userEmail}"
        )
    }

    override suspend fun update(request: Request) {
        val updated = requests.updateOneById(request._id, request).wasAcknowledged()
        if (!updated) throw DatabaseException(
            "error updating request of ${request.userEmail}"
        )
    }

    override suspend fun getAllPending(): List<Request> {
        return requests.find(Request::fulfilled eq false).toList().reversed()
    }

    override suspend fun get(id: String): Request? {
        return requests.findOneById(id)
    }
}