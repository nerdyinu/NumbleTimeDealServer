package com.example.numbletimedealserver.repository.product

import com.example.numbletimedealserver.domain.Product
import com.example.numbletimedealserver.repository.MyBaseRepository
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalTime
import java.util.*


interface ProductRepository: JpaRepository<Product, UUID>, ProductRepositoryCustom
{
    fun findAllByAppointedTimeBetween(start:LocalTime, end:LocalTime):List<Product>
}