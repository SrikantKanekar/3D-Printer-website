package com.example.routes

import com.example.data.database.Constants.HELLO_WORLD_RESPONSE
import com.example.module
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals

class TestRoutesTest {

    @Test
    fun helloWorldTest() {
        withTestApplication({ module(testing = true) }) {
            handleRequest(HttpMethod.Get, "/test/hello-world").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(HELLO_WORLD_RESPONSE, response.content)
            }
        }
    }
}