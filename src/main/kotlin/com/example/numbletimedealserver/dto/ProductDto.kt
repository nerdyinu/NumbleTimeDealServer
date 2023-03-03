package com.example.numbletimedealserver.dto

import com.example.numbletimedealserver.domain.Product
import java.time.LocalTime
import java.util.*

data class ProductDto(val id: UUID, val name: String, val description: String, val appointedTime: LocalTime, val appointedQuantity:Long) {
    constructor(product: Product) : this(product.id, product.name, product.description, product.appointedTime, product.appointedQuantity)
}