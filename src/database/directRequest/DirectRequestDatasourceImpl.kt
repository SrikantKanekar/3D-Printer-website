package com.example.database.request

import com.example.model.DirectRequest
import com.example.util.DatabaseException
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.eq

class DirectRequestDatasourceImpl(
    private val requests: CoroutineCollection<DirectRequest>
) : DirectRequestDatasource {

    override suspend fun add(request: DirectRequest) {
        val exists = requests.findOneById(request._id)
        val done = if (exists == null) {
            requests.insertOne(request).wasAcknowledged()
        } else {
            requests.updateOneById(request._id, request).wasAcknowledged()
        }
        if (!done) throw DatabaseException(
            "error inserting DirectRequest of ${request.userEmail}"
        )
    }

    override suspend fun update(request: DirectRequest) {
        val updated = requests.updateOneById(request._id, request).wasAcknowledged()
        if (!updated) throw DatabaseException(
            "error updating request of ${request.userEmail}"
        )
    }

    override suspend fun getAllActive(): List<DirectRequest> {
        return requests.find(DirectRequest::fulfilled eq false).toList().reversed()
    }

    override suspend fun get(id: String): DirectRequest? {
        return requests.findOneById(id)
    }
}