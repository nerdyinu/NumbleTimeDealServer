package com.example.numbletimedealserver.repository.product

import com.example.numbletimedealserver.domain.Product
import com.example.numbletimedealserver.domain.QProduct
import com.example.numbletimedealserver.domain.QProduct.*

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Service
import java.time.LocalTime
import java.util.UUID


@Service
class ProductRepositoryCustomImpl(private val jpaQueryFactory: JPAQueryFactory) :ProductRepositoryCustom{

    override fun findAllByAppointedTimeBetween(start:LocalTime, end:LocalTime):List<Product> {
      return   jpaQueryFactory.selectFrom(product).where(product.appointedTime.between(start,end)).fetch()
    }


}