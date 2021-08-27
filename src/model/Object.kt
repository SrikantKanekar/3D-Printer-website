package com.example.model

import com.example.util.enums.ObjectStatus
import com.example.util.enums.ObjectStatus.NONE
import com.example.util.enums.PrintingStatus
import com.example.util.enums.PrintingStatus.PENDING
import com.example.util.enums.Quality
import com.example.util.enums.Quality.STANDARD
import com.example.util.now
import kotlinx.serialization.Serializable

@Serializable
data class Object(
    val id: String,
    var name: String,
    val fileUrl: String,
    val fileExtension: String,
    val imageUrl: String,
    val userEmail: String,
    var status: ObjectStatus = NONE,
    var printingStatus: PrintingStatus = PENDING,
    var quality: Quality = STANDARD,
    var quantity: Int = 1,
    var message: String = "",
    var slicing: Slicing = Slicing(),
    val trackingDetails: TrackingDetails = TrackingDetails(),
    val created_at: String = now()
)