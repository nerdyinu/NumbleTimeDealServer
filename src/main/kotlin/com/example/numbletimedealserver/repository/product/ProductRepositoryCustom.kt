package com.example.numbletimedealserver.repository.product

import com.example.numbletimedealserver.domain.Product
import com.example.numbletimedealserver.request.ProductUpdateRequest
import com.querydsl.jpa.impl.JPAQuery
import org.springframework.data.domain.Pageable
import java.time.LocalTime
import java.util.UUID

interface ProductRepositoryCustom {
    fun findAllByAppointedTimeBetween(start: LocalTime, end: LocalTime):List<Product>
    fun findAllByAdminId(adminId:UUID):List<Product>
    fun countAllByAdminId(adminId:UUID):JPAQuery<Long>
}