package com.example.numbletimedealserver.service.product

import com.example.numblebankingserverchallenge.exception.CustomException
import com.example.numbletimedealserver.domain.Customer
import com.example.numbletimedealserver.domain.Product
import com.example.numbletimedealserver.dto.ProductDto
import com.example.numbletimedealserver.dto.ProductListCondition
import com.example.numbletimedealserver.repository.order.OrderRepository
import com.example.numbletimedealserver.repository.product.ProductRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.support.PageableExecutionUtils
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

@Service
class ProductServiceImpl(
    private val productRepository: ProductRepository,
    private val orderRepository: OrderRepository
) : ProductService {
    @Transactional
    override fun update(productId: UUID): ProductDto {
        val product =
            productRepository.findById(productId).orElse(null) ?: throw CustomException.AccountNotFoundException()
        product.dailyUpdate()
        return ProductDto(product)
    }

    override fun findAllByAppointedTime(start: LocalTime, end: LocalTime): List<ProductDto> {
        return productRepository.findAllByAppointedTimeBetween(start, end).map(::ProductDto)
    }

    override fun getAllBuyers(productId: UUID, pageable: Pageable): Page<Customer> {
        val content = orderRepository.findAllByProductIdAndDate(productId, LocalDate.now()).map { it.customer }
        val countQuery = orderRepository.countAllByProductIdAndDate(productId, LocalDate.now())
        return PageableExecutionUtils.getPage(content, pageable) { countQuery.fetchOne() ?: 0L }
    }

    override fun getAllProducts(
        customerId: UUID,
        productListCondition: ProductListCondition,
        pageable: Pageable
    ): Page<Product> {
        val content = orderRepository.findAllByCustomerId(customerId, productListCondition).map { it.product }
        val countQuery = orderRepository.countByCustomerId(customerId, productListCondition)
        return PageableExecutionUtils.getPage(content, pageable) { countQuery.fetchOne() ?: 0L }
    }
}