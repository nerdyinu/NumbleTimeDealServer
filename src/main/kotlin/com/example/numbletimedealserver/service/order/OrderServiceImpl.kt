package com.example.numbletimedealserver.service.order

import com.example.numbletimedealserver.domain.Order
import com.example.numbletimedealserver.dto.OrderDto
import com.example.numbletimedealserver.exception.CustomException
import com.example.numbletimedealserver.repository.customer.CustomerRepository
import com.example.numbletimedealserver.repository.order.OrderRepository
import com.example.numbletimedealserver.repository.product.ProductRepository
import com.example.numbletimedealserver.request.OrderRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class OrderServiceImpl(
    private val customerRepository: CustomerRepository,
    private val orderRepository: OrderRepository,
    private val productRepository: ProductRepository
) : OrderService {
    @Transactional
    override fun createOrder(customerId: UUID, productId: UUID): OrderDto {
        val product =
            productRepository.findByIdLockOption(customerId, true) ?: throw CustomException.ProductNotFoundException()
        val customer =
            customerRepository.findById(customerId).orElse(null) ?: throw CustomException.UserNotFoundException()
        product.descStock()
        return orderRepository.save(Order(customer, product)).let(::OrderDto)
    }
}