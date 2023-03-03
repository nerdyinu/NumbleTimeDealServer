package com.example.numbletimedealserver.service

import com.example.numbletimedealserver.dto.ProductDto
import java.time.LocalTime
import java.util.UUID

interface ProductService {
    fun update(productId:UUID):ProductDto
    fun findAllByAppointedTime(start:LocalTime, end:LocalTime):List<ProductDto>
}