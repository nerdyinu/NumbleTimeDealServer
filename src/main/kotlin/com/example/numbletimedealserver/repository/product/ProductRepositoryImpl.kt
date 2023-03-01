package com.example.numbletimedealserver.repository.product

import com.example.numbletimedealserver.domain.Product
import com.example.numbletimedealserver.domain.QCustomer
import com.example.numbletimedealserver.domain.QCustomer.*
import com.example.numbletimedealserver.domain.QProduct
import com.example.numbletimedealserver.domain.QProduct.*
import com.querydsl.jpa.impl.JPAQueryFactory
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.UUID


class ProductRepositoryImpl(private val jpaQueryFactory: JPAQueryFactory) :ProductRepository{

    override fun findAllByAppointedTimeBetween(start:LocalTime, end:LocalTime):List<Product> {
      return   jpaQueryFactory.selectFrom(product).where(product.appointedTime.between(start,end)).fetch()
    }

    override fun findById(id: UUID): Product? =jpaQueryFactory.selectFrom(product).where(product.id.eq(id)).fetchOne()


    override fun <S : Product> save(entity: S)  {
        val res  = jpaQueryFactory.insert(product).values(entity.id,entity.appointedTime,entity.name,entity.description,entity.stockQuantity).execute()
    }
}