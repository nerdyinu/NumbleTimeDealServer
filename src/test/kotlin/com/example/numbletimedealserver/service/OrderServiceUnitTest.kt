package com.example.numbletimedealserver.service

import com.example.numbletimedealserver.domain.Customer
import com.example.numbletimedealserver.domain.Order
import com.example.numbletimedealserver.domain.Product
import com.example.numbletimedealserver.domain.ROLE
import com.example.numbletimedealserver.dto.OrderDto
import com.example.numbletimedealserver.exception.CustomException
import com.example.numbletimedealserver.repository.customer.CustomerRepository
import com.example.numbletimedealserver.repository.order.OrderRepository
import com.example.numbletimedealserver.repository.product.ProductRepository
import com.example.numbletimedealserver.service.order.OrderServiceImpl
import io.mockk.EqMatcher
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockkConstructor
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.LocalTime
import java.util.*

@ExtendWith(SpringExtension::class, MockKExtension::class)
class OrderServiceUnitTest {
    @InjectMockKs(overrideValues = true, injectImmutable = true)
    lateinit var orderService:OrderServiceImpl

    @MockK
    lateinit var productRepository: ProductRepository

    @MockK
    lateinit var customerRepository: CustomerRepository
    @MockK
    lateinit var orderRepository: OrderRepository

    val admin = Customer("inu", "12345", ROLE.ADMIN)
    val product = Product("book1", "empty1", LocalTime.now().withNano(0), 100L, admin)

    /*
    *     @Transactional
    override fun createOrder(customerId: UUID, productId: UUID): OrderDto {
        val product =
            productRepository.findByIdLockOption(productId,true) ?: throw CustomException.ProductNotFoundException()
        val customer =
            customerRepository.findById(customerId).orElse(null) ?: throw CustomException.UserNotFoundException()
        product.descStock()
        return orderRepository.save(Order(customer, product)).let(::OrderDto)
    }
    * */
    @Test
    fun `createOrder success`(){
        val origin = product.stockQuantity
        val order = Order(admin,product)
        mockkConstructor(Order::class)
        every { productRepository.findByIdLockOption(product.id,true) } returns product
        every { customerRepository.findById(admin.id) } returns Optional.of(admin)
        every { orderRepository.save(any()) } returns order
        assertThat(orderService.createOrder(admin.id,product.id)).isEqualTo(OrderDto(order))
        assertThat(product.stockQuantity).isEqualTo(origin-1L)
        verify(exactly = 1) { productRepository.findByIdLockOption(product.id, true) }
        verify(exactly = 1) { customerRepository.findById(admin.id) }
        verify(exactly = 1) { orderRepository.save(any()) }
    }
    @Test
    fun `createorder returns 404 when product id not found`(){
        val origin = product.stockQuantity
        val order = Order(admin,product)
        every { productRepository.findByIdLockOption(product.id,true) } returns null
        every { customerRepository.findById(admin.id) } returns Optional.of(admin)
        every { orderRepository.save(any()) } returns order
        assertThrows<CustomException.ProductNotFoundException>{orderService.createOrder(admin.id,product.id)}

        verify(exactly = 1) { productRepository.findByIdLockOption(product.id, true) }
        verify(exactly = 0) { customerRepository.findById(admin.id) }
        verify(exactly = 0) { orderRepository.save(any()) }
    }
    @Test
    fun `createOrder throws 404 when customer id not found`(){
        val origin = product.stockQuantity
        val order = Order(admin,product)
        every { productRepository.findByIdLockOption(product.id,true) } returns product
        every { customerRepository.findById(admin.id) } returns Optional.empty()
        every { orderRepository.save(any()) } returns order
        assertThrows<CustomException.UserNotFoundException>{orderService.createOrder(admin.id,product.id)}

        verify(exactly = 1) { productRepository.findByIdLockOption(product.id, true) }
        verify(exactly = 1) { customerRepository.findById(admin.id) }
        verify(exactly = 0) { orderRepository.save(any()) }
    }


}