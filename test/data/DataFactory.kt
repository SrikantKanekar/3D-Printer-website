package com.example.data

import com.example.feautures.auth.domain.User
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DataFactory {

    fun produceHashMapOfUsers(): HashMap<String, User> {
        val list = produceListOfUsers()
        val map = HashMap<String, User>()
        for (user in list) {
            map[user.email] = user
        }
        return map
    }

    fun produceListOfUsers(): List<User> {
        return Gson()
            .fromJson(
                readFile("user_list.json"),
                object : TypeToken<List<User>>() {}.type
            )
    }

    private fun readFile(fileName: String): String {
        return this.javaClass.classLoader.getResource(fileName).readText()
    }
}