package com.example.numbletimedealserver.service

import com.example.numbletimedealserver.domain.Customer
import com.example.numbletimedealserver.domain.Order
import com.example.numbletimedealserver.domain.Product
import com.example.numbletimedealserver.domain.ROLE
import com.example.numbletimedealserver.dto.CustomerDto
import com.example.numbletimedealserver.dto.OrderDto
import com.example.numbletimedealserver.dto.ProductDto
import com.example.numbletimedealserver.exception.CustomException
import com.example.numbletimedealserver.repository.customer.CustomerRepository
import com.example.numbletimedealserver.repository.order.OrderRepository
import com.example.numbletimedealserver.repository.product.ProductRepository
import com.example.numbletimedealserver.request.ProductListCondition
import com.example.numbletimedealserver.request.ProductRegisterRequest
import com.example.numbletimedealserver.request.ProductUpdateRequest
import com.example.numbletimedealserver.service.product.ProductService
import com.example.numbletimedealserver.service.product.ProductServiceImpl
import com.ninjasquad.springmockk.MockkBean
import com.querydsl.jpa.impl.JPAQuery
import com.querydsl.jpa.impl.JPAQueryFactory
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.support.PageableExecutionUtils
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

@ExtendWith( MockKExtension::class,SpringExtension::class)
class ProductServiceUnitTest {


    @InjectMockKs(overrideValues = true, injectImmutable = true)
    lateinit var productService: ProductServiceImpl
    @MockK
    lateinit var productRepository: ProductRepository
    @MockK
    lateinit var orderRepository: OrderRepository
    @MockK
    lateinit var customerRepository: CustomerRepository


    val admin = Customer("inu", "12345", ROLE.ADMIN)
    val product = Product("book1", "empty1", LocalTime.now().withNano(0), 100L, admin)






    /*@Transactional
    override fun update(productId: UUID, adminId: UUID, productUpdateRequest: ProductUpdateRequest): ProductDto {
        val product =
            productRepository.findByIdLockOption(productId,true)?: throw CustomException.ProductNotFoundException()
        productUpdateRequest.also { (name, desc, time, quantity) ->
            product.update(
                time = time,
                quantity = quantity,
                newName = name,
                desc = desc
            )
        }

        return ProductDto(product)
    }
    */
    @Test
    fun `update success`() {
        val updateRequest = ProductUpdateRequest("book2", "empty2", LocalTime.now().minusHours(1L).withNano(0), 100L)

        every { productRepository.findByIdLockOption(product.id, true) } returns product
        val updated = productService.update(product.id, admin.id, updateRequest)
        updated.let { (id, name, desc, time, quantity) ->
            assertThat(id).isEqualTo(product.id)
            assertThat(name).isEqualTo(updateRequest.name)
            assertThat(desc).isEqualTo(updateRequest.description)
            assertThat(time).isEqualTo(updateRequest.appointedTime)
            assertThat(quantity).isEqualTo(updateRequest.appointedQuantity)
        }
        verify(exactly = 1) { productRepository.findByIdLockOption(product.id, true) }
    }

    @Test
    fun `throws 404 when product not found`() {
        val updateRequest = ProductUpdateRequest("book2", "empty2", LocalTime.now().minusHours(1L).withNano(0), 100L)

        every { productRepository.findByIdLockOption(product.id, true) } returns null
        assertThrows<CustomException.ProductNotFoundException> {
            productService.update(
                product.id,
                admin.id,
                updateRequest
            )
        }
        verify(exactly = 1) { productRepository.findByIdLockOption(product.id, true) }
    }

