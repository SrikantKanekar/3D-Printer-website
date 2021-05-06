package com.example.features.admin.data

import com.example.features.`object`.domain.Object
import com.example.features.`object`.domain.ObjectStatus.*
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.eq

class AdminDataSourceImpl(
    private val processingOrders: CoroutineCollection<Object>,
    private val historyOrders: CoroutineCollection<Object>
) : AdminDataSource {

    override suspend fun getProcessingOrders(): ArrayList<Object> {
        return ArrayList(processingOrders.find().toList())
    }

    override suspend fun getOrderHistory(): ArrayList<Object> {
        return ArrayList(historyOrders.find().toList())
    }

    override suspend fun getProcessingOrder(orderId: String): Object? {
        return processingOrders.findOneById(orderId)
    }

    override suspend fun updateProcessingOrder(order: Object): Boolean {
        return processingOrders.updateOne(Object::id eq order.id, order).wasAcknowledged()
    }

    // handle user as well(after adding userID to order object)
    override suspend fun orderDelivered(orderId: String): Boolean {
        val order = processingOrders.findOneById(orderId)
        return if (order != null) {
            val deleted = processingOrders.deleteOneById(order.id).wasAcknowledged()
            val inserted = historyOrders.insertOne(order.copy(status = COMPLETED)).wasAcknowledged()
            deleted and inserted
        } else {
            false
        }
    }
}