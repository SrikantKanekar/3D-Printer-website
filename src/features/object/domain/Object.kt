package com.example.features.`object`.domain

import com.example.features.`object`.domain.ObjectStatus.NONE
import com.example.features.order.domain.PrintingStatus
import com.example.features.order.domain.PrintingStatus.*
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class Object(
    var filename: String, // give the object same name as filename,can be changed later by user
    var image: String? = null,
    var basicSettings: BasicSettings = BasicSettings(),
    var advancedSettings: AdvancedSettings = AdvancedSettings(),
    var status: ObjectStatus = NONE,
    var printingStatus: PrintingStatus = PENDING,
    var price: Int? = null,
    var quantity: Int = 1,
    var timeToPrint: Int? = null, // approx printing time in minutes
    val trackingDetails: TrackingDetails = TrackingDetails(),
    @BsonId
    val id: String = ObjectId().toString()
)