    /*

    override fun register(adminId: UUID, productRegisterRequest: ProductRegisterRequest): ProductDto {
        val (name, desc, time, quantity) = productRegisterRequest
        val admin = customerRepository.findById(adminId).orElse(null) ?: throw CustomException.UserNotFoundException()
        return productRepository.save(Product(name, desc, time, quantity, admin)).let(com.example.numbletimedealserver.dto::ProductDto)
    }*/
    @Test
    fun `register returns ProductDto`() {

        val registerRequest =
            ProductRegisterRequest(product.name, product.description, product.appointedTime, product.appointedQuantity)
        val product = Product(
            registerRequest.name,
            registerRequest.description,
            registerRequest.appointedTime,
            registerRequest.appointedQuantity,
            admin
        )

        every { customerRepository.findById(admin.id) } returns Optional.of(admin)
        every { productRepository.save(any()) } returns product
        assertThat(productService.register(admin.id, registerRequest)).isEqualTo(ProductDto(product))
        verify(exactly = 1) { productRepository.save(any()) }
        verify(exactly = 1) { customerRepository.findById(admin.id) }
    }

    @Test
    fun `throws 404 when admin id not found`() {
        val registerRequest =
            ProductRegisterRequest(product.name, product.description, product.appointedTime, product.appointedQuantity)
        every { customerRepository.findById(admin.id) } returns Optional.empty()
        assertThrows<CustomException.UserNotFoundException> { productService.register(admin.id, registerRequest) }
        verify(exactly = 1) { customerRepository.findById(admin.id) }
    }

    /*

    override fun getAllBuyers(productId: UUID, pageable: Pageable): Page<CustomerDto> {
        val content =
            orderRepository.findAllByProductIdAndDate(productId, LocalDate.now()).map { it.customer.let(com.example.numbletimedealserver.dto::CustomerDto) }
        val countQuery = orderRepository.countAllByProductIdAndDate(productId, LocalDate.now())
        return PageableExecutionUtils.getPage(content, pageable) { countQuery.fetchOne() ?: 0L }
    }
    */
    @Test
    fun `getAllBuyers success`() {
        val orders = List(4) { Order(admin, product) }
        val request = PageRequest.of(0, 10)

        every { orderRepository.findAllByProductIdAndDate(product.id, LocalDate.now()) } returns orders
        every { orderRepository.countAllByProductIdAndDate(product.id, LocalDate.now()) } returns JPAQuery<Long>()

        val pageRes = productService.getAllBuyers(productId = product.id, request)
        assertThat(pageRes.size).isEqualTo(request.pageSize)
        val dtos = orders.asSequence().map { it.customer }.map(::CustomerDto).toList()
        assertThat(pageRes.content).containsAll(dtos)
        assertThat(pageRes.totalElements).isEqualTo(orders.size.toLong())
        verify(exactly = 1) { orderRepository.findAllByProductIdAndDate(product.id, LocalDate.now()) }
        verify(exactly = 1) { orderRepository.countAllByProductIdAndDate(product.id, LocalDate.now()) }

    }

    /*
    override fun getAllProductsBought(
        customerId: UUID,
        productListCondition: ProductListCondition,
        pageable: Pageable
    ): Page<ProductDto> {

        val (from, to) = productListCondition
        from?.let { if (it.isAfter(LocalDate.now())) throw CustomException.BadRequestException() }
        val content =
            orderRepository.findAllByCustomerId(customerId, productListCondition).map { it.product.let(com.example.numbletimedealserver.dto::ProductDto) }
        val countQuery = orderRepository.countByCustomerId(customerId, productListCondition)
        return PageableExecutionUtils.getPage(content, pageable) { countQuery.fetchOne() ?: 0L }
    }
    */
    @Test
    fun `getAllProductsBought`() {
        val condition = ProductListCondition(LocalDate.now().minusMonths(1L))
        val request = PageRequest.of(0, 10)
        val list = MutableList(5) { Order(admin, product) }
        every { orderRepository.findAllByCustomerId(admin.id, condition) } returns list
        every { orderRepository.countByCustomerId(admin.id, condition) } returns JPAQuery<Long>()
        val res = productService.getAllProductsBought(admin.id, condition, request)
        assertThat(res.size).isEqualTo(request.pageSize)
        val dtos = list.asSequence().map { it.product }.map(::ProductDto).toList()
        assertThat(res.content).containsAll(dtos)
        assertThat(res.totalElements).isEqualTo(list.size.toLong())
        verify(exactly = 1) { orderRepository.findAllByCustomerId(admin.id, condition) }
        verify(exactly = 1) { orderRepository.countByCustomerId(admin.id, condition) }
    }

