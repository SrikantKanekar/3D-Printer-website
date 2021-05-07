package data

import com.example.features.account.domain.User
import com.example.features.checkout.domain.Order

fun userDataFactory(): HashMap<String, User> {
    val map = HashMap<String, User>()
    for (user in testUserData) {
        map[user.email] = user
    }
    return map
}

fun orderDataFactory(): HashMap<String, Order> {
    val map = HashMap<String, Order>()
    for (order in testOrderData) {
        map[order.id] = order
    }
    return map
}