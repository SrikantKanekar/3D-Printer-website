package features.admin

import com.example.features.admin.data.AdminDataSource
import com.example.features.order.domain.Object

class FakeAdminDataSourceImpl: AdminDataSource {
    override suspend fun getProcessingOrders(): ArrayList<Object> {
        TODO("Not yet implemented")
    }

    override suspend fun getOrderHistory(): ArrayList<Object> {
        TODO("Not yet implemented")
    }

    override suspend fun getProcessingOrder(orderId: String): Object? {
        TODO("Not yet implemented")
    }

    override suspend fun updateProcessingOrder(order: Object): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun orderDelivered(orderId: String): Boolean {
        TODO("Not yet implemented")
    }
}