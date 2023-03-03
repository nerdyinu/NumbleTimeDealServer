package com.example.numbletimedealserver.repository.product

import com.example.numbletimedealserver.domain.Product
import java.time.LocalTime

interface ProductRepositoryCustom {
    fun findAllByAppointedTimeBetween(start: LocalTime, end: LocalTime):List<Product>
}