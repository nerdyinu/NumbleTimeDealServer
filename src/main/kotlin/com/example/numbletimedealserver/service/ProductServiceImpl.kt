package com.example.numbletimedealserver.service

import com.example.numblebankingserverchallenge.exception.CustomException
import com.example.numbletimedealserver.domain.Product
import com.example.numbletimedealserver.dto.ProductDto
import com.example.numbletimedealserver.repository.product.ProductRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import java.util.UUID

@Service
class ProductServiceImpl(private val productRepository: ProductRepository) :ProductService{
    @Transactional
    override fun update(productId:UUID):ProductDto{
        val product = productRepository.findById(productId).orElse(null) ?: throw CustomException.AccountNotFoundException()
        product.dailyUpdate()
        return ProductDto(product)
    }

    override fun findAllByAppointedTime(start: LocalTime, end: LocalTime): List<ProductDto> {
        return productRepository.findAllByAppointedTimeBetween(start,end).map(::ProductDto)
    }
}