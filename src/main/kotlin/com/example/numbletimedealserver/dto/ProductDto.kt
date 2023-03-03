package com.example.numbletimedealserver.dto

import com.example.numbletimedealserver.domain.Product
import com.fasterxml.jackson.databind.BeanDescription
import java.time.LocalTime
import java.util.UUID

data class ProductDto(val id:UUID, val name:String, val description: String, val appointedTime:LocalTime) {
    constructor(product:Product):this(product.id,product.name,product.description,product.appointedTime)
}