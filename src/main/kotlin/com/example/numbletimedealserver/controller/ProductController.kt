package com.example.numbletimedealserver.controller


import com.example.numblebankingserverchallenge.config.SessionLogin
import com.example.numbletimedealserver.dto.CustomerDto
import com.example.numbletimedealserver.dto.ProductDto
import com.example.numbletimedealserver.request.ProductListCondition
import com.example.numbletimedealserver.request.ProductRegisterRequest
import com.example.numbletimedealserver.request.ProductUpdateRequest
import com.example.numbletimedealserver.service.customer.CustomerService
import com.example.numbletimedealserver.service.product.ProductService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

@RestController
class ProductController(private val productService: ProductService, private val customerService: CustomerService) {
    //상품 : 삭제/목록/상세 기능
    @PostMapping("/register")
    fun register(
        @RequestBody productRegisterRequest: ProductRegisterRequest,
        @SessionLogin(admin = true) admin: CustomerDto
    ): ProductDto = productService.register(admin.id, productRegisterRequest)


    @PutMapping("/product/{productId}")
    fun update(
        @PathVariable("productId") productId: UUID,
        @RequestBody productUpdateRequest: ProductUpdateRequest,
        @SessionLogin(admin = true) admin: CustomerDto
    ): ProductDto = productService.update(productId, admin.id, productUpdateRequest)


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
        @RequestParam params:Map<String,String> ,
        pageable: Pageable
    ): ResponseEntity<Page<ProductDto>> {
        val  productListCondition = ProductListCondition(LocalDate.parse(params.get("from")), LocalDate.parse(params.get("to")))
        return productService.getAllProductsBought(loginUser.id, productListCondition, pageable)
            .let { ResponseEntity.ok(it) }
    }
}