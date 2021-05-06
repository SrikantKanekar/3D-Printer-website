package com.example.features.tracker.data

import com.example.features.account.domain.User
import com.example.features.order.domain.Object
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.eq

class TrackerDataSourceImpl(
    private val users: CoroutineCollection<User>,
    private val processingOrders: CoroutineCollection<Object>
): TrackerDataSource {

    override suspend fun getUserTrackingOrders(email: String): ArrayList<Object> {
        val user = users.findOne(User::email eq email)!!
        val trackingOrderIds = user.currentOrders
        return ArrayList(
            trackingOrderIds.map {
                processingOrders.findOneById(it)!!
            }
        )
    }
}