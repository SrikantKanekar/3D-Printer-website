package com.example.database.request

import com.example.model.Request

interface RequestDatasource {

    suspend fun add(request: Request)

    suspend fun update(request: Request)

    suspend fun getAllPending(): List<Request>

    suspend fun get(id: String): Request?
}