package com.example.numbletimedealserver.service.product

import com.example.numblebankingserverchallenge.exception.CustomException
import com.example.numbletimedealserver.domain.Customer
import com.example.numbletimedealserver.domain.Product
import com.example.numbletimedealserver.dto.CustomerDto
import com.example.numbletimedealserver.dto.ProductDto
import com.example.numbletimedealserver.repository.customer.CustomerRepository
import com.example.numbletimedealserver.request.ProductListCondition
import com.example.numbletimedealserver.repository.order.OrderRepository
import com.example.numbletimedealserver.repository.product.ProductRepository
import com.example.numbletimedealserver.request.ProductRegisterRequest
import com.example.numbletimedealserver.request.ProductUpdateRequest
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
    private val orderRepository: OrderRepository,
    private val customerRepository: CustomerRepository,
) : ProductService {
    @Transactional
    override fun update(productId: UUID, adminId: UUID, productUpdateRequest: ProductUpdateRequest): ProductDto {
        val product =
            productRepository.findById(productId).orElse(null) ?: throw CustomException.ProductNotFoundException()
        if (adminId != product.admin.id) throw CustomException.ForbiddenException()
        val (name, desc, time, quantity) = productUpdateRequest
        name?.let { product.updateName(it) }
        desc?.let { product.updateDesc(it) }
        time?.let { product.updateTime(it) }
        quantity?.let { product.updateQuantity(it) }
        return ProductDto(product)
    }

    @Transactional
    override fun dailyUpdate(productId: UUID): ProductDto {
        val product =
            productRepository.findById(productId).orElse(null) ?: throw CustomException.ProductNotFoundException()
        product.dailyUpdate()
        return ProductDto(product)
    }

    override fun findAllByAppointedTime(start: LocalTime, end: LocalTime): List<ProductDto> {
        return productRepository.findAllByAppointedTimeBetween(start, end).map(::ProductDto)
    }

    override fun register(adminId: UUID, productRegisterRequest: ProductRegisterRequest): ProductDto {
        val (name, desc, time, quantity) = productRegisterRequest
        val admin = customerRepository.findById(adminId).orElse(null) ?: throw CustomException.UserNotFoundException()
        return productRepository.save(Product(name, desc, time, quantity, admin)).let(::ProductDto)
    }

    override fun getAllBuyers(productId: UUID, pageable: Pageable): Page<CustomerDto> {
        val content =
            orderRepository.findAllByProductIdAndDate(productId, LocalDate.now()).map { it.customer.let(::CustomerDto) }
        val countQuery = orderRepository.countAllByProductIdAndDate(productId, LocalDate.now())
        return PageableExecutionUtils.getPage(content, pageable) { countQuery.fetchOne() ?: 0L }
    }

    override fun getAllProductsBought(
        customerId: UUID,
        productListCondition: ProductListCondition,
        pageable: Pageable
    ): Page<ProductDto> {

        val (from, to) = productListCondition
        from?.let { if (it.isAfter(LocalDate.now())) throw CustomException.BadRequestException() }
        val content =
            orderRepository.findAllByCustomerId(customerId, productListCondition).map { it.product.let(::ProductDto) }
        val countQuery = orderRepository.countByCustomerId(customerId, productListCondition)
        return PageableExecutionUtils.getPage(content, pageable) { countQuery.fetchOne() ?: 0L }
    }

    override fun getAllProductsRegistered(adminId: UUID, pageable: Pageable): Page<ProductDto> {
        val content = productRepository.findAllByAdminId(adminId).map(::ProductDto)
        val countQuery = productRepository.countAllByAdminId(adminId)
        return PageableExecutionUtils.getPage(content, pageable) { countQuery.fetchOne() ?: 0L }
    }
}