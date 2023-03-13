package com.example.numbletimedealserver.controller

import com.example.numblebankingserverchallenge.config.SessionLogin
import com.example.numbletimedealserver.domain.Product
import com.example.numbletimedealserver.domain.ROLE
import com.example.numbletimedealserver.dto.CustomerDto
import com.example.numbletimedealserver.dto.ProductDto
import com.example.numbletimedealserver.request.ProductListCondition
import com.example.numbletimedealserver.request.ProductRegisterRequest
import com.example.numbletimedealserver.request.ProductUpdateRequest
import com.example.numbletimedealserver.request.SignUpRequest
import com.example.numbletimedealserver.service.customer.CustomerService
import com.example.numbletimedealserver.service.product.ProductService
import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.support.PageableExecutionUtils
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.*
import org.springframework.test.web.servlet.delete
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

@ExtendWith(SpringExtension::class, MockKExtension::class)
@WebMvcTest(ProductController::class)
class ProductControllerUnitTest @Autowired constructor(val mapper: ObjectMapper) {

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockkBean
    lateinit var customerService: CustomerService

    @MockkBean
    lateinit var productService: ProductService
    val registerRequest = ProductRegisterRequest("book1", "empty", LocalTime.now().withNano(0), 100L)

    /*@PostMapping("/register")
    fun register(
        @RequestBody productRegisterRequest: ProductRegisterRequest,
        @SessionLogin(admin = true) admin: CustomerDto
    ): ProductDto = productService.register(admin.id, productRegisterRequest)
    */
    @BeforeEach
    fun setup() {

    }

