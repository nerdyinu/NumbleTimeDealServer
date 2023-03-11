package com.example.numbletimedealserver.request

import com.fasterxml.jackson.annotation.JsonFormat
import java.io.Serializable
import java.time.LocalTime

data class ProductRegisterRequest(
    val name: String,
    val description: String,
    val appointedTime: LocalTime,
    val appointedQuantity: Long
): Serializable