    @Test
    fun `when from condition is after now, throws 400`() {
        val condition = ProductListCondition(LocalDate.now().plusDays(10L))
        val request = PageRequest.of(0, 10)
        val list = MutableList(5) { Order(admin, product) }
        every { orderRepository.findAllByCustomerId(admin.id, condition) } returns list
        every { orderRepository.countByCustomerId(admin.id, condition) } returns JPAQuery<Long>()
        assertThrows<CustomException.BadRequestException> {
            productService.getAllProductsBought(
                admin.id,
                condition,
                request
            )
        }
        verify(exactly = 0) { orderRepository.findAllByCustomerId(admin.id, condition) }
        verify(exactly = 0) { orderRepository.countByCustomerId(admin.id, condition) }
    }

    /*
    override fun getAllProductsRegistered(adminId: UUID, pageable: Pageable): Page<ProductDto> {
        val content = productRepository.findAllByAdminId(adminId).map(com.example.numbletimedealserver.dto::ProductDto)
        val countQuery = productRepository.countAllByAdminId(adminId)
        return PageableExecutionUtils.getPage(content, pageable) { countQuery.fetchOne() ?: 0L }
    }*/
    @Test
    fun `getAllProductsRegistered success`() {
        val list = List(4) { Product("book1", "empty", LocalTime.now(), 100L, admin) }
        val request = PageRequest.of(0, 10)
        every { productRepository.findAllByAdminId(admin.id) } returns list
        every { productRepository.countAllByAdminId(admin.id) } returns JPAQuery<Long>()
        val res = productService.getAllProductsRegistered(admin.id, request)
        assertThat(res.size).isEqualTo(request.pageSize)
        assertThat(res.totalElements).isEqualTo(list.size.toLong())
        val dtos = list.asSequence().map(::ProductDto).toList()
        assertThat(res.content).containsAll(dtos)
        verify(exactly = 1) { productRepository.findAllByAdminId(admin.id) }
        verify(exactly = 1) { productRepository.countAllByAdminId(admin.id) }
    }

    /*

    override fun delete(productId: UUID, adminId: UUID) {
        productRepository.findByIdAndAdminId(productId, adminId) ?: throw CustomException.ProductNotFoundException()
        productRepository.deleteById(productId)
    }
    */
    @Test
    fun `delete success`() {
        every { productRepository.findByIdAndAdminId(productId = product.id, admin.id) } returns product
        every { productRepository.deleteById(product.id) } returns Unit
        productService.delete(product.id, admin.id)
        verify(exactly = 1) { productRepository.findByIdAndAdminId(product.id, admin.id) }
        verify(exactly = 1) { productRepository.deleteById(product.id) }
    }

    @Test
    fun `delete throws 404 when product id not found`() {
        every { productRepository.findByIdAndAdminId(productId = product.id, admin.id) } returns null
        every { productRepository.deleteById(product.id) } returns Unit
        assertThrows<CustomException.ProductNotFoundException> { productService.delete(product.id, admin.id) }
        verify(exactly = 1) { productRepository.findByIdAndAdminId(product.id, admin.id) }
        verify(exactly = 0) { productRepository.deleteById(product.id) }
    }

    /*
    override fun productDetail(productId: UUID): ProductDto =
        productRepository.findById(productId).orElse(null)?.let(com.example.numbletimedealserver.dto::ProductDto)
            ?: throw CustomException.ProductNotFoundException()
    */
    @Test
    fun `productDetail success`() {
        every { productRepository.findById(product.id) } returns Optional.of(product)
        val res = productService.productDetail(product.id)
        assertThat(res).isEqualTo(ProductDto(product))
        verify(exactly = 1) { productRepository.findById(product.id) }
    }

    @Test
    fun `productDetail throws 404 when product id not found`() {
        every { productRepository.findById(product.id) } returns Optional.empty()
        assertThrows<CustomException.ProductNotFoundException> { productService.productDetail(product.id) }
        verify(exactly = 1) { productRepository.findById(product.id) }
    }
}