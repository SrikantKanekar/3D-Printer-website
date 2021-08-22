package com.example.database.request

import com.example.model.SpecialRequest

interface SpecialRequestDatasource {

    suspend fun add(request: SpecialRequest)

    suspend fun update(request: SpecialRequest)

    suspend fun getAllActive(): List<SpecialRequest>

    suspend fun get(id: String): SpecialRequest?
}