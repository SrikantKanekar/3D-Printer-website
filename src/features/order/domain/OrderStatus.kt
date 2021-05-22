package com.example.features.order.domain

enum class OrderStatus {
    PLACED, CONFIRMED, PROCESSING, DELIVERING, DELIVERED
}

enum class PrintingStatus {
    PENDING, PRINTING, PRINTED
}