package com.example.numbletimedealserver.controller

import com.example.numblebankingserverchallenge.config.SessionLogin
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
import org.springframework.data.domain.Pageable
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import org.springframework.web.bind.annotation.*
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

    @MockkBean(relaxed = true)
    lateinit var productService: ProductService

    /*@PostMapping("/register")
    fun register(
        @RequestBody productRegisterRequest: ProductRegisterRequest,
        @SessionLogin(admin = true) admin: CustomerDto
    ): ProductDto = productService.register(admin.id, productRegisterRequest)
    */

    @Test
    fun `상품등록 성공`() {
        val productId = UUID.randomUUID()
        val adminId = UUID.randomUUID()
        val registerRequest = ProductRegisterRequest("book1", "empty", LocalTime.now(), 100L)

        every { productService.register(any(),any()) } returns ProductDto(
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

    /*
    @PutMapping("/product/{productId}")
    fun update(
        @PathVariable("productId") productId: UUID,
        @RequestBody productUpdateRequest: ProductUpdateRequest,
        @SessionLogin(admin = true) admin: CustomerDto
    ): ProductDto = productService.update(productId = productId, adminId =admin.id, productUpdateRequest)


    @DeleteMapping("/product/{productId}")
    fun delete(
        @PathVariable("productId") productId: UUID,
        @SessionLogin(admin = true) admin: CustomerDto
    ): ResponseEntity<String> {
        productService.delete(productId, admin.id)
        return ResponseEntity.ok().build()
    }

    @GetMapping("/product/{productId}")
    fun productDetail(
        @PathVariable("productId") productId: UUID,
        @SessionLogin customer: CustomerDto
    ): ResponseEntity<ProductDto> = productService.productDetail(productId).let { ResponseEntity.ok(it) }

    @GetMapping("/products/admin")
    fun productListAdmin(
        @SessionLogin(admin = true) admin: CustomerDto,
        pageable: Pageable
    ): ResponseEntity<Page<ProductDto>> =
        productService.getAllProductsRegistered(admin.id, pageable).let { ResponseEntity.ok(it) }


    @GetMapping("/products/user")
    fun productListUser(
        @SessionLogin loginUser: CustomerDto,
        @ModelAttribute productListCondition: ProductListCondition,
        pageable: Pageable
    ): ResponseEntity<Page<ProductDto>> {

        return productService.getAllProductsBought(loginUser.id, productListCondition, pageable)
            .let { ResponseEntity.ok(it) }
    }*/

}