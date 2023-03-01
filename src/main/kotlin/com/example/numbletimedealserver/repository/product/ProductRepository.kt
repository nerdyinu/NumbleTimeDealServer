package com.example.numbletimedealserver.repository.product

import com.example.numbletimedealserver.domain.Product
import com.example.numbletimedealserver.repository.MyBaseRepository
import java.time.LocalTime
import java.util.*


interface ProductRepository: MyBaseRepository<Product, UUID>
{
    fun findAllByAppointedTimeBetween(start:LocalTime, end:LocalTime):List<Product>
}