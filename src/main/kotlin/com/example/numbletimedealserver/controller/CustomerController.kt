package com.example.numbletimedealserver.controller

import com.example.numblebankingserverchallenge.config.SessionLogin
import com.example.numbletimedealserver.dto.CustomerDto
import com.example.numbletimedealserver.request.LoginRequest
import com.example.numbletimedealserver.request.SignUpRequest
import com.example.numbletimedealserver.service.customer.CustomerService
import com.example.numbletimedealserver.service.product.ProductService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class CustomerController(private val productService: ProductService, private val customerService: CustomerService) {
    //회원 : 가입/탈퇴/조회 기능
    //
    @PostMapping("/signup")
    fun signUp(signupRequest: SignUpRequest):ResponseEntity<CustomerDto>{
        return customerService.signup(signupRequest).let{ResponseEntity.ok(it)}
    }
    @PostMapping("/login")
    fun login(loginRequest: LoginRequest):ResponseEntity<CustomerDto>{
        return customerService.login(loginRequest).let { ResponseEntity.ok(it) }
    }
    @PostMapping("/resign")
    fun resign(@SessionLogin loggedinUser:CustomerDto):ResponseEntity<String>{
        customerService.resign(loggedinUser.id)
        return ResponseEntity.ok().build()
    }
    @GetMapping("/users")
    fun listUsers(pageable: Pageable):ResponseEntity<Page<CustomerDto>>{
        return customerService.getAll(pageable).let { ResponseEntity.ok(it) }
    }
}