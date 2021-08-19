package com.example.model

import com.example.util.enums.ObjectStatus
import com.example.util.enums.ObjectStatus.NONE
import com.example.util.enums.PrintingStatus
import com.example.util.enums.PrintingStatus.PENDING
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
    var slicing: Slicing,
    val trackingDetails: TrackingDetails = TrackingDetails()
)