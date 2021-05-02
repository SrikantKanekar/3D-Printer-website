package data

import com.example.features.account.domain.User
import com.example.features.order.domain.Order
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DataFactory {

    fun users(): HashMap<String, User> {
        val userList = Gson().fromJson<List<User>>(
            readFile("user_list.json"),
            object : TypeToken<List<User>>() {}.type
        )
        val map = HashMap<String, User>()
        for (user in userList) {
            map[user.email] = user
        }
        return map
    }

    fun wishlistOrders(): HashMap<String, Order> {
        val orderList = Gson().fromJson<List<Order>>(
            readFile("wishlist_orders.json"),
            object : TypeToken<List<Order>>() {}.type
        )
        val map = HashMap<String, Order>()
        for (order in orderList) {
            map[order.id] = order
        }
        return map
    }

    fun cartOrders(): HashMap<String, Order> {
        val orderList = Gson().fromJson<List<Order>>(
            readFile("cart_orders.json"),
            object : TypeToken<List<Order>>() {}.type
        )
        val map = HashMap<String, Order>()
        for (order in orderList) {
            map[order.id] = order
        }
        return map
    }

    private fun readFile(fileName: String): String {
        return this.javaClass.classLoader.getResource(fileName).readText()
    }
}