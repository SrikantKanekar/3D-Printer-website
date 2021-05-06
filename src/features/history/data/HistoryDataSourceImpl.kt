package com.example.features.history.data

import com.example.features.account.domain.User
import com.example.features.`object`.domain.Object
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.eq

class HistoryDataSourceImpl(
    private val users: CoroutineCollection<User>,
    private val historyOrders: CoroutineCollection<Object>
) : HistoryDataSource {

    override suspend fun getUserHistoryOrders(email: String): ArrayList<Object> {
        val user = users.findOne(User::email eq email)!!
        val historyOrderIds = user.currentOrders
        return ArrayList(
            historyOrderIds.map {
                historyOrders.findOneById(it)!!
            }
        )
    }
}