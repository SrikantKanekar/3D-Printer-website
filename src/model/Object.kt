package com.example.model

import com.example.util.enums.ObjectStatus
import com.example.util.enums.PrintingStatus
import kotlinx.serialization.Serializable

@Serializable
data class Object(
    val id: String,
    var name: String,
    val fileUrl: String,
    val fileExtension: String,
    val imageUrl: String,
    var quantity: Int = 1,
    var status: ObjectStatus = ObjectStatus.NONE,
    var printingStatus: PrintingStatus = PrintingStatus.PENDING,
    var setting: Setting = Setting(),
    var slicingDetails: SlicingDetails = SlicingDetails(),
    val trackingDetails: TrackingDetails = TrackingDetails()
)