package data

import com.example.model.Order
import com.example.model.User
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun userDataFactory(): HashMap<String, User> {
    val map = HashMap<String, User>()

    val stringData = Json.encodeToString(testUserData)
    val users = Json.decodeFromString<List<User>>(stringData)

    for (user in users) {
        map[user.email] = user
    }
    return map
}

fun orderDataFactory(): HashMap<String, Order> {
    val map = HashMap<String, Order>()

    val stringData = Json.encodeToString(testOrderData)
    val orders = Json.decodeFromString<List<Order>>(stringData)

    for (order in orders) {
        map[order.id] = order
    }
    return map
}