package com.example.numbletimedealserver.repository.order

import com.example.numbletimedealserver.domain.Order
import com.example.numbletimedealserver.request.ProductListCondition
import com.querydsl.jpa.impl.JPAQuery
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.time.LocalDate
import java.util.*

interface OrderRepositoryCustom {
    fun findAllByCustomerId(customerId:UUID,productListCondition: ProductListCondition): List<Order>
    fun countByCustomerId(customerId: UUID,productListCondition: ProductListCondition): JPAQuery<Long>
    fun pageByCustomerId(customerId:UUID, productListCondition: ProductListCondition, pageable: Pageable):Page<Order>

    fun findAllByProductIdAndDate(productId:UUID, orderDate:LocalDate):List<Order>
    fun countAllByProductIdAndDate(productId:UUID, orderDate:LocalDate):JPAQuery<Long>
    fun pageByProductIdAndDate(productId:UUID, orderDate:LocalDate,pageable: Pageable):Page<Order>
}