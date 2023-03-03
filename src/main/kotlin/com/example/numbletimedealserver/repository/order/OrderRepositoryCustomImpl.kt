package com.example.numbletimedealserver.repository.order

import com.example.numbletimedealserver.domain.Order
import com.example.numbletimedealserver.domain.QOrder
import com.example.numbletimedealserver.domain.QOrder.*
import com.example.numbletimedealserver.dto.ProductListCondition
import com.querydsl.jpa.impl.JPAQuery
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.support.PageableExecutionUtils
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.util.*

@Service
class OrderRepositoryCustomImpl(private val jpaQueryFactory: JPAQueryFactory) :OrderRepositoryCustom{
    override fun findAllByCustomerId(customerId: UUID,productListCondition: ProductListCondition): List<Order> {
        val(from,to)=productListCondition
        val start = from?.let { it.atStartOfDay() }
        val end = to?.let { it.atStartOfDay().plusDays(1L) }
        return jpaQueryFactory.selectFrom(order).where(
            order.customer.id.eq(customerId),
            start?.let { s->end?.let{e-> order.createdDate.between(s,e)} }
        ).fetch()
    }

    override fun countByCustomerId(customerId: UUID,productListCondition: ProductListCondition): JPAQuery<Long> {
        val(from,to)=productListCondition
        val start = from?.let { it.atStartOfDay() }
        val end = to?.let { it.atStartOfDay().plusDays(1L) }
        return jpaQueryFactory.select(order.count()).from(order).where(
            order.customer.id.eq(customerId),
            start?.let { s->end?.let{e-> order.createdDate.between(s,e)} }
            )
    }

    override fun pageByCustomerId(customerId: UUID, productListCondition: ProductListCondition,pageable: Pageable): Page<Order> {
        val content = findAllByCustomerId(customerId,productListCondition)
        val countQuery = countByCustomerId(customerId,productListCondition)
        return PageableExecutionUtils.getPage(content,pageable){ countQuery.fetchOne()?:0L}
    }

    override fun findAllByProductIdAndDate(productId: UUID, orderDate: LocalDate) {
        TODO("Not yet implemented")
    }
}