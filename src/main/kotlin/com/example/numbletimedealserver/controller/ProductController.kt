package com.example.numbletimedealserver.controller

import com.example.numbletimedealserver.service.product.ProductService
import org.springframework.web.bind.annotation.RestController

@RestController
class ProductController(private val productService: ProductService) {
    //상품 : 등록/수정/삭제/목록/상세 기능
}