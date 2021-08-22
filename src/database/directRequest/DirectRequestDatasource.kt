package com.example.database.request

import com.example.model.DirectRequest

interface DirectRequestDatasource {

    suspend fun add(request: DirectRequest)

    suspend fun update(request: DirectRequest)

    suspend fun getAllActive(): List<DirectRequest>

    suspend fun get(id: String): DirectRequest?
}