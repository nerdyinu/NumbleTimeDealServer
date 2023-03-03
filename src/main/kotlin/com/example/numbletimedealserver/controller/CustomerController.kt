package com.example.numbletimedealserver.controller

import com.example.numbletimedealserver.service.product.ProductService
import org.springframework.web.bind.annotation.RestController

@RestController
class CustomerController(private val productService: ProductService) {
    //회원 : 가입/탈퇴/조회 기능
    //

}