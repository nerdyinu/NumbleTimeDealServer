package com.example.numbletimedealserver.controller

import com.example.numblebankingserverchallenge.config.SessionLogin
import com.example.numblebankingserverchallenge.exception.CustomException
import com.example.numbletimedealserver.config.SessionAdmin
import com.example.numbletimedealserver.domain.ROLE
import com.example.numbletimedealserver.dto.CustomerDto
import com.example.numbletimedealserver.dto.ProductDto
import com.example.numbletimedealserver.request.ProductRegisterRequest
import com.example.numbletimedealserver.request.ProductUpdateRequest
import com.example.numbletimedealserver.service.customer.CustomerService
import com.example.numbletimedealserver.service.product.ProductService
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class ProductController(private val productService: ProductService, private val customerService: CustomerService) {
    //상품 : 등록/수정/삭제/목록/상세 기능
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
}