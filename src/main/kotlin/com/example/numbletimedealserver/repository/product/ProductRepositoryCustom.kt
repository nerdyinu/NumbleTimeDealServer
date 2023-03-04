package com.example.numbletimedealserver.repository.product

import com.example.numbletimedealserver.domain.Product
import com.querydsl.jpa.impl.JPAQuery
import java.time.LocalTime
import java.util.*

interface ProductRepositoryCustom {
    fun findAllByAppointedTimeBetween(start: LocalTime, end: LocalTime):List<Product>
    fun findAllByAdminId(adminId:UUID):List<Product>
    fun countAllByAdminId(adminId:UUID):JPAQuery<Long>
    fun findByIdAndAdminId(productId:UUID, adminId:UUID):Product?
    fun findByIdLockOption(productId: UUID, isLock:Boolean):Product?
}