package com.example.numbletimedealserver.request

import java.time.LocalTime
import java.util.*

data class ProductRegisterRequest(
    val name: String,
    val description: String,
    val appointedTime: LocalTime,
    val appointedQuantity: Long
)
