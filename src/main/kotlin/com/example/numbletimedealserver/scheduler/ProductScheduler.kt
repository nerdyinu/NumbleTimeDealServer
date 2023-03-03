package com.example.numbletimedealserver.scheduler

import com.example.numbletimedealserver.service.product.ProductService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalTime
import java.time.temporal.ChronoUnit

@Component
class ProductScheduler
    (private val productService: ProductService){
    @Scheduled(cron= "0 0/1 * * *")
    @Transactional
    fun update() {
        val now = LocalTime.now()
        val min = now.truncatedTo(ChronoUnit.MINUTES)
        val end = min.plusMinutes(1)

        productService.findAllByAppointedTime(min,end).asSequence().onEach {
            productService.update(it.id)
        }
    }
}