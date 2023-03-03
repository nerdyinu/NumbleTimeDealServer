package com.example.numbletimedealserver.controller

import com.example.numblebankingserverchallenge.config.SessionLogin
import com.example.numblebankingserverchallenge.exception.CustomException
import com.example.numbletimedealserver.config.SessionAdmin
import com.example.numbletimedealserver.domain.ROLE
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
import java.util.*

@RestController
class ProductController(private val productService: ProductService, private val customerService: CustomerService) {
    //상품 : 삭제/목록/상세 기능
    @PostMapping("/register")
    fun register(productRegisterRequest: ProductRegisterRequest, @SessionAdmin admin: CustomerDto): ProductDto {
        return productService.register(admin.id, productRegisterRequest)
    }

    @PutMapping("/product/{productId}")
    fun update(
        @PathVariable("productId") productId: UUID,
        productUpdateRequest: ProductUpdateRequest,
        @SessionAdmin admin: CustomerDto
    ): ProductDto {
        return productService.update(productId, admin.id, productUpdateRequest)
    }

    @DeleteMapping("/product/{productId}")
    fun delete(@PathVariable("productId") productId: UUID, @SessionAdmin admin: CustomerDto) {

    }

    @GetMapping("/products/admin")
    fun productListAdmin(@SessionAdmin admin: CustomerDto, pageable: Pageable): ResponseEntity<Page<ProductDto>> {
        return productService.getAllProductsRegistered(admin.id, pageable).let { ResponseEntity.ok(it) }

    }

    @GetMapping("/products/user")
    fun productListUser(
        @SessionLogin loginUser: CustomerDto,
        productListCondition: ProductListCondition,
        pageable: Pageable
    ): ResponseEntity<Page<ProductDto>> {
        return productService.getAllProductsBought(loginUser.id, productListCondition, pageable)
            .let { ResponseEntity.ok(it) }
    }
}