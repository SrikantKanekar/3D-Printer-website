package com.example.features.`object`.domain

import com.example.features.`object`.domain.ObjectStatus.NONE
import com.example.features.order.domain.PrintingStatus
import com.example.features.order.domain.PrintingStatus.PENDING
import kotlinx.serialization.Serializable

@Serializable
data class Object(
    val id: String,
    var name: String,
    val fileUrl: String,
    val fileExtension: String,
    val imageUrl: String,
    var quantity: Int = 1,
    var status: ObjectStatus = NONE,
    var printingStatus: PrintingStatus = PENDING,
    var setting: Setting = Setting(),
    var slicingDetails: SlicingDetails = SlicingDetails(),
    val trackingDetails: TrackingDetails = TrackingDetails()
)