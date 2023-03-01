package com.example.numbletimedealserver.service

import com.example.numbletimedealserver.domain.Product
import com.example.numbletimedealserver.repository.product.ProductRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit

@Service
class ProductServiceImpl(private val productRepository: ProductRepository) :ProductService{
    @Scheduled(cron= "0 0/5 * * *")
    @Transactional
    override fun update() {
        val now = LocalTime.now()
        val min = now.truncatedTo(ChronoUnit.MINUTES)
        val end = min.plusMinutes(5)
        productRepository.findAllByAppointedTimeBetween(min,end).forEach(Product::dailyUpdate)
    }
}