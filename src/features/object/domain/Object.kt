package com.example.features.`object`.domain

import com.example.features.`object`.domain.ObjectStatus.NONE
import com.example.features.order.domain.PrintingStatus
import com.example.features.order.domain.PrintingStatus.*
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class Object(
    var filename: String,
    var image: String? = null,
    var quantity: Int = 1,
    var status: ObjectStatus = NONE,
    var printingStatus: PrintingStatus = PENDING,
    var setting: Setting = Setting(),
    var slicingDetails: SlicingDetails = SlicingDetails(),
    val trackingDetails: TrackingDetails = TrackingDetails(),
    @BsonId
    val id: String = ObjectId().toString()
)