    @Test
    fun `상품등록 성공`() {
        val productId = UUID.randomUUID()
        val adminId = UUID.randomUUID()
        every { productService.register(adminId, registerRequest) } returns ProductDto(
            productId,
            registerRequest.name,
            registerRequest.description,
            registerRequest.appointedTime,
            registerRequest.appointedQuantity
        )

        mockMvc.post("/register") {
            content = mapper.writeValueAsString(registerRequest)
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            sessionAttrs = mapOf("user" to CustomerDto(adminId, "inu", ROLE.ADMIN))
        }.andExpect {
            content { contentType(MediaType.APPLICATION_JSON) }
            status { isOk() }
            jsonPath("$.id") { isString() }
            jsonPath("$.name") { value(registerRequest.name) }
            jsonPath("$.description") { value(registerRequest.description) }
            jsonPath("$.appointedTime") { value(registerRequest.appointedTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"))) }
            jsonPath("$.appointedQuantity") { value(registerRequest.appointedQuantity) }
        }
        verify(exactly = 1) { productService.register(adminId, registerRequest) }
    }

    @Test
    fun `return 403 forbidden when not admin`() {
        val productId = UUID.randomUUID()
        val adminId = UUID.randomUUID()
        every { productService.register(adminId, registerRequest) } returns ProductDto(
            productId,
            registerRequest.name,
            registerRequest.description,
            registerRequest.appointedTime,
            registerRequest.appointedQuantity
        )
        mockMvc.post("/register") {
            content = mapper.writeValueAsString(registerRequest)
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            sessionAttrs = mapOf("user" to CustomerDto(adminId, "inu", ROLE.USER))
        }.andExpect {
            status { isForbidden() }

        }
        verify(exactly = 0) { productService.register(adminId, registerRequest) }
    }

    /*
    @PutMapping("/product/{productId}")
    fun update(
        @PathVariable("productId") productId: UUID,
        @RequestBody productUpdateRequest: ProductUpdateRequest,
        @SessionLogin(admin = true) admin: CustomerDto
    ): ProductDto = productService.update(productId = productId, adminId =admin.id, productUpdateRequest)
        */
    @Test
    fun `test update`() {
        val productId = UUID.randomUUID()
        val adminId = UUID.randomUUID()

        val updateRequest = ProductUpdateRequest("book1", "empty", LocalTime.now().withNano(0), 100L)
        every { productService.update(productId, adminId, updateRequest) } returns ProductDto(
            productId,
            updateRequest.name!!,
            updateRequest.description!!,
            updateRequest.appointedTime!!,
            updateRequest.appointedQuantity!!
        )

        mockMvc.put("/product/{productId}", productId) {
            content = mapper.writeValueAsString(updateRequest)
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            sessionAttrs = mapOf("user" to CustomerDto(adminId, "inu", ROLE.ADMIN))
        }.andExpect {
            content { contentType(MediaType.APPLICATION_JSON) }
            status { isOk() }
            jsonPath("$.id") { isString() }
            jsonPath("$.name") { value(updateRequest.name) }
            jsonPath("$.description") { value(updateRequest.description) }
            jsonPath("$.appointedTime") { value(updateRequest.appointedTime?.format(DateTimeFormatter.ofPattern("HH:mm:ss"))) }
            jsonPath("$.appointedQuantity") { value(updateRequest.appointedQuantity) }
        }
        verify(exactly = 1) { productService.update(productId, adminId, updateRequest) }
    }

    @Test
    fun `update returns 403 forbidden when not admin`() {
        val productId = UUID.randomUUID()
        val adminId = UUID.randomUUID()

        val updateRequest = ProductUpdateRequest("book1", "empty", LocalTime.now().withNano(0), 100L)
        every { productService.update(productId, adminId, updateRequest) } returns ProductDto(
            productId,
            updateRequest.name!!,
            updateRequest.description!!,
            updateRequest.appointedTime!!,
            updateRequest.appointedQuantity!!
        )

        mockMvc.put("/product/{productId}", productId) {
            content = mapper.writeValueAsString(updateRequest)
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            sessionAttrs = mapOf("user" to CustomerDto(adminId, "inu", ROLE.USER))
        }.andExpect {
            content { contentType(MediaType.APPLICATION_JSON) }
            status { isForbidden() }
        }
        verify(exactly = 0) { productService.update(productId, adminId, updateRequest) }
    }

    /*

        @DeleteMapping("/product/{productId}")
        fun delete(
            @PathVariable("productId") productId: UUID,
            @SessionLogin(admin = true) admin: CustomerDto
        ): ResponseEntity<String> {
            productService.delete(productId, admin.id)
            return ResponseEntity.ok().build()
        }
        */
    @Test
    fun `delete product succeed`() {
        val productId = UUID.randomUUID()
        val adminId = UUID.randomUUID()
        every { productService.delete(productId, adminId) } returns Unit

        mockMvc.delete("/product/{productId}", productId) {
            sessionAttrs = mapOf("user" to CustomerDto(adminId, "inu", ROLE.ADMIN))
        }.andExpect {
            status { isOk() }
        }
        verify(exactly = 1) { productService.delete(productId, adminId) }
    }

    @Test
    fun `delete return 403 forbidden when not admin`() {
        val productId = UUID.randomUUID()
        val adminId = UUID.randomUUID()
        every { productService.delete(productId, adminId) } returns Unit

        mockMvc.delete("/product/{productId}", productId) {
            sessionAttrs = mapOf("user" to CustomerDto(adminId, "inu", ROLE.USER))
        }.andExpect {
            status { isForbidden() }
        }
        verify(exactly = 0) { productService.delete(productId, adminId) }
    }

    /*
    @GetMapping("/product/{productId}")
    fun productDetail(
        @PathVariable("productId") productId: UUID,
        @SessionLogin customer: CustomerDto
    ): ResponseEntity<ProductDto> = productService.productDetail(productId).let { ResponseEntity.ok(it) }
    */
    @Test
    fun `getDetail Succeed`() {
        val productId = UUID.randomUUID()
        val adminId = UUID.randomUUID()
        val returnDto = ProductDto(
            productId,
            "book1",
            "empty",
            LocalTime.now().withNano(0),
            100L
        )
        every { productService.productDetail(productId) } returns returnDto

        mockMvc.get("/product/{productId}", productId) {
            sessionAttrs = mapOf("user" to CustomerDto(adminId, "inu", ROLE.USER))
        }.andExpect {
            status { isOk() }
            jsonPath("$.id") { value(productId.toString()) }
            jsonPath("$.name") { value(returnDto.name) }
            jsonPath("$.description") { value(returnDto.description) }
            jsonPath("$.appointedTime") { value(returnDto.appointedTime.toString()) }
            jsonPath("$.appointedQuantity") { value(returnDto.appointedQuantity) }
        }
        verify(exactly = 1) { productService.productDetail(productId) }
    }

    /*
    @GetMapping("/products/admin")
    fun productListAdmin(
        @SessionLogin(admin = true) admin: CustomerDto,
        pageable: Pageable
    ): ResponseEntity<Page<ProductDto>> =
        productService.getAllProductsRegistered(admin.id, pageable).let { ResponseEntity.ok(it) }
    */
    @Test
    fun `return registered list success`() {
        val adminId = UUID.randomUUID()
        val request = PageRequest.of(0, 10)
        val p1 = ProductDto(UUID.randomUUID(), "p1", "empty", LocalTime.now().withNano(0), 100L)
        val p2 = ProductDto(UUID.randomUUID(), "p2", "empty2", LocalTime.now().withNano(0), 100L)
        every {
            productService.getAllProductsRegistered(adminId, pageable = request)
        } returns PageableExecutionUtils.getPage(mutableListOf(p1, p2), request) { 2L }
        mockMvc.get("/products/admin"){
            sessionAttrs = mapOf("user" to CustomerDto(adminId,"inu", ROLE.ADMIN))
            accept= MediaType.APPLICATION_JSON
            param("page", "0")
            param("size", "10")
        }.andExpect {
            content { contentType(MediaType.APPLICATION_JSON) }
            status { isOk() }
            jsonPath("$.content[0].id"){value(p1.id.toString())}
            jsonPath("$.content[1].id"){value(p2.id.toString())}
        }
        verify(exactly = 1) {  productService.getAllProductsRegistered(adminId, pageable = request)}
    }
    @Test
    fun `return 403 when not admin`(){
        val adminId = UUID.randomUUID()
        val request = PageRequest.of(0, 10)
        val p1 = ProductDto(UUID.randomUUID(), "p1", "empty", LocalTime.now().withNano(0), 100L)
        val p2 = ProductDto(UUID.randomUUID(), "p2", "empty2", LocalTime.now().withNano(0), 100L)
        every {
            productService.getAllProductsRegistered(adminId, pageable = request)
        } returns PageableExecutionUtils.getPage(mutableListOf(p1, p2), request) { 2L }
        mockMvc.get("/products/admin"){
            sessionAttrs = mapOf("user" to CustomerDto(adminId,"inu", ROLE.USER))
            accept= MediaType.APPLICATION_JSON
            param("page", "0")
            param("size", "10")
        }.andExpect {
            content { contentType(MediaType.APPLICATION_JSON) }
            status { isForbidden() }
        }
        verify(exactly = 0) {  productService.getAllProductsRegistered(adminId, pageable = request)}
    }
    /*

    @GetMapping("/products/user")
    fun productListUser(
        @SessionLogin loginUser: CustomerDto,
        @ModelAttribute productListCondition: ProductListCondition,
        pageable: Pageable
    ): ResponseEntity<Page<ProductDto>> {

        return productService.getAllProductsBought(loginUser.id, productListCondition, pageable)
            .let { ResponseEntity.ok(it) }
    }*/

    @Test
    fun `return bought list success`() {
        val customerId = UUID.randomUUID()
        val request = PageRequest.of(0, 10)
        val cond = ProductListCondition(from= LocalDate.now().minusDays(10L))
        val p1 = ProductDto(UUID.randomUUID(), "p1", "empty", LocalTime.now().withNano(0), 100L)
        val p2 = ProductDto(UUID.randomUUID(), "p2", "empty2", LocalTime.now().withNano(0), 100L)
        every {
            productService.getAllProductsBought(customerId, cond,pageable = request)
        } returns PageableExecutionUtils.getPage(mutableListOf(p1, p2), request) { 2L }
        mockMvc.get("/products/user"){
            sessionAttrs = mapOf("user" to CustomerDto(customerId,"inu", ROLE.USER))
            accept= MediaType.APPLICATION_JSON
            param("page", request.pageNumber.toString())
            param("size", request.pageSize.toString())
            param("from", cond.from.toString())
            param("to", cond.to.toString())
        }.andExpect {
            content { contentType(MediaType.APPLICATION_JSON) }
            status { isOk() }
            jsonPath("$.content[0].id"){value(p1.id.toString())}
            jsonPath("$.content[1].id"){value(p2.id.toString())}
        }
        verify(exactly = 1) {  productService.getAllProductsBought(customerId, cond,pageable = request)}
    }